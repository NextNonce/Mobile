package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.auth.domain.model.AuthUser
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.platform.Platform
import com.nextnonce.app.core.platform.currentPlatform

class SignInWithGoogleUseCase(
    private val repo: AuthRepository,
) {

    suspend fun execute(): Result<AuthUser, DataError> {
        return if (currentPlatform is Platform.Android) {
            when (val authUser = repo.signInWithGoogle()) {
                is Result.Success -> authUser // should be called checking of new user and creating user
                is Result.Error -> Result.Error(authUser.error)
            }
        } else {
            Result.Error(
                DataError.Remote.UNKNOWN
            )
        }
    }
}