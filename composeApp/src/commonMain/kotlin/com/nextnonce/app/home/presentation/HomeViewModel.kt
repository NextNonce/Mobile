package com.nextnonce.app.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.balance.TotalBalance
import com.nextnonce.app.core.domain.onError
import com.nextnonce.app.core.domain.onSuccess
import com.nextnonce.app.core.enums.NumberSign
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
import kotlinx.coroutines.flow.combine
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
                        portfolioUI.id?.let { id ->
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
                            val formatedChangePercent =
                                formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO)
                            val updatedPortfolio = old.portfolio.copy(
                                formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                                formatedChangePercent = formatedChangePercent,
                                changePercentSign = formatedChangePercent.toSign()
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

    /**
     * HYBRID LOGIC:
     * 1. Fetches the list of wallets.
     * 2. IMMEDIATELY displays that list with "loading" placeholders for balances.
     * 3. Launches a SINGLE background job that uses `combine` to efficiently fetch all balances.
     * 4.Continuously collects updates from the combined flow to show cached, then fresh data.
     * This provides immediate feedback while ensuring efficient state updates.
     */
    private fun observePortfolioWallets(portfolioId: String) = viewModelScope.launch {
        getPortfolioWalletsUseCase.execute(portfolioId)
            .onStart { _state.update { it.copy(areWalletsLoading = true) } }
            .collect { walletListResult ->
                walletListResult.onSuccess { walletModels ->
                    // 1. Create and display the initial list with loading placeholders immediately.
                    val initialWalletsUI = walletModels.map { pwm ->
                        createUiHomeWalletItem(pwm)
                    }
                    _state.update {
                        it.copy(
                            wallets = initialWalletsUI,
                            areWalletsLoading = false // The list of names is now loaded.
                        )
                    }

                    // If there are no wallets to fetch balances for, we're done.
                    if (walletModels.isEmpty()) return@onSuccess

                    // 2. Launch a single, non-blocking background job to fetch all balances.
                    viewModelScope.launch {
                        val balanceFlows = walletModels.map { pwm ->
                            getWalletTotalBalanceUseCase.execute(pwm.wallet.id)
                        }

                        // `combine` will emit a full list of results once every balance flow has emitted at least once.
                        val combinedBalancesFlow = combine(balanceFlows) { latestBalances ->
                            walletModels.mapIndexed { index, pwm ->
                                createUiHomeWalletItem(pwm, latestBalances[index])
                            }
                        }

                        combinedBalancesFlow.collect { updatedWalletsList ->
                            // Update the state with the latest list, whether from cache or fresh from network.
                            _state.update { it.copy(wallets = updatedWalletsList) }
                        }
                    }
                }
                .onError { error ->
                    _state.update { it.copy(error = error.toUIText(), areWalletsLoading = false) }
                }
            }
    }


    /**
     * Helper function to map a wallet model and its balance result to a UI item.
     * Handles a nullable balanceResult to create loading states.
     */
    private fun createUiHomeWalletItem(
        pwm: PortfolioWalletModel,
        balanceResult: Result<TotalBalance, DataError>? = null // Nullable to represent loading state
    ): UIHomeWalletItem {
        var formatedBalance: String? = null
        var formatedPercent: String? = null
        var sign: NumberSign? = null

        // If we have a successful result, populate the values.
        balanceResult?.onSuccess { totalBalance ->
            formatedBalance = formatQuote(totalBalance.balanceQuote)
            formatedPercent = formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO)
            sign = formatedPercent.toSign() // Assuming toSign() is an extension on String
        }

        return UIHomeWalletItem(
            id = pwm.wallet.id,
            address = pwm.wallet.address,
            name = pwm.name,
            formatedBalanceQuote = formatedBalance, // Will be null during loading
            formatedChangePercent = formatedPercent,
            changePercentSign = sign
        )
    }
}