package com.nextnonce.app.portfolio.data.remote.impl

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.network.backendHttpClient
import com.nextnonce.app.core.network.safeCall
import com.nextnonce.app.portfolio.data.remote.RemotePortfolioDataSource
import com.nextnonce.app.portfolio.data.remote.dto.CreatePortfolioWalletDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioBalancesDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioWalletDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemotePortfolioDataSourceImpl : RemotePortfolioDataSource, KoinComponent {

    private val backend: HttpClient by inject(backendHttpClient)

    override suspend fun getPortfolios(): Result<List<PortfolioDto>, DataError.Remote> {
        return safeCall {
            backend.get("portfolios")
        }
    }

    override suspend fun getPortfolioById(id: String): Result<PortfolioDto, DataError.Remote> {
        return safeCall {
            backend.get("portfolios/$id")
        }
    }

    override suspend fun getCachedBalances(id: String): Result<PortfolioBalancesDto, DataError.Remote> {
        return safeCall {
            backend.get("portfolios/$id/balances/cached")
        }
    }

    override suspend fun getPortfolioWallets(id: String): Result<List<PortfolioWalletDto>, DataError.Remote> {
        return safeCall {
            backend.get("portfolios/$id/wallets")
        }
    }

    override suspend fun createPortfolioWallet(
        id: String,
        createPortfolioWalletDto: CreatePortfolioWalletDto
    ): Result<PortfolioWalletDto, DataError.Remote> {
        return safeCall {
            backend.post("portfolios/$id/wallets") {
                setBody(createPortfolioWalletDto)
            }
        }
    }
}