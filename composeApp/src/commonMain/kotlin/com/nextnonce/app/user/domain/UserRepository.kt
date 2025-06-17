package com.nextnonce.app.user.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.domain.model.UserModel

interface UserRepository {
    suspend fun create(user: UserModel): Result<UserModel, DataError.Remote>
    suspend fun get(): Result<UserModel, DataError.Remote>
}