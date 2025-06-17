package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.auth.domain.model.AuthUserModel
import com.nextnonce.app.core.domain.DataError

class SignInWithEmailUseCase(
    private val repo: AuthRepository
) {

    suspend fun execute(email: String, password: String): Result<AuthUserModel, DataError> {
        return repo.signInWithEmail(email, password)
    }
}