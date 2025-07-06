package com.nextnonce.app.core.network

import com.nextnonce.app.auth.domain.GetBearerTokensUseCase
import com.nextnonce.app.auth.domain.RefreshBearerTokensUseCase
import com.nextnonce.app.core.utils.API_BASE_URL
import com.nextnonce.app.core.utils.REQUEST_TIMEOUT_MILLIS
import com.nextnonce.app.core.utils.SOCKET_TIMEOUT_MILLIS
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
    /**
     * Creates an instance of [HttpClient] configured for the backend.
     *
     * @param engine The HTTP client engine to use.
     * @param getBearerTokensUseCase Use case to retrieve bearer tokens.
     * @param refreshBearerTokensUseCase Use case to refresh bearer tokens.
     * @return Configured [HttpClient] instance.
     */
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
                socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
                requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
            }
            install(HttpCache)
            install(Logging) {
                logger = KermitLoggerAdapter
                level = LogLevel.ALL
            }
            defaultRequest {
                url(API_BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}