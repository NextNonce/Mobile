package com.nextnonce.app.portfolio.domain.model

import com.nextnonce.app.core.domain.portfolio.Portfolio
import kotlinx.datetime.Instant

/**
 * Represents a model for a portfolio containing its details and timestamps.
 *
 * @property portfolio The portfolio details.
 * @property createdAt The timestamp when the portfolio was created.
 * @property updatedAt The timestamp when the portfolio was last updated.
 */
data class PortfolioModel(
    val portfolio: Portfolio,
    val createdAt: Instant,
    val updatedAt: Instant,
)