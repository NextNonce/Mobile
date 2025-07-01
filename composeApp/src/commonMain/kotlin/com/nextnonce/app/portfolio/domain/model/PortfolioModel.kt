package com.nextnonce.app.portfolio.domain.model

import com.nextnonce.app.core.domain.portfolio.Portfolio
import kotlinx.datetime.Instant

data class PortfolioModel(
    val portfolio: Portfolio,
    val createdAt: Instant,
    val updatedAt: Instant,
)