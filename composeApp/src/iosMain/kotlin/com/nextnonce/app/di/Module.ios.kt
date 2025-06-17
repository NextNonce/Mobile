package com.nextnonce.app.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule = module {
    // core
    single<HttpClientEngine> {
        Darwin.create()
    }
}