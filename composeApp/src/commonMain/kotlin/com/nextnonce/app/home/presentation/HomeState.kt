package com.nextnonce.app.home.presentation

import org.jetbrains.compose.resources.StringResource

/**
 * Represents the state of the Home screen in the application.
 *
 * @property portfolio The default portfolio item displayed on the home screen.
 * @property wallets A list of wallet items associated with default portfolio.
 * @property error An optional error message to display if something goes wrong.
 * @property areWalletsLoading Indicates whether the wallets are currently being loaded.
 */
data class HomeState(
    val portfolio: UIHomePortfolioItem = UIHomePortfolioItem(),
    val wallets: List<UIHomeWalletItem> = emptyList(),
    val error: StringResource? = null,
    val areWalletsLoading: Boolean = false,
)
