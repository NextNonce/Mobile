package com.nextnonce.app.core.domain

/**
 * Represents an error that can occur in the data layer.
 *
 * This interface is used to define various types of errors that can occur during data operations,
 * such as network issues, serialization problems, or local storage errors.
 */
sealed interface DataError : Error {

    /**
     * Represents remote data errors that can occur, such as network issues or server errors.
     */
    enum class Remote : DataError {
        // Network
        NO_INTERNET,
        SERIALIZATION,

        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        REQUEST_TIMEOUT,
        CONFLICT,
        TOO_MANY_REQUESTS,

        SERVER,

        // Auth
        INVALID_CREDENTIALS,
        EMAIL_ALREADY_REGISTERED,
        WEAK_PASSWORD,

        UNKNOWN,
    }

    /**
     * Represents local data errors that can occur, such as disk full or no session.
     */
    enum class Local : DataError {
        DISK_FULL,
        NO_SESSION,
        UNKNOWN
    }
}