package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.logging.AppLogger
import io.ktor.client.plugins.auth.providers.BearerTokens

class GetBearerTokensUseCase(private val repo: AuthRepository) {
    suspend fun execute(): BearerTokens? {
        return when(val authUser = repo.getAuthUser()) {
            is Result.Success -> {
                BearerTokens(
                    accessToken = authUser.data.accessToken,
                    refreshToken = authUser.data.refreshToken
                )
            }
            is Result.Error -> {
                AppLogger.e {
                    "Failed to get BearerTokens: ${authUser.error}"
                }
                null
            }
        }
    }
}