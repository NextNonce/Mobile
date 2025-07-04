package com.nextnonce.app.portfolio.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class CreatePortfolioWalletDto(
    val address: String,
    val name: String? = null,
)
