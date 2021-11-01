package dev.decagon.godday.xkcdcomicsapp.search.presentation

import kotlinx.coroutines.flow.StateFlow

sealed class SearchEvent {
    data class QueryInput(val query: StateFlow<String>) : SearchEvent()
}