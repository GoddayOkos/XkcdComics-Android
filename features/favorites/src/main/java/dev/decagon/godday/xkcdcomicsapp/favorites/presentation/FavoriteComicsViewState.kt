package dev.decagon.godday.xkcdcomicsapp.favorites.presentation

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics

sealed class FavoriteComicsViewState {
    object Loading: FavoriteComicsViewState()
    data class FavoriteComics(val results: List<XkcdComics>) : FavoriteComicsViewState()
    object NoFavoriteComics : FavoriteComicsViewState()
}