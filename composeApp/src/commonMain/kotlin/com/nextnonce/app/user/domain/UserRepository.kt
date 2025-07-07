package com.nextnonce.app.user.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.domain.model.UserModel

/**
 * UserRepository interface defines the contract for user-related operations.
 */
interface UserRepository {
    /**
     * Creates a new user with the provided UserModel.
     * @param user The user model containing user information.
     * @return Result containing UserModel on success or DataError on error.
     */
    suspend fun create(user: UserModel): Result<UserModel, DataError.Remote>
    /**
     * Fetches the user data.
     * @return Result containing UserModel on success or DataError on error.
     */
    suspend fun get(): Result<UserModel, DataError.Remote>
}