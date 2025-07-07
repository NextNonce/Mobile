package com.nextnonce.app.wallet.domain

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.wallet.domain.model.WalletModel

class GetWalletUseCase(
    private val walletRepository: WalletRepository
) {

    suspend fun execute(
        walletId: String
    ): Result<WalletModel, DataError> {
        return walletRepository.getWalletById(walletId)
    }
}