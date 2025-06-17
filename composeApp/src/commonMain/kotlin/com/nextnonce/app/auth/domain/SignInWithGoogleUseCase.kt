package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.auth.domain.model.AuthUserModel
import com.nextnonce.app.auth.domain.model.toUserModel
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.platform.Platform
import com.nextnonce.app.core.platform.currentPlatform
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.user.domain.CreateUserUseCase

class SignInWithGoogleUseCase(
    private val repo: AuthRepository,
    private val createUserUseCase: CreateUserUseCase
) {

    suspend fun execute(): Result<AuthUserModel, DataError> {
        if (currentPlatform !is Platform.Android) {
            AppLogger.e { "SignInWithGoogleUseCase can only be executed on Android platform." }
            return Result.Error(
                DataError.Remote.UNKNOWN
            )
        }
        return when (val authUser = repo.signInWithGoogle()) {
            is Result.Success -> {
                val isNewUser = repo.isNewAuthUser() ?: true
                if (isNewUser) {
                    // If it's a new user, we can create a user model from the auth user
                    when ( val user = createUserUseCase.execute(authUser.data.toUserModel())) {
                        is Result.Success -> {}  // User creation was successful,
                        is Result.Error -> {
                            AppLogger.e { "Failed to create user after sign in with google: ${user.error}" }
                            Result.Error(user.error)
                        }
                    }
                }
                authUser
            }
            is Result.Error -> Result.Error(authUser.error)
        }
    }
}