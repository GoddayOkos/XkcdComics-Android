package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics

sealed class BrowseComicEvent {
    data class RequestNextXkcdComic(val index: Long) : BrowseComicEvent()
    data class AddComicToFavorite(val comic: XkcdComics) : BrowseComicEvent()
}