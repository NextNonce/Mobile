package com.nextnonce.app.portfolio.data.mapper

import com.nextnonce.app.core.data.mapper.toAssetBalance
import com.nextnonce.app.core.data.mapper.toTotalBalance
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioBalancesDto
import com.nextnonce.app.portfolio.domain.model.PortfolioBalancesModel

fun PortfolioBalancesDto.toPortfolioBalancesModel() = PortfolioBalancesModel(
    actual = actual,
    totalBalance = totalBalance.toTotalBalance(),
    assetBalances = assetBalances.map { it.toAssetBalance() }
)