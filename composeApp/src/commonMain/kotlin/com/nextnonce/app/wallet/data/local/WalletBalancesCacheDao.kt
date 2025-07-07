package com.nextnonce.app.wallet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) for accessing cached wallet balances in the local database.
 *
 * This DAO provides methods to retrieve and update cached balances for a specific wallet.
 */
@Dao
interface WalletBalancesCacheDao {

    /**
     * Retrieves the cached balances for a specific wallet by its ID.
     *
     * @param walletId The ID of the wallet for which to retrieve cached balances.
     * @return The cached balances for the specified wallet, or null if no cache exists.
     */
    @Query("SELECT * FROM WalletBalancesCacheEntity WHERE walletId = :walletId")
    suspend fun getBalancesCache(walletId: String): WalletBalancesCacheEntity?

    /**
     * Inserts or updates the cached balances for a specific wallet.
     *
     * If a cache entry for the specified wallet already exists, it will be replaced.
     * Otherwise, a new cache entry will be created.
     *
     * @param entity The WalletBalancesCacheEntity containing the cached balances to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBalancesCache(entity: WalletBalancesCacheEntity)
}