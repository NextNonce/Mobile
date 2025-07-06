package com.nextnonce.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes Koin with the provided configuration and modules.
 *
 * @param config Optional KoinAppDeclaration to configure Koin.
 */
fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            platformModule,
            authModule,
            startModule,
            coreModule,
            userModule,
            walletModule,
            portfolioModule,
            homeModule,
        )
    }

/**
 * Koin module for platform-specific dependencies.
 * This module is expected to be provided by the platform implementation.
 */
expect val platformModule: Module