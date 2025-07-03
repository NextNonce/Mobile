package com.nextnonce.app.wallet.data.remote

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto

interface RemoteWalletDataSource {
    suspend fun getBalances(address: String): Result<WalletBalancesDto, DataError.Remote>
}