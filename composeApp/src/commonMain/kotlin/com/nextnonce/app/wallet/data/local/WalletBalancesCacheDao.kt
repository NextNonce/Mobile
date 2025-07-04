package com.nextnonce.app.wallet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface WalletBalancesCacheDao {

    @Query("SELECT * FROM WalletBalancesCacheEntity WHERE walletId = :walletId")
    suspend fun getBalancesCache(walletId: String): WalletBalancesCacheEntity?

    /** Inserts or replaces the cached balances for this wallet. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBalancesCache(entity: WalletBalancesCacheEntity)
}