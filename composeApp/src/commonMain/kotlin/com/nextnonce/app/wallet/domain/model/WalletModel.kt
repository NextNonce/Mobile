package com.nextnonce.app.wallet.domain.model

import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.core.domain.wallet.WalletType

/**
 * Represents the wallet model containing wallet information and its type.
 *
 * @property wallet The wallet object containing wallet details.
 * @property walletType The type of the wallet (e.g., SIMPLE, SMART).
 */
data class WalletModel(
    val wallet: Wallet,
    val walletType: WalletType,
)
