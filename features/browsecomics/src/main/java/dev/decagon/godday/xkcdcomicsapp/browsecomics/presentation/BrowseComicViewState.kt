package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event

/**
 * A simple data class for modelling the UI state of the BrowseComicFragment.
 * Ideally, a sealed class is more suitable for modelling states but since the state
 * we are modelling here is quite simple, a data class will serve the need.
 */
data class BrowseComicViewState(
    val loading: Boolean = true,
    val xkcdComic: XkcdComics? = null,
    val noMoreXkcdComics: Boolean = false,
    val failure: Event<Throwable>? = null
)