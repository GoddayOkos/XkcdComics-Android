package dev.decagon.godday.common.data.api

import dev.decagon.godday.common.domain.NetworkException
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
        throw NetworkException(exception.message ?: "Code ${exception.code()}")
    } catch (unknownException: Exception) {
        throw unknownException
    }
}