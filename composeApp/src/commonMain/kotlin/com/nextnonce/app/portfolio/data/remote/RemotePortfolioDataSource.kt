package com.nextnonce.app.portfolio.data.remote

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.portfolio.data.remote.dto.CreatePortfolioWalletDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioBalancesDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioWalletDto

/**
 * Remote data source interface for portfolio operations.
 * This interface defines methods to interact with remote portfolio data,
 * including fetching portfolios, portfolio details, balances, and wallets.
 */
interface RemotePortfolioDataSource  {
    suspend fun getPortfolios(): Result<List<PortfolioDto>, DataError.Remote>
    suspend fun getPortfolioById(id: String): Result<PortfolioDto, DataError.Remote>
    suspend fun getCachedBalances(id: String): Result<PortfolioBalancesDto, DataError.Remote>
    suspend fun getPortfolioWallets(id: String): Result<List<PortfolioWalletDto>, DataError.Remote>
    suspend fun createPortfolioWallet(id: String, createPortfolioWalletDto: CreatePortfolioWalletDto): Result<PortfolioWalletDto, DataError.Remote>
}