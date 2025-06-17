package com.nextnonce.app.auth.data

import com.nextnonce.app.auth.data.mapper.toAuthUser
import com.nextnonce.app.auth.domain.AuthRepository
import com.nextnonce.app.auth.domain.model.AuthUserModel
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.logging.AppLogger
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class AuthRepositoryImpl(
    private val auth: Auth
) : AuthRepository {

    override suspend fun getAuthUser(): Result<AuthUserModel, DataError.Local> {
        AppLogger.d {
            "Fetching current auth user"
        }
        return try {
            val userSession = getUserSession()
                ?: return Result.Error(DataError.Local.NO_SESSION)
            AppLogger.d {
                "User session found: ${userSession.user?.email ?: "No email"}"
            }
            Result.Success(userSession.toAuthUser())
        } catch (e: Exception) {
            AppLogger.e {
                "Error fetching auth user: ${e.message}"
            }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun refreshCurrentSession(): EmptyResult<DataError> {
        try {
            AppLogger.d {
                "Refreshing current session"
            }
            return Result.Success(auth.refreshCurrentSession())
        } catch (e: Exception) {
            AppLogger.e {
                "Error refreshing session: ${e.message}"
            }
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun isNewAuthUser(): Boolean? {
        AppLogger.d {
            "Checking if user is new"
        }
        val userSession = getUserSession()
            ?: return null // User is not signed in
        AppLogger.d {
            "User session found: ${userSession.user?.email ?: "No email"}"
        }
        val userCreatedAt = userSession.user?.createdAt
            ?: return null
        AppLogger.d {
            "User created at: $userCreatedAt"
        }
        val userLastSignInAt = userSession.user?.lastSignInAt
            ?: return null
        AppLogger.d {
            "User last sign-in at: $userLastSignInAt"
        }

        return userLastSignInAt - userCreatedAt <= 1.minutes
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<AuthUserModel, DataError> {
        AppLogger.d {
            "Signing up with email: $email"
        }
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            getAuthUser()
        } catch (e: AuthRestException) {
            when (e.errorCode) {
                AuthErrorCode.InvalidCredentials -> Result.Error(DataError.Remote.INVALID_CREDENTIALS)
                AuthErrorCode.UserAlreadyExists -> Result.Error(DataError.Remote.EMAIL_ALREADY_REGISTERED)
                AuthErrorCode.WeakPassword -> Result.Error(DataError.Remote.WEAK_PASSWORD)
                else -> {
                    AppLogger.e { "Unhandled auth error: ${e.errorCode}" }
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            AppLogger.e { "Unhandled sign-in error: ${e.message}" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<AuthUserModel, DataError> {
        AppLogger.d {
            "Signing in with email: $email"
        }
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            getAuthUser()
        } catch (e: AuthRestException) {
            when (e.errorCode) {
                AuthErrorCode.InvalidCredentials -> Result.Error(DataError.Remote.INVALID_CREDENTIALS)
                AuthErrorCode.UserAlreadyExists -> Result.Error(DataError.Remote.EMAIL_ALREADY_REGISTERED)
                AuthErrorCode.WeakPassword -> Result.Error(DataError.Remote.WEAK_PASSWORD)
                else -> {
                    AppLogger.e { "Unhandled auth error: ${e.errorCode}" }
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            AppLogger.e { "Unhandled sign-in error: ${e.message}" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun signInWithGoogle(): Result<AuthUserModel, DataError.Local> {
        return getAuthUser()
    }

    private suspend fun getUserSession(): UserSession? {
        AppLogger.d {
            "Awaiting auth initialization"
        }
        auth.awaitInitialization()
        AppLogger.d {
            "Auth initialized, checking current session"
        }
        val currentSession = auth.currentSessionOrNull()
        AppLogger.d {
            "Current session: ${currentSession?.user?.email ?: "No user"}"
        }
        if (currentSession == null) {
            AppLogger.d {
                "No current session found"
            }
            return null
        }
        if (currentSession.expiresIn.hours <= 24.hours) {
            AppLogger.d {
                "Current session is about to expire in ${currentSession.expiresIn.hours} hours"
            }
            return null
        }
        return currentSession
    }
}