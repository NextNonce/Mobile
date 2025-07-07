package com.nextnonce.app.wallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nextnonce.app.core.network.JsonHumanReadable
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto

/**
 * Represents a cached entity for wallet balances.
 *
 * @property walletId The unique identifier of the wallet.
 * @property timestamp The timestamp when the balances were cached.
 * @property balancesJson The JSON representation of the wallet balances.
 */
@Entity
data class WalletBalancesCacheEntity(
    @PrimaryKey
    val walletId: String,
    val timestamp: Long,
    val balancesJson: String
) {
    fun toDto(): WalletBalancesDto =
        JsonHumanReadable.decodeFromString(balancesJson)

    constructor(walletId: String, timestamp: Long, dto: WalletBalancesDto) : this(
        walletId = walletId,
        timestamp = timestamp,
        balancesJson = JsonHumanReadable.encodeToString(dto)
    )
}