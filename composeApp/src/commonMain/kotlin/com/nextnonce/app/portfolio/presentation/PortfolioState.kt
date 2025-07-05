package com.nextnonce.app.portfolio.presentation

import com.nextnonce.app.balance.presentation.UIAssetBalanceListItem
import org.jetbrains.compose.resources.StringResource

data class PortfolioState(
    val isLoading: Boolean = true,
    val uiPortfolioInfo: UIPortfolioInfo? = null,
    val uiPortfolioTotalBalance: UIPortfolioTotalBalance = UIPortfolioTotalBalance(),
    val assetBalances: List<UIAssetBalanceListItem> = emptyList(),
    val error: StringResource? = null,
)