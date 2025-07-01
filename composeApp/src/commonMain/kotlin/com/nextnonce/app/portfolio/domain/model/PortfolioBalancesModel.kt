package com.nextnonce.app.portfolio.domain.model

import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.balance.TotalBalance

data class PortfolioBalancesModel(
    val actual: Boolean,
    val totalBalance: TotalBalance,
    val assetBalances: List<AssetBalance> = emptyList()
)
