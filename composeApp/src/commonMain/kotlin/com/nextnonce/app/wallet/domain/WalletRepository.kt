package com.nextnonce.app.wallet.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel
import com.nextnonce.app.wallet.domain.model.WalletModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing a repository for managing wallet data.
 * Provides methods to retrieve wallet information and balances.
 */
interface WalletRepository {

    /**
     * Returns a WalletModel for the specified wallet ID.
     *
     * @param walletId The ID of the wallet to fetch.
     * @return A Result containing the WalletModel on success or DataError on error.
     */
    suspend fun getWalletById(
        walletId: String
    ): Result<WalletModel, DataError>

    /**
     * Returns a flow of wallet balances for the specified wallet ID.
     * The flow emits results containing the wallet balances model or an error.
     *
     * @param walletId The ID of the wallet to fetch balances for.
     * @return A flow emitting Result containing WalletBalancesModel or DataError.
     */
    suspend fun balancesFlow(
        walletId: String
    ): Flow<Result<WalletBalancesModel, DataError>>
}