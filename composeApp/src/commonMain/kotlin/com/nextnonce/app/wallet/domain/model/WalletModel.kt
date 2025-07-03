package com.nextnonce.app.wallet.domain.model

import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.core.domain.wallet.WalletType

data class WalletModel(
    val wallet: Wallet,
    val walletType: WalletType,
)
