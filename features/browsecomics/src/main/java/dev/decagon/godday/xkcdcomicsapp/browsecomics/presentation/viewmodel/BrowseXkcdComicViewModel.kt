package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.decagon.godday.xkcdcomicsapp.browsecomics.domain.usecases.GetXkcdComic
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicEvent
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicViewState
import dev.decagon.godday.xkcdcomicsapp.common.domain.NetworkException
import dev.decagon.godday.xkcdcomicsapp.common.domain.NoMoreXkcdComicsException
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event
import dev.decagon.godday.xkcdcomicsapp.common.utils.DispatchersProvider
import dev.decagon.godday.xkcdcomicsapp.common.utils.createExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BrowseXkcdComicViewModel @Inject constructor(
    private val getXkcdComic: GetXkcdComic,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _state = MutableLiveData<BrowseComicViewState>()
    val state: LiveData<BrowseComicViewState> get() = _state

    private var getComicJob: Job = Job()

    init {
        _state.value = BrowseComicViewState()
    }

    fun onEvent(event: BrowseComicEvent) {
        getComicJob.cancel()

        when (event) {
            is BrowseComicEvent.RequestNextXkcdComic -> getXkcdComicById(event.index)
        }
    }

    private fun getXkcdComicById(id: Long) {
        val errorMessage = "Failed to load xkcd comic"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) {
            onFailure(it)
        }

        getComicJob = viewModelScope.launch(exceptionHandler) {
            val xkcdComic = withContext(dispatchersProvider.io()) { getXkcdComic(id) }

            onXkcdComicAvailable(xkcdComic)
        }
    }

    private fun onXkcdComicAvailable(xkcdComics: XkcdComics) {
        _state.value = state.value!!.copy(loading = false, xkcdComic = xkcdComics)
    }

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException -> {
                _state.value = state.value!!.copy(
                    loading = false,
                    failure = Event(failure)
                )
            }
            is NoMoreXkcdComicsException -> {
                _state.value = state.value!!.copy(
                    loading = false,
                    noMoreXkcdComics = true,
                    failure = Event(failure)
                )
            }
            else -> {
                _state.value = state.value!!.copy(
                    loading = false,
                    noMoreXkcdComics = false,
                    failure = Event(failure)
                )
            }
        }
    }

    fun clear() {
        getComicJob.cancel()
        onCleared()
    }
}