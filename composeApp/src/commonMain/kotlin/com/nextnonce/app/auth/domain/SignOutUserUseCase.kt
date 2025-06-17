package com.nextnonce.app.auth.domain

class SignOutUserUseCase(private val repo: AuthRepository) {
    suspend fun execute() {
        repo.signOut()
    }
}