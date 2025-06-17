package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.auth.domain.model.AuthUser
import com.nextnonce.app.core.domain.DataError

class SignUpWithEmailUseCase(
    private val repo: AuthRepository
) {

    suspend fun execute(email: String, password: String): Result<AuthUser, DataError> {
        return when (val authUser = repo.signUpWithEmail(email, password)) {
            is Result.Success -> authUser // should be called checking of new user and creating user
            is Result.Error -> Result.Error(authUser.error)
        }
    }
}