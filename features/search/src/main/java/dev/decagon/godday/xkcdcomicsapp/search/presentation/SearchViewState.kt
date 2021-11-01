package dev.decagon.godday.xkcdcomicsapp.search.presentation

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event

sealed class SearchViewState {
    object NoSearchQuery : SearchViewState()
    object Searching : SearchViewState()
    data class SearchResults(val results: List<XkcdComics>) : SearchViewState()
    object NoResults : SearchViewState()
    data class InvalidQuery(val message: String) : SearchViewState()
    data class Failure(val error: Event<Throwable>): SearchViewState()
}