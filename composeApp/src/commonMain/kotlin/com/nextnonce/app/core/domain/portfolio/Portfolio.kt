package com.nextnonce.app.core.domain.portfolio

/**
 * Represents a portfolio in the application.
 * @property id The unique identifier of the portfolio.
 * @property name The name of the portfolio.
 * @property access The access level of the portfolio, which can be private, public, or unlisted.
 */
data class Portfolio (
    val id: String,
    val name: String,
    val access: PortfolioAccess
)