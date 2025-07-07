package com.nextnonce.app.auth.domain

import com.nextnonce.app.auth.domain.model.AuthUserModel
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.core.domain.Result

/** * Repository interface for managing authentication-related operations.
 *
 * This interface defines methods for signing up, signing in, signing out,
 * and retrieving the current authenticated user.
 */
interface AuthRepository {
    /** null ⇒ user not signed in */
    suspend fun getAuthUser(): Result<AuthUserModel, DataError.Local>

    /**
     * Signs out the current user.
     */
    suspend fun signOut()

    /**
     * Refreshes the current user session.
     * Returns an EmptyResult indicating success or error.
     */
    suspend fun refreshCurrentSession(): EmptyResult<DataError>

    /**
     * Returns true if the user is new, false if the user was registered before.
     * null if the user is not signed in.
     */
    suspend fun isNewAuthUser(): Boolean?

    /**
     * Signs up a new user with the provided email and password.
     */
    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUserModel, DataError>
    /**
     * Signs in an existing user with the provided email and password.
     */
    suspend fun signInWithEmail(email: String, password: String): Result<AuthUserModel, DataError>
    /**
     * Signs in with Google authentication.
     * Returns a Result containing the authenticated user model or an error.
     * This method is intended for use with Google Sign-In integration.
     * Is implemented only for Android, for iOS it returns an error.
     */
    suspend fun signInWithGoogle(): Result<AuthUserModel, DataError.Local>
}