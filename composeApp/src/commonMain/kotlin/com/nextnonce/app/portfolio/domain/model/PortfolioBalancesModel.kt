package com.nextnonce.app.portfolio.domain.model

import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.balance.TotalBalance

/**
 * Represents the portfolio balances model containing total balance and asset balances.
 *
 * @property actual Indicates if the balances are actual or not.
 * @property totalBalance The total balance of the portfolio.
 * @property assetBalances A list of asset balances in the portfolio.
 */
data class PortfolioBalancesModel(
    val actual: Boolean,
    val totalBalance: TotalBalance,
    val assetBalances: List<AssetBalance> = emptyList()
)
