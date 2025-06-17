package com.nextnonce.app.auth.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.logging.AppLogger
import io.ktor.client.plugins.auth.providers.BearerTokens

class RefreshBearerTokensUseCase(private val repo: AuthRepository) {
    suspend fun execute(): BearerTokens? {
        return when(repo.refreshCurrentSession()) {
            is Result.Success -> {
                return when(val authUser = repo.getAuthUser()) {
                    is Result.Success -> {
                        BearerTokens(
                            accessToken = authUser.data.accessToken,
                            refreshToken = authUser.data.refreshToken
                        )
                    }
                    is Result.Error -> {
                        AppLogger.e {
                            "Failed to get BearerTokens after session refresh: ${authUser.error}"
                        }
                        null
                    }
                }
            }
            is Result.Error -> {
                AppLogger.e {
                    "Failed to refresh session"
                }
                null
            }
        }
    }
}