package com.nextnonce.app.user.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.domain.model.UserModel

class CreateUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun execute(userModel: UserModel): Result<UserModel, DataError.Remote> {
        return userRepository.create(userModel)
    }
}