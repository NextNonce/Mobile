package com.nextnonce.app.user.data.remote

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.data.remote.dto.CreateUserDto
import com.nextnonce.app.user.data.remote.dto.UserDto

/**
 * RemoteUserDataSource defines the contract for user-related remote data operations.
 * It provides methods to fetch user data and create a new user.
 */
interface RemoteUserDataSource {
    /**
     * Fetches the user data from the remote source.
     * @return Result containing UserDto on success or DataError on error.
     */
    suspend fun get(): Result<UserDto, DataError.Remote>

    /**
     * Creates a new user with the provided CreateUserDto.
     * @param createUserDto Data transfer object containing user creation details.
     * @return Result containing UserDto on success or DataError on error.
     */
    suspend fun create(createUserDto: CreateUserDto): Result<UserDto, DataError.Remote>
}