package com.nextnonce.app.wallet.data.remote.impl

import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.network.backendHttpClient
import com.nextnonce.app.core.network.safeCall
import com.nextnonce.app.wallet.data.remote.RemoteWalletDataSource
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class RemoteWalletDataSourceImpl : RemoteWalletDataSource, KoinComponent  {

    private val backend: HttpClient by inject(backendHttpClient)

    override suspend fun getBalances(address: String): Result<WalletBalancesDto, DataError.Remote> {
        return safeCall {
            backend.get("wallets/$address/balances")
        }
    }
}