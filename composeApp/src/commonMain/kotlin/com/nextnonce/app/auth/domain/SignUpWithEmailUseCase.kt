package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.auth.domain.model.AuthUserModel
import com.nextnonce.app.auth.domain.model.toUserModel
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.user.domain.CreateUserUseCase

class SignUpWithEmailUseCase(
    private val repo: AuthRepository,
    private val createUserUseCase: CreateUserUseCase
) {

    suspend fun execute(email: String, password: String): Result<AuthUserModel, DataError> {
        return when (val authUser = repo.signUpWithEmail(email, password)) {
            is Result.Success -> {
                when (val user = createUserUseCase.execute(authUser.data.toUserModel())) {
                    is Result.Success -> authUser // User creation was successful, return authUser
                    is Result.Error -> {
                        // If user creation failed, we can log the error and return it
                        AppLogger.e { "Failed to create user after sign up: ${user.error}" }
                        Result.Error(user.error)
                    }
                }
            }
            is Result.Error -> Result.Error(authUser.error)
        }
    }
}