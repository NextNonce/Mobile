package com.nextnonce.app.core.network

import com.nextnonce.app.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json


object HttpClientFactory {
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json (JsonHumanReadable)
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L // 20 seconds
                requestTimeoutMillis = 20_000L // 20 seconds
            }
            install(HttpCache)
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}