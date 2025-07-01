package com.nextnonce.app.portfolio.data

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.EmptyResult
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.map
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.portfolio.data.mapper.toCreatePortfolioWalletDto
import com.nextnonce.app.portfolio.data.mapper.toPortfolioBalancesModel
import com.nextnonce.app.portfolio.data.mapper.toPortfolioModel
import com.nextnonce.app.portfolio.data.mapper.toPortfolioWalletModel
import com.nextnonce.app.portfolio.data.remote.RemotePortfolioDataSource
import com.nextnonce.app.portfolio.domain.PortfolioRepository
import com.nextnonce.app.portfolio.domain.model.CreatePortfolioWalletCommand
import com.nextnonce.app.portfolio.domain.model.PortfolioBalancesModel
import com.nextnonce.app.portfolio.domain.model.PortfolioModel
import com.nextnonce.app.portfolio.domain.model.PortfolioWalletModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class PortfolioRepositoryImpl(
    private val remoteDataSource: RemotePortfolioDataSource,
) : PortfolioRepository {

    private val refreshWallets = mutableMapOf<String, MutableSharedFlow<Unit>>()

    private fun triggerFor(id: String) =
        refreshWallets.getOrPut(id) {
            // replay=1 so onStart()
            MutableSharedFlow(replay = 1)
        }

    override fun portfoliosFlow(): Flow<Result<List<PortfolioModel>, DataError>> {
        return flow {
            when (val res = remoteDataSource.getPortfolios()) {
                is Result.Success -> {
                    val portfolioModels = res.data.map { it.toPortfolioModel() }
                    if (portfolioModels.isEmpty()) {
                        AppLogger.e { "No portfolios found." }
                        emit(Result.Error(DataError.Remote.NOT_FOUND))
                    } else {
                        emit(Result.Success(portfolioModels))
                    }
                }
                is Result.Error -> {
                    AppLogger.e {
                        "Error fetching portfolios: ${res.error.name}"
                    }
                    emit(res)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun portfolioWalletsFlow(id: String): Flow<Result<List<PortfolioWalletModel>, DataError>> {
        return triggerFor(id)
            .onStart { emit(Unit) }   // emit once when someone starts collecting
            .flatMapLatest {
                flow {
                    when (val res = remoteDataSource.getPortfolioWallets(id)) {
                        is Result.Success ->
                            emit(Result.Success(res.data.map { it.toPortfolioWalletModel() }))
                        is Result.Error -> {
                            AppLogger.e {
                                "Error fetching portfolio wallets for id $id: ${res.error.name}"
                            }
                            emit(res)
                        }
                    }
                }
            }
    }

    override suspend fun createPortfolioWallet(
        id: String,
        command: CreatePortfolioWalletCommand
    ): EmptyResult<DataError> {
        val dto = command.toCreatePortfolioWalletDto()
        return when (val res = remoteDataSource.createPortfolioWallet(id, dto)) {
            is Result.Success -> {
                triggerFor(id).emit(Unit)
                return Result.Success(Unit)
            }
            is Result.Error -> {
                AppLogger.e {
                    "Error creating portfolio wallet: ${res.error.name}"
                }
                res
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun cachedBalancesFlow(id: String): Flow<Result<PortfolioBalancesModel, DataError>> {
        return triggerFor(id)
            .onStart { emit(Unit) }
            .flatMapLatest { flow {
                val dtoResult = remoteDataSource.getCachedBalances(id)

                val modelResult: Result<PortfolioBalancesModel, DataError> =
                    dtoResult.map { dto ->
                        dto.toPortfolioBalancesModel()
                    }

                if (modelResult is Result.Error) {
                    AppLogger.e {
                        "Error fetching cached balances for id $id: ${modelResult.error}"
                    }
                } else {
                    AppLogger.d {
                        "Successfully fetched cached balances for id $id"
                    }
                }

                emit(modelResult)
            }
        }
    }
}