package com.nextnonce.app.wallet.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.balance.TotalBalance
import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWalletTotalBalanceUseCase(
    private val walletRepository: WalletRepository
) {
    suspend fun execute(
        walletId: String
    ): Flow<Result<TotalBalance, DataError>> {
        return walletRepository
            .balancesFlow(walletId)
            .map { result: Result<WalletBalancesModel, DataError> ->
                when (result) {
                    is Result.Success -> Result.Success(result.data.totalBalance)
                    is Result.Error -> Result.Error(result.error)
                }
            }
    }
}
