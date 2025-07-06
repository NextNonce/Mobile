package com.nextnonce.app.di

import com.nextnonce.app.core.network.HttpClientFactory
import com.nextnonce.app.core.network.backendHttpClient
import com.nextnonce.app.core.network.nnKamelConfig
import io.kamel.core.config.KamelConfig
import org.koin.dsl.module

/**
 * Koin module for core dependencies.
 * This module provides the HTTP client and Kamel configuration for the application.
 */
val coreModule = module {
    single(backendHttpClient) {
        HttpClientFactory.createBackend(get(), get(), get())
    }
    single<KamelConfig> { nnKamelConfig }
}