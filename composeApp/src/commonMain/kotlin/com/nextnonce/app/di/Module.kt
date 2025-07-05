package com.nextnonce.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

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

expect val platformModule: Module