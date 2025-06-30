package com.nextnonce.app.core.domain.portfolio

data class Portfolio (
    val id: String,
    val name: String,
    val access: PortfolioAccess
)