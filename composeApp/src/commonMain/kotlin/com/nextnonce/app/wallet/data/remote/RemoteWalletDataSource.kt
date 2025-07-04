package com.nextnonce.app.wallet.data.remote

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto
import com.nextnonce.app.wallet.data.remote.dto.WalletDto

interface RemoteWalletDataSource {
    suspend fun get(address: String): Result<WalletDto, DataError.Remote>
    suspend fun getBalances(address: String): Result<WalletBalancesDto, DataError.Remote>
}