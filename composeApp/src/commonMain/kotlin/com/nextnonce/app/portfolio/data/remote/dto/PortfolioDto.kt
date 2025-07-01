package com.nextnonce.app.portfolio.data.remote.dto

import com.nextnonce.app.core.domain.portfolio.PortfolioAccess
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PortfolioDto(
    val id: String,
    val name: String,
    val portfolioAccess: PortfolioAccess,
    val createdAt: Instant,
    val updatedAt: Instant,
)
