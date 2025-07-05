package com.nextnonce.app.wallet.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel
import com.nextnonce.app.wallet.domain.model.WalletModel
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    suspend fun getWalletById(
        walletId: String
    ): Result<WalletModel, DataError>

    suspend fun balancesFlow(
        walletId: String
    ): Flow<Result<WalletBalancesModel, DataError>>
}