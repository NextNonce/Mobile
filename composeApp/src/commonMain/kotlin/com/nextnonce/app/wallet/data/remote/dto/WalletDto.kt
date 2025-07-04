package com.nextnonce.app.wallet.data.remote.dto

import com.nextnonce.app.core.domain.chain.ChainType
import com.nextnonce.app.core.domain.wallet.WalletType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class WalletDto(
    val address: String,
    val walletType: WalletType,
    val chainType: ChainType,
    val createdAt: Instant,
    val updatedAt: Instant,
)