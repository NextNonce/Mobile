package com.nextnonce.app.portfolio.presentation

import com.nextnonce.app.balance.presentation.UIAssetBalanceListItem
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the state of the portfolio screen.
 *
 * @property isLoading Indicates whether the portfolio data is currently being loaded.
 * @property uiPortfolioInfo The UI model for portfolio information, or null if not available.
 * @property uiPortfolioTotalBalance The UI model for the total balance of the portfolio.
 * @property assetBalances A list of UI models representing asset balances in the portfolio.
 * @property error An optional error message to display if an error occurs while loading the portfolio.
 */
data class PortfolioState(
    val isLoading: Boolean = true,
    val uiPortfolioInfo: UIPortfolioInfo? = null,
    val uiPortfolioTotalBalance: UIPortfolioTotalBalance = UIPortfolioTotalBalance(),
    val assetBalances: List<UIAssetBalanceListItem> = emptyList(),
    val error: StringResource? = null,
)