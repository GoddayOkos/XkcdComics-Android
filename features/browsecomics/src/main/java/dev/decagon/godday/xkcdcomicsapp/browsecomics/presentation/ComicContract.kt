package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation

interface ComicContract {
    fun onEndOfComicsReached(index: Int)
}