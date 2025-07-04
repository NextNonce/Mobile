package com.nextnonce.app.core.database.wallet

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

fun getWalletDatabaseBuilder(): RoomDatabase.Builder<WalletDatabase> {
    val dbDirectory = NSSearchPathForDirectoriesInDomains(
        directory = NSDocumentDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).first() as String

    // 2. Append the database file name to the directory path
    val dbFile = "$dbDirectory/wallet.db"

    // 3. Build the database with the correct, writable path
    return Room.databaseBuilder<WalletDatabase>(
        name = dbFile
    )
}