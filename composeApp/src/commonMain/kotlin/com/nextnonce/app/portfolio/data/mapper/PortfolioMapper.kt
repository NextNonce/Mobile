package com.nextnonce.app.portfolio.data.mapper

import com.nextnonce.app.core.domain.portfolio.Portfolio
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioDto
import com.nextnonce.app.portfolio.domain.model.PortfolioModel

fun PortfolioDto.toPortfolioModel() = PortfolioModel(
    portfolio = Portfolio(
        id = id,
        name = name,
        access = portfolioAccess
    ),
    createdAt = createdAt,
    updatedAt = updatedAt
)