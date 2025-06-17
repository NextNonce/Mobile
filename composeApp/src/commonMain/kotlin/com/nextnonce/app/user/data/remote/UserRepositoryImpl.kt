package com.nextnonce.app.user.data.remote

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.data.mapper.toCreateUserDto
import com.nextnonce.app.user.data.mapper.toUserModel
import com.nextnonce.app.user.domain.UserRepository
import com.nextnonce.app.user.domain.model.UserModel

class UserRepositoryImpl(
    private val remoteDataSource: RemoteUserDataSource
) : UserRepository {

    override suspend fun create(user: UserModel): Result<UserModel, DataError.Remote> {
        return when (val res = remoteDataSource.create(user.toCreateUserDto())) {
            is Result.Success -> Result.Success(res.data.toUserModel())
            is Result.Error   -> Result.Error(res.error)
        }
    }

    override suspend fun get(): Result<UserModel, DataError.Remote> {
        return when (val res = remoteDataSource.get()) {
            is Result.Success -> Result.Success(res.data.toUserModel())
            is Result.Error   -> Result.Error(res.error)
        }
    }
}