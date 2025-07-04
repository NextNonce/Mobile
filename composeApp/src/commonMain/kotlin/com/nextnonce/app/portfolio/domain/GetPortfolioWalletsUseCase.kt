package com.nextnonce.app.portfolio.domain

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.portfolio.domain.model.PortfolioWalletModel
import kotlinx.coroutines.flow.Flow

class GetPortfolioWalletsUseCase(
    private val portfolioRepository: PortfolioRepository,
) {
    fun execute(id: String): Flow<Result<List<PortfolioWalletModel>, DataError>> {
        return portfolioRepository.portfolioWalletsFlow(id)
    }
}