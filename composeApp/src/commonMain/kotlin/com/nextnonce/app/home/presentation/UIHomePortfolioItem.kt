package com.nextnonce.app.home.presentation

import com.nextnonce.app.core.enums.NumberSign

/**
 * Represents a portfolio item in the home screen.
 *
 * @property id The unique identifier for the portfolio item.
 * @property name The name of the portfolio item, default is "Portfolio".
 * @property formatedBalanceQuote The formatted balance in quote currency, can be null.
 * @property formatedChangePercent The formatted change percentage, can be null.
 * @property changePercentSign The sign of the change percentage, can be null.
 * @property isLoading Indicates whether the portfolio item is currently loading.
 */
data class UIHomePortfolioItem(
    val id: String? = null,
    val name: String = "Portfolio",
    val formatedBalanceQuote: String? = null,
    val formatedChangePercent: String? = null,
    val changePercentSign: NumberSign? = null,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}
