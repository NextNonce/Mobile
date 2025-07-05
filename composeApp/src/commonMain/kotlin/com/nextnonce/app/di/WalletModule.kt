package com.nextnonce.app.di

import androidx.room.RoomDatabase
import com.nextnonce.app.core.database.wallet.WalletDatabase
import com.nextnonce.app.core.database.wallet.getWalletDatabase
import com.nextnonce.app.wallet.data.WalletRepositoryImpl
import com.nextnonce.app.wallet.data.remote.RemoteWalletDataSource
import com.nextnonce.app.wallet.data.remote.impl.RemoteWalletDataSourceImpl
import com.nextnonce.app.wallet.domain.GetWalletAssetBalancesUseCase
import com.nextnonce.app.wallet.domain.GetWalletTotalBalanceUseCase
import com.nextnonce.app.wallet.domain.GetWalletUseCase
import com.nextnonce.app.wallet.domain.WalletRepository
import com.nextnonce.app.wallet.presentation.WalletViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val walletModule = module {
    singleOf(::WalletRepositoryImpl).bind<WalletRepository>()
    single {
        getWalletDatabase(get<RoomDatabase.Builder<WalletDatabase>>())
    }
    single { get<WalletDatabase>().walletBalancesCacheDao() }
    singleOf(::GetWalletUseCase)
    singleOf(::GetWalletAssetBalancesUseCase)
    singleOf(::GetWalletTotalBalanceUseCase)
    singleOf(::RemoteWalletDataSourceImpl).bind<RemoteWalletDataSource>()
    viewModel { (walletId: String, walletName: String?) ->
        WalletViewModel(
            id = walletId,
            name = walletName,
            get(),
            get(),
            get()
        )
    }
}