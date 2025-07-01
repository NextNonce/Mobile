package com.nextnonce.app.portfolio.data.remote.dto

import com.nextnonce.app.core.data.remote.dto.balance.AssetBalanceDto
import com.nextnonce.app.core.data.remote.dto.balance.TotalBalanceDto
import kotlinx.serialization.Serializable

@Serializable
data class PortfolioBalancesDto(
    val actual: Boolean,
    val totalBalance: TotalBalanceDto,
    val assetBalances: List<AssetBalanceDto> = emptyList()
)
