package com.nextnonce.app.home.presentation

import org.jetbrains.compose.resources.StringResource

data class HomeState(
    val portfolio: UIHomePortfolioItem = UIHomePortfolioItem(),
    val wallets: List<UIHomeWalletItem> = emptyList(),
    val error: StringResource? = null,
    val areWalletsLoading: Boolean = false,
)
