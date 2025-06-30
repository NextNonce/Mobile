package com.nextnonce.app.core.domain

sealed interface Result<out D, out E : Error> {
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : com.nextnonce.app.core.domain.Error>(val error: E) : Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return when (this) {
        is Result.Success  -> Result.Success(Unit)
        is Result.Error  -> this
    }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}