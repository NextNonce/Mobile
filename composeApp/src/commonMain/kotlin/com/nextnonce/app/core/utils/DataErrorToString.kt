package com.nextnonce.app.core.utils

import com.nextnonce.app.core.domain.DataError
import nextnonce.composeapp.generated.resources.Res
import nextnonce.composeapp.generated.resources.error_account_exists
import nextnonce.composeapp.generated.resources.error_conflict
import nextnonce.composeapp.generated.resources.error_disk_full
import nextnonce.composeapp.generated.resources.error_forbidden
import nextnonce.composeapp.generated.resources.error_invalid_credentials
import nextnonce.composeapp.generated.resources.error_no_internet
import nextnonce.composeapp.generated.resources.error_no_session
import nextnonce.composeapp.generated.resources.error_not_found
import nextnonce.composeapp.generated.resources.error_request_timeout
import nextnonce.composeapp.generated.resources.error_serialization
import nextnonce.composeapp.generated.resources.error_too_many_requests
import nextnonce.composeapp.generated.resources.error_unauthorized
import nextnonce.composeapp.generated.resources.error_unknown
import nextnonce.composeapp.generated.resources.error_weak_password
import org.jetbrains.compose.resources.StringResource


fun DataError.toUIText(): StringResource {
    val stringRes = when (this) {
        is DataError.Local -> when (this) {
            DataError.Local.DISK_FULL -> Res.string.error_disk_full
            DataError.Local.NO_SESSION -> Res.string.error_no_session
            DataError.Local.UNKNOWN -> Res.string.error_unknown
        }
        is DataError.Remote -> when (this) {
            DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
            DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
            DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
            DataError.Remote.SERVER -> Res.string.error_unknown // Assuming server error is treated as unknown
            DataError.Remote.SERIALIZATION -> Res.string.error_serialization
            DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorized
            DataError.Remote.FORBIDDEN -> Res.string.error_forbidden
            DataError.Remote.NOT_FOUND -> Res.string.error_not_found
            DataError.Remote.CONFLICT -> Res.string.error_conflict
            DataError.Remote.INVALID_CREDENTIALS -> Res.string.error_invalid_credentials
            DataError.Remote.EMAIL_ALREADY_REGISTERED -> Res.string.error_account_exists
            DataError.Remote.WEAK_PASSWORD -> Res.string.error_weak_password
            DataError.Remote.UNKNOWN -> Res.string.error_unknown
        }
    }
    return stringRes
}