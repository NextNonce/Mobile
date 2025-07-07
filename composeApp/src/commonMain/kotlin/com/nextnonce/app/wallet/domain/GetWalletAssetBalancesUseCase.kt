package com.nextnonce.app.wallet.domain

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWalletAssetBalancesUseCase(
    private val walletRepository: WalletRepository
) {

    /**
     * Retrieves the asset balances for a specific wallet using balancesFlow from the wallet repository.
     *
     * @param walletId The ID of the wallet for which to retrieve asset balances.
     * @return A flow that emits a Result containing a list of AssetBalance or a DataError.
     */
    suspend fun execute(
        walletId: String
    ): Flow<Result<List<AssetBalance>, DataError>> {
        return walletRepository
            .balancesFlow(walletId)
            .map { result: Result<WalletBalancesModel, DataError> ->
                when (result) {
                    is Result.Success -> Result.Success(result.data.assetBalances)
                    is Result.Error -> result
                }
            }
    }
}