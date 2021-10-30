package dev.decagon.godday.xkcdcomicsapp.common.domain

/**
 * This is exception would be thrown when there are no more comics
 * on the endpoint i.e when the user has browsed to the end of the comics list
 */
class NoMoreXkcdComicsException(message: String): Exception(message)

/**
 * This exception would be thrown when we encounter network error
 */
class NetworkException(message: String) : Exception(message)