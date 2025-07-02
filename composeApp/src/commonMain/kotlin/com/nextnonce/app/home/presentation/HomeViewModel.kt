package com.nextnonce.app.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.core.domain.onError
import com.nextnonce.app.core.domain.onSuccess
import com.nextnonce.app.core.utils.formatPercentage
import com.nextnonce.app.core.utils.formatQuote
import com.nextnonce.app.core.utils.toSign
import com.nextnonce.app.core.utils.toUIText
import com.nextnonce.app.portfolio.domain.GetDefaultPortfolioUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioTotalBalanceUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioWalletsUseCase
import com.nextnonce.app.portfolio.domain.model.PortfolioWalletModel
import com.nextnonce.app.wallet.domain.GetWalletTotalBalanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getDefaultPortfolioUseCase: GetDefaultPortfolioUseCase,
    private val getPortfolioTotalBalanceUseCase: GetPortfolioTotalBalanceUseCase,
    private val getPortfolioWalletsUseCase: GetPortfolioWalletsUseCase,
    private val getWalletTotalBalanceUseCase: GetWalletTotalBalanceUseCase
) : ViewModel()  {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        // start the chain
        viewModelScope.launch {
            observeDefaultPortfolio()
        }
    }

    private suspend fun observeDefaultPortfolio() {
        getDefaultPortfolioUseCase.execute()
            // show loading until we have at least an error or value
            .onStart { _state.update { it.copy(areWalletsLoading = true) } }
            .collect { result ->
                result
                    .onSuccess { portfolioModel ->
                        // 1 update the portfolio UI model
                        val portfolioUI = UIHomePortfolioItem(
                            id = portfolioModel.portfolio.id,
                            name = portfolioModel.portfolio.name,
                            formatedBalanceQuote = null,
                            formatedChangePercent = null,
                        )
                        _state.update {
                            it.copy(
                                portfolio = portfolioUI,
                                error = null
                            )
                        }
                        // 2 kick off the two dependent streams
                        if (portfolioUI.id != null) {
                            observePortfolioTotalBalance(portfolioUI.id)
                            observePortfolioWallets(portfolioUI.id)
                        }
                    }
                    .onError { error ->
                        // show that error and stop
                        _state.update { it.copy(error = error.toUIText(), areWalletsLoading = false) }
                    }
            }
    }

    private fun observePortfolioTotalBalance(portfolioId: String) = viewModelScope.launch {
        getPortfolioTotalBalanceUseCase.execute(portfolioId)
            .collect { result ->
                result
                    .onSuccess { totalBalance ->
                        _state.update { old ->
                            val updatedPortfolio = old.portfolio.copy(
                                formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                                formatedChangePercent =
                                    formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO),
                                changePercentSign = totalBalance.balanceQuoteChangePercent?.toSign()
                            )
                            old.copy(
                                portfolio = updatedPortfolio,
                                error = old.error
                            )
                        }
                    }
                    .onError { error ->
                        _state.update { it.copy(error = error.toUIText()) }
                    }
            }
    }

    private fun observePortfolioWallets(portfolioId: String) = viewModelScope.launch {
        getPortfolioWalletsUseCase.execute(portfolioId)
            .collect { result ->
                result
                    .onSuccess { walletModels ->
                        val initialWalletsUI = walletModels.map { pwm ->
                            UIHomeWalletItem(
                                id                   = pwm.wallet.id,
                                address              = pwm.wallet.address,
                                name                 = pwm.name,
                                formatedBalanceQuote = null,     // loading placeholder
                                formatedChangePercent= null,
                                changePercentSign    = null
                            )
                        }
                        // show that ordered list immediately, clear loading flag
                        _state.update { it.copy(
                            wallets = initialWalletsUI,
                            areWalletsLoading = initialWalletsUI.isEmpty()
                        ) }

                        // 2) For each one, start loading its balance
                        walletModels.forEachIndexed { index, pwm ->
                            observeSingleWalletBalance(index, pwm)
                        }
                    }
                    .onError { error ->
                        _state.update { it.copy(error = error.toUIText(), areWalletsLoading = false) }
                    }
            }
    }

    private fun observeSingleWalletBalance( index: Int, pwm: PortfolioWalletModel) = viewModelScope.launch {
        getWalletTotalBalanceUseCase.execute(pwm.wallet)
            .collect { result ->
                result
                    .onSuccess { totalBalance ->
                        _state.update { old ->
                            // 3) Copy the existing list, then replace only at [index]
                            val updated = old.wallets.toMutableList().apply {
                                this[index] = UIHomeWalletItem(
                                    id                   = pwm.wallet.id,
                                    address              = pwm.wallet.address,
                                    name                 = pwm.name,
                                    formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                                    formatedChangePercent= formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO),
                                    changePercentSign    = totalBalance.balanceQuoteChangePercent?.toSign()
                                )
                            }
                            old.copy(
                                wallets = updated,
                                error = old.error
                            )
                        }
                    }
                    .onError { error ->
                        _state.update { it.copy(error = error.toUIText()) }
                    }
            }
    }
}