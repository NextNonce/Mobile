package com.nextnonce.app.core.database.wallet

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nextnonce.app.core.utils.BigDecimalConverters
import com.nextnonce.app.wallet.data.local.WalletBalancesCacheDao
import com.nextnonce.app.wallet.data.local.WalletBalancesCacheEntity

/**
 * WalletDatabase is the Room database for storing wallet balances.
 * It contains a single table for caching wallet balances.
 */
@Database(entities = [
    WalletBalancesCacheEntity::class],
    version = 1)
@TypeConverters(BigDecimalConverters::class)
@ConstructedBy(WalletDatabaseCreator::class)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun walletBalancesCacheDao(): WalletBalancesCacheDao
}