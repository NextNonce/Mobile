package com.nextnonce.app.wallet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nextnonce.app.core.network.JsonHumanReadable
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto

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