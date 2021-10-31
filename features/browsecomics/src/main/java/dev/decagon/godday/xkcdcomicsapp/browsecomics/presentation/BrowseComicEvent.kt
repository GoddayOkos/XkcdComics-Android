package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation

sealed class BrowseComicEvent {
    data class RequestNextXkcdComic(val index: Long) : BrowseComicEvent()
}