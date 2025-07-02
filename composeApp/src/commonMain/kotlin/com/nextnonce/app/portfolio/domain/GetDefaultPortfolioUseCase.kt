package com.nextnonce.app.portfolio.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.portfolio.domain.model.PortfolioModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDefaultPortfolioUseCase(
    private val portfolioRepository: PortfolioRepository,
) {
    fun execute(): Flow<Result<PortfolioModel, DataError>> {
        return portfolioRepository
            .portfoliosFlow()
            .map { result: Result<List<PortfolioModel>, DataError> ->
                when (result) {
                    is Result.Success -> {
                        Result.Success(result.data.first())
                    }
                    is Result.Error -> result
                }
            }
    }
}
