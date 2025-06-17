package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.auth.domain.model.AuthUser
import com.nextnonce.app.core.domain.DataError

class SignInWithEmailUseCase(
    private val repo: AuthRepository
) {

    suspend fun execute(email: String, password: String): Result<AuthUser, DataError> {
        return repo.signInWithEmail(email, password)
    }
}