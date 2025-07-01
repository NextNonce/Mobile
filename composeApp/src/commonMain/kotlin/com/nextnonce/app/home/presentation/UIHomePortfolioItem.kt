package com.nextnonce.app.home.presentation

import com.nextnonce.app.core.enums.NumberSign

data class UIHomePortfolioItem(
    val id: String? = null,
    val name: String = "Portfolio",
    val formatedBalanceQuote: String? = null,
    val formatedChangePercent: String? = null,
    val changePercentSign: NumberSign? = null,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}
