package com.nextnonce.app.auth.domain

import com.nextnonce.app.auth.domain.model.AuthUserModel
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.core.domain.Result

interface AuthRepository {
    /** null ⇒ user not signed in */
    suspend fun getAuthUser(): Result<AuthUserModel, DataError.Local>

    suspend fun signOut()

    suspend fun refreshCurrentSession(): EmptyResult<DataError>

    /**
     * Returns true if the user is new, false if the user was registered before.
     * null if the user is not signed in.
     */
    suspend fun isNewAuthUser(): Boolean?

    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUserModel, DataError>
    suspend fun signInWithEmail(email: String, password: String): Result<AuthUserModel, DataError>
    /** no-op on iOS; native sheet on Android via ComposeAuth */
    suspend fun signInWithGoogle(): Result<AuthUserModel, DataError.Local>
}