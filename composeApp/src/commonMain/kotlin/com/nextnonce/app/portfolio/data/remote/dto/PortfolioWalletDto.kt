package com.nextnonce.app.portfolio.data.remote.dto

import com.nextnonce.app.core.domain.chain.ChainType
import com.nextnonce.app.core.domain.wallet.WalletType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class PortfolioWalletDto(
    val address: String,
    val name: String? = null,
    val walletType: WalletType,
    val chainType: ChainType,
    val createdAt: Instant,
    val updatedAt: Instant,
)
