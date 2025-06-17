package com.nextnonce.app.user.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.user.domain.model.UserModel

class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend fun execute(): Result<UserModel, DataError.Remote> {
        return userRepository.get()
    }
}