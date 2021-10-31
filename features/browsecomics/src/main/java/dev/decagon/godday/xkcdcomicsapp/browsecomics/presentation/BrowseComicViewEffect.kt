package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation

sealed class BrowseComicViewEffect {
    object OnFavoriteComicAdded : BrowseComicViewEffect()
}