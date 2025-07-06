package com.nextnonce.app.di

import com.nextnonce.app.portfolio.data.PortfolioRepositoryImpl
import com.nextnonce.app.portfolio.data.remote.RemotePortfolioDataSource
import com.nextnonce.app.portfolio.data.remote.impl.RemotePortfolioDataSourceImpl
import com.nextnonce.app.portfolio.domain.CreatePortfolioWalletUseCase
import com.nextnonce.app.portfolio.domain.GetDefaultPortfolioUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioAssetBalancesUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioTotalBalanceUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioWalletsUseCase
import com.nextnonce.app.portfolio.domain.PortfolioRepository
import com.nextnonce.app.portfolio.presentation.AddPortfolioWalletViewModel
import com.nextnonce.app.portfolio.presentation.PortfolioViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * Koin module for portfolio-related dependencies.
 * This module provides the repository, use cases, and view models for managing portfolio and their wallets.
 */
val portfolioModule = module {
    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    singleOf(::CreatePortfolioWalletUseCase)
    singleOf(::GetDefaultPortfolioUseCase)
    singleOf(::GetPortfolioAssetBalancesUseCase)
    singleOf(::GetPortfolioTotalBalanceUseCase)
    singleOf(::GetPortfolioWalletsUseCase)
    singleOf(::RemotePortfolioDataSourceImpl).bind<RemotePortfolioDataSource>()

    viewModel { AddPortfolioWalletViewModel(get()) }
    viewModel { (portfolioId: String) ->
        PortfolioViewModel(
            id = portfolioId,
            get(),
            get(),
            get(),
        )
    }
}