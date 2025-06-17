package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result

class IsUserSignedInUseCase(private val repo: AuthRepository) {

    suspend fun execute(): Boolean {
        return when (repo.getAuthUser()) {
            is Result.Success -> true
            is Result.Error -> false
        }
    }
}