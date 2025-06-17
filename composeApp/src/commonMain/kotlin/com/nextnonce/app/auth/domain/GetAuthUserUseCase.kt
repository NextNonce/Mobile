package com.nextnonce.app.auth.domain

import com.nextnonce.app.auth.domain.model.AuthUser
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result

class GetAuthUserUseCase(private val repo: AuthRepository) {

    suspend fun execute(): Result<AuthUser, DataError.Local> {
        return repo.getAuthUser()
    }
}