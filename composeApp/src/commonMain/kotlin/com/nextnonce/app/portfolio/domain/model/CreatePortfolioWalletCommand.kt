package com.nextnonce.app.portfolio.domain.model

data class CreatePortfolioWalletCommand(
    val address: String,
    val name: String?,
)
