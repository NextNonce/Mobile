package com.nextnonce.app.core.domain

sealed interface DataError : Error {
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

    enum class Local : DataError {
        DISK_FULL,
        NO_SESSION,
        UNKNOWN
    }
}