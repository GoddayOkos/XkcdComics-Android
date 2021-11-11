package dev.decagon.godday.xkcdcomicsapp.common.data.api

import dev.decagon.godday.xkcdcomicsapp.common.domain.NetworkException
import dev.decagon.godday.xkcdcomicsapp.common.domain.NoMoreXkcdComicsException
import retrofit2.HttpException

/**
 * An abstraction of try and catch block providing a clean and safe API for
 * making network calls
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): T {
    return try {
        apiCall()
    } catch (exception: HttpException) {
        exception.printStackTrace()
        throw if (exception.code() == 404) {
            NoMoreXkcdComicsException("No more comic to display")
        } else {
            NetworkException(exception.message ?: "Code ${exception.code()}")
        }
    } catch (unknownException: Exception) {
        throw unknownException
    }
}