package com.nextnonce.app.user.data.remote

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.data.remote.dto.CreateUserDto
import com.nextnonce.app.user.data.remote.dto.UserDto

interface RemoteUserDataSource {
    suspend fun get(): Result<UserDto, DataError.Remote>

    suspend fun create(createUserDto: CreateUserDto): Result<UserDto, DataError.Remote>
}