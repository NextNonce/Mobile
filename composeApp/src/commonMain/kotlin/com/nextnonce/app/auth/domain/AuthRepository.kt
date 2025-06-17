package com.nextnonce.app.auth.domain

import com.nextnonce.app.auth.domain.model.AuthUser
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result

interface AuthRepository {
    /** null ⇒ user not signed in */
    suspend fun getAuthUser(): Result<AuthUser, DataError.Local>

    suspend fun signOut()

    /**
     * Returns true if the user is new, false if the user was registered before.
     * null if the user is not signed in.
     */
    suspend fun isNewAuthUser(): Boolean?

    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser, DataError>
    suspend fun signInWithEmail(email: String, password: String): Result<AuthUser, DataError>
    /** no-op on iOS; native sheet on Android via ComposeAuth */
    suspend fun signInWithGoogle(): Result<AuthUser, DataError.Local>
}