package com.nextnonce.app.portfolio.presentation

import com.nextnonce.app.core.enums.NumberSign

data class UIPortfolioInfo(
    val id: String,
    val name: String,
    val access: String,
)

data class UIPortfolioTotalBalance(
    val formatedBalanceQuote: String? = null,
    val formattedBalanceQuoteChange: String? = null,
    val formatedChangePercent: String? = null,
    val changePercentSign: NumberSign? = null,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}