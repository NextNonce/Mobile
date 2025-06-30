package com.nextnonce.app.core.network

import com.nextnonce.app.auth.domain.GetBearerTokensUseCase
import com.nextnonce.app.auth.domain.RefreshBearerTokensUseCase
import com.nextnonce.app.logging.KermitLoggerAdapter
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging


object HttpClientFactory {
    fun createBackend(
        engine: HttpClientEngine,
        getBearerTokensUseCase: GetBearerTokensUseCase,
        refreshBearerTokensUseCase: RefreshBearerTokensUseCase
    ): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json (JsonHumanReadable)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        getBearerTokensUseCase.execute()
                    }
                    refreshTokens {
                        refreshBearerTokensUseCase.execute()
                    }
                }
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L // 20 seconds
                requestTimeoutMillis = 20_000L // 20 seconds
            }
            install(HttpCache)
            install(Logging) {
                logger = KermitLoggerAdapter
                level = LogLevel.ALL
            }
            defaultRequest {
                url("https://api.nextnonce.com/v1/")
                contentType(ContentType.Application.Json)
            }
        }
    }
}