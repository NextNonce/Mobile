package com.nextnonce.app.portfolio.domain.model

import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.core.domain.wallet.WalletType
import kotlinx.datetime.Instant


/**
 * Represents a model for a portfolio wallet containing its details and timestamps.
 * @property wallet The wallet details.
 * @property name An optional name for the wallet.
 * @property walletType The type of the wallet (e.g., simple, smart).
 * @property createdAt The timestamp when the wallet was created.
 * @property updatedAt The timestamp when the wallet was last updated.
 */
data class PortfolioWalletModel(
    val wallet: Wallet,
    val name: String?,
    val walletType: WalletType,
    val createdAt: Instant,
    val updatedAt: Instant,
)
