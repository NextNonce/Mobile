package com.nextnonce.app.auth.domain

class IsNewUserUseCase(private val repo: AuthRepository) {

    suspend fun execute(): Boolean {
        return repo.isNewAuthUser() ?: true
    }
}