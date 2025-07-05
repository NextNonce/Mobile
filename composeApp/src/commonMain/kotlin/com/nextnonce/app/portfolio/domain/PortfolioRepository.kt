package com.nextnonce.app.portfolio.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.portfolio.domain.model.CreatePortfolioWalletCommand
import com.nextnonce.app.portfolio.domain.model.PortfolioBalancesModel
import com.nextnonce.app.portfolio.domain.model.PortfolioModel
import com.nextnonce.app.portfolio.domain.model.PortfolioWalletModel
import kotlinx.coroutines.flow.Flow


interface PortfolioRepository {
    fun portfoliosFlow(): Flow<Result<List<PortfolioModel>, DataError>>
    suspend fun createPortfolioWallet(
        id: String,
        command: CreatePortfolioWalletCommand
    ): EmptyResult<DataError>

    fun portfolioWalletsFlow(
        id: String
    ): Flow<Result<List<PortfolioWalletModel>, DataError>>

    suspend fun cachedBalancesFlow(id: String): Flow<Result<PortfolioBalancesModel, DataError>>
}