package com.example.campusconnect.util

import kotlinx.coroutines.CancellationException

suspend fun <T> safeCall(
    action: suspend () -> T
): Result<T> {
    return try {
        Result.success(action())
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}