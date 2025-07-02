package com.nextnonce.app.portfolio.domain

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.balance.TotalBalance
import com.nextnonce.app.portfolio.domain.model.PortfolioBalancesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPortfolioTotalBalanceUseCase(
    private val portfolioRepository: PortfolioRepository,
){
    fun execute(id: String): Flow<Result<TotalBalance, DataError>> {
        return portfolioRepository
            .cachedBalancesFlow(id)
            .map { result: Result<PortfolioBalancesModel, DataError> ->
                when (result) {
                    is Result.Success -> Result.Success(result.data.totalBalance)
                    is Result.Error -> Result.Error(result.error)
                }
            }
    }
}