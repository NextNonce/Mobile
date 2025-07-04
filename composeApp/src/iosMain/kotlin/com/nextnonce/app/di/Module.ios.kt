package com.nextnonce.app.di

import androidx.room.RoomDatabase
import com.nextnonce.app.core.database.wallet.WalletDatabase
import com.nextnonce.app.core.database.wallet.getWalletDatabaseBuilder
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    // core
    single<HttpClientEngine> {
        Darwin.create()
    }

    // database
    singleOf(::getWalletDatabaseBuilder).bind<RoomDatabase.Builder<WalletDatabase>>()
}