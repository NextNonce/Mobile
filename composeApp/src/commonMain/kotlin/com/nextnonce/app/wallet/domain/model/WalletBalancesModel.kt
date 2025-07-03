package com.nextnonce.app.wallet.domain.model

import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.balance.TotalBalance

data class WalletBalancesModel(
    val totalBalance: TotalBalance,
    val assetBalances: List<AssetBalance> = emptyList()
)
