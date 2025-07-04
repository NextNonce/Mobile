package com.nextnonce.app.core.database.wallet

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object WalletDatabaseCreator : RoomDatabaseConstructor<WalletDatabase>

fun getWalletDatabase(
    builder: RoomDatabase.Builder<WalletDatabase>
): WalletDatabase {
    return builder
        // .addMigrations(// Add migrations here if needed)
        // .fallbackToDestructiveMigration()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}