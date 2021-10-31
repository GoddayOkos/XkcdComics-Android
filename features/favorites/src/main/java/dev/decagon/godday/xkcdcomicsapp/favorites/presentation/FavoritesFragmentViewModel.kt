package dev.decagon.godday.xkcdcomicsapp.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.decagon.godday.xkcdcomicsapp.favorites.domain.usecases.GetFavoriteComicsFromDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesFragmentViewModel @Inject constructor(
    private val getFavoriteComicsFromDB: GetFavoriteComicsFromDB,
) : ViewModel() {

    private val _state = MutableLiveData<FavoriteComicsViewState>()
    val state: LiveData<FavoriteComicsViewState> get() = _state

    init {
        _state.value = FavoriteComicsViewState.Loading
        subscribeToFavoriteComicsUpdate()
    }

    private fun subscribeToFavoriteComicsUpdate() {
        viewModelScope.launch {
            getFavoriteComicsFromDB()
                .flowOn(Dispatchers.Default)
                .collect {
                    if (it.isEmpty()) {
                        _state.value = FavoriteComicsViewState.NoFavoriteComics
                    } else {
                        _state.value = FavoriteComicsViewState.FavoriteComics(it)
                    }
                }
        }
    }

}