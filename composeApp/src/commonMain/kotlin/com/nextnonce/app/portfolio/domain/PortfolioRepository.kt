package com.nextnonce.app.portfolio.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.portfolio.domain.model.CreatePortfolioWalletCommand
import com.nextnonce.app.portfolio.domain.model.PortfolioBalancesModel
import com.nextnonce.app.portfolio.domain.model.PortfolioModel
import com.nextnonce.app.portfolio.domain.model.PortfolioWalletModel
import kotlinx.coroutines.flow.Flow


/**
 * Interface for the Portfolio Repository that defines methods to interact with portfolio data.
 * It provides methods to fetch portfolios, create portfolio wallets, and observe portfolio balances.
 */
interface PortfolioRepository {
    /**
     * Fetches a list of portfolios.
     *
     * @return A flow that emits the result containing a list of PortfolioModel or an error.
     */
    fun portfoliosFlow(): Flow<Result<List<PortfolioModel>, DataError>>

    /**
     * Creates a new portfolio wallet.
     * @param id The ID of the portfolio to which the wallet will be added.
     * @param command The command containing the details for creating the portfolio wallet.
     * @return An empty result indicating success or error of the operation.
     */
    suspend fun createPortfolioWallet(
        id: String,
        command: CreatePortfolioWalletCommand
    ): EmptyResult<DataError>

    /**
     * Observes the wallets associated with a specific portfolio.
     *
     * @param id The ID of the portfolio.
     * @return A flow that emits the result containing a list of PortfolioWalletModel or an error.
     */
    fun portfolioWalletsFlow(
        id: String
    ): Flow<Result<List<PortfolioWalletModel>, DataError>>

    /**
     * Observes the cached asset balances for a specific portfolio.
     *
     * @param id The ID of the portfolio.
     * @return A flow that emits the result containing PortfolioBalancesModel or an error.
     */
    suspend fun cachedBalancesFlow(id: String): Flow<Result<PortfolioBalancesModel, DataError>>
}