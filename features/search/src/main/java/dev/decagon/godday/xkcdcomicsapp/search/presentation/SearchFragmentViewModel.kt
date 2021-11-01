package dev.decagon.godday.xkcdcomicsapp.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event
import dev.decagon.godday.xkcdcomicsapp.common.utils.DispatchersProvider
import dev.decagon.godday.xkcdcomicsapp.search.domain.usecases.SearchComics
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val searchComics: SearchComics,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _state: MutableLiveData<SearchViewState> = MutableLiveData()
    val state: LiveData<SearchViewState> get() =  _state

    private var searchJob: Job? = null

    init {
        _state.value = SearchViewState.NoSearchQuery
    }


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryInput -> updateQuery(event.query)
        }
    }

    private fun updateQuery(query: StateFlow<String>) {
        searchJob = viewModelScope.launch {
            query.debounce(500L)
                .filter { str ->
                    when {
                        str.isBlank() -> {
                            _state.postValue(SearchViewState.NoSearchQuery)
                            return@filter false
                        }
                        validateQuery(str) -> {
                            _state.postValue(SearchViewState.InvalidQuery("Please enter a valid search query"))
                            return@filter false
                        }
                        else -> {
                            _state.postValue(SearchViewState.Searching)
                            return@filter true
                        }
                    }
                }
                .flatMapLatest { str ->
                    searchComics(str)
                        .catch { error ->
                            _state.postValue(SearchViewState.Failure(Event(error)))
                        }
                }
                .flowOn(dispatchersProvider.io())
                .collect {
                    if (it.isEmpty()) {
                        _state.value = SearchViewState.NoResults
                    } else {
                        _state.value = SearchViewState.SearchResults(it)
                    }
                }
        }
    }

    // Check if query matches string or numbers
    private fun validateQuery(query: String): Boolean {
        return query.matches("\\W".toRegex())
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}