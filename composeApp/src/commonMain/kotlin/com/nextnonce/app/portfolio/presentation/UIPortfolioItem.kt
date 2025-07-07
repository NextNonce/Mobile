package com.nextnonce.app.portfolio.presentation

import com.nextnonce.app.core.enums.NumberSign

/**
 * Represents the UI model for a portfolio item.
 *
 * @property id The unique identifier of the portfolio.
 * @property name The name of the portfolio.
 * @property access The access level of the portfolio (e.g., Public, Private).
 */
data class UIPortfolioInfo(
    val id: String,
    val name: String,
    val access: String,
)

/**
 * Represents the UI model for the total balance of a portfolio.
 *
 * @property formatedBalanceQuote The formatted total balance in quote currency.
 * @property formattedBalanceQuoteChange The formatted change in total balance in quote currency.
 * @property formatedChangePercent The formatted change percentage of the total balance.
 * @property changePercentSign The sign of the change percentage.
 */
data class UIPortfolioTotalBalance(
    val formatedBalanceQuote: String? = null,
    val formattedBalanceQuoteChange: String? = null,
    val formatedChangePercent: String? = null,
    val changePercentSign: NumberSign? = null,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}