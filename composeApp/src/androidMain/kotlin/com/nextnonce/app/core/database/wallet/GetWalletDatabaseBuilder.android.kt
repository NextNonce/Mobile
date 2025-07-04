package com.nextnonce.app.core.database.wallet

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getWalletDatabaseBuilder(context: Context): RoomDatabase.Builder<WalletDatabase> {
    val dbFile = context.getDatabasePath("wallet.db")
    return Room.databaseBuilder<WalletDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}