package dev.decagon.godday.xkcdcomicsapp.common.presentation

data class Event<out T>(private val content: T) {

    private var hasBeenHandle = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandle) {
            null
        } else {
            hasBeenHandle = true
            content
        }
    }
}