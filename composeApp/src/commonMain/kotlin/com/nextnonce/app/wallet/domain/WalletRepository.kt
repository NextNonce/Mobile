package com.nextnonce.app.wallet.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun balancesFlow(
        walletId: String
    ): Flow<Result<WalletBalancesModel, DataError>>
}