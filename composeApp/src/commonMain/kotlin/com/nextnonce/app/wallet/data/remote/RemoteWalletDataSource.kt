package com.nextnonce.app.wallet.data.remote

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto
import com.nextnonce.app.wallet.data.remote.dto.WalletDto

/**
 * Remote data source interface for wallet operations.
 * This interface defines methods to interact with remote wallet data,
 * including fetching wallet details and balances.
 */
interface RemoteWalletDataSource {
    /**
     * Fetches the wallet details for a given address.
     * @param address The wallet address to fetch details for.
     * @return Result containing WalletDto on success or DataError on error.
     */
    suspend fun get(address: String): Result<WalletDto, DataError.Remote>
    /**
     * Fetches the wallet balances for a given address.
     * @param address The wallet address to fetch balances for.
     * @return Result containing WalletBalancesDto on success or DataError on error.
     */
    suspend fun getBalances(address: String): Result<WalletBalancesDto, DataError.Remote>
}