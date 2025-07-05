package com.nextnonce.app.wallet.data

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.wallet.toWallet
import com.nextnonce.app.core.utils.BASE_REFRESH_DELAY
import com.nextnonce.app.core.utils.CACHE_AGE_ONE_MINUTE
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.wallet.data.local.WalletBalancesCacheDao
import com.nextnonce.app.wallet.data.local.WalletBalancesCacheEntity
import com.nextnonce.app.wallet.data.mapper.toWalletBalancesModel
import com.nextnonce.app.wallet.data.mapper.toWalletModel
import com.nextnonce.app.wallet.data.remote.RemoteWalletDataSource
import com.nextnonce.app.wallet.domain.WalletRepository
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel
import com.nextnonce.app.wallet.domain.model.WalletModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlin.random.Random

class WalletRepositoryImpl(
    private val remoteDataSource: RemoteWalletDataSource,
    private val walletBalancesCacheDao: WalletBalancesCacheDao
) : WalletRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val balancesFlowMutex = Mutex()
    private val balancesFlows =
        mutableMapOf<String, SharedFlow<Result<WalletBalancesModel, DataError>>>()

    override suspend fun getWalletById(
        walletId: String
    ): Result<WalletModel, DataError> {
        AppLogger.d {
            "Fetching wallet by ID: $walletId"
        }
        val wallet = walletId.toWallet()
            ?: return Result.Error(DataError.Local.UNKNOWN)

        return when (val result = remoteDataSource.get(wallet.address)) {
            is Result.Success -> {
                AppLogger.d {
                    "Successfully fetched wallet: ${result.data.address}"
                }
                Result.Success(result.data.toWalletModel())
            }
            is Result.Error -> {
                AppLogger.e {
                    "Error fetching wallet: ${result.error.name}"
                }
                Result.Error(result.error)
            }
        }
    }

    override suspend fun balancesFlow(
        walletId: String
    ): Flow<Result<WalletBalancesModel, DataError>>{

        balancesFlows[walletId]?.let { return it }

        return balancesFlowMutex.withLock {
            balancesFlows.getOrPut(walletId) {
                createAndShareBalancesFlow(walletId)
            }
        }
    }

    private fun createAndShareBalancesFlow(
        walletId: String
    ): SharedFlow<Result<WalletBalancesModel, DataError>> = flow {
        val cached = walletBalancesCacheDao.getBalancesCache(walletId)
        if (cached != null) {
            AppLogger.d {
                "Using cached balances for wallet $walletId"
            }
            val cachedDto = cached.toDto()
            emit(
                Result.Success(cachedDto.toWalletBalancesModel())
            )
            if (now() - cached.timestamp <= CACHE_AGE_ONE_MINUTE) {
                AppLogger.d {
                    "Using cached balances for wallet $walletId, which is still fresh."
                }
            }
        }

        val wallet = walletId.toWallet()
            ?: return@flow emit(Result.Error(DataError.Local.UNKNOWN))

        while (true) {
            // in order to avoid hammering the server
            when (val dtoResult = remoteDataSource.getBalances(wallet.address)) {
                is Result.Error -> emit(Result.Error(dtoResult.error))
                is Result.Success -> {
                    val dto = dtoResult.data
                    AppLogger.d {
                        "Fetched fresh balances for wallet $walletId: ${dto.totalBalance}"
                    }

                    // Emit fresh
                    emit(Result.Success(dto.toWalletBalancesModel()))

                    AppLogger.d {
                        "Caching fresh balances for wallet $walletId."
                    }
                    // Cache the fresh DTO
                    walletBalancesCacheDao.upsertBalancesCache(
                        WalletBalancesCacheEntity(walletId, now(), dto)
                    )
                }
            }
            // in order to avoid hammering the server
            randomDelay()
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000L),
        replay = 1
    )

    private fun now(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    private suspend fun randomDelay() {
        delay(
            Random.nextLong
                (BASE_REFRESH_DELAY, 2 * BASE_REFRESH_DELAY)
        )
    }
}