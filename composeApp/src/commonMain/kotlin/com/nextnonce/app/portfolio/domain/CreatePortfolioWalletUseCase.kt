package com.nextnonce.app.portfolio.domain

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.portfolio.domain.model.CreatePortfolioWalletCommand

class CreatePortfolioWalletUseCase(
    private val portfolioRepository: PortfolioRepository,
) {
    suspend fun execute(
        id: String,
        command: CreatePortfolioWalletCommand
    ): EmptyResult<DataError> {
        return portfolioRepository.createPortfolioWallet(id, command)
    }
}