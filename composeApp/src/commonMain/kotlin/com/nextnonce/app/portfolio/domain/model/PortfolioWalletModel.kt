package com.nextnonce.app.portfolio.domain.model

import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.core.domain.wallet.WalletType
import kotlinx.datetime.Instant


data class PortfolioWalletModel(
    val wallet: Wallet,
    val name: String?,
    val walletType: WalletType,
    val createdAt: Instant,
    val updatedAt: Instant,
)
