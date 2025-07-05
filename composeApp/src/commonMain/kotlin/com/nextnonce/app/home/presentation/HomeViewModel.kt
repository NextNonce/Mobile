package com.nextnonce.app.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.balance.TotalBalance
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePortfolioWallets(portfolioId: String) = viewModelScope.launch {
        // flatMapLatest ensures that if the list of wallets changes, we cancel the old
        // observers and create a new set of combined balance flows.
        getPortfolioWalletsUseCase.execute(portfolioId)
            .flatMapLatest { walletListResult ->
                // Handle the result for the list of wallets itself.
                when (walletListResult) {
                    is Result.Success -> {
                        val walletModels = walletListResult.data
                        if (walletModels.isEmpty()) {
                            // If the list is empty, just emit an empty list for the UI.
                            flowOf(emptyList<UIHomeWalletItem>())
                        } else {
                            // 1. Create a list of balance flows, one for each wallet.
                            val balanceFlows: List<Flow<Result<TotalBalance, DataError>>> =
                                walletModels.map { pwm ->
                                    getWalletTotalBalanceUseCase.execute(pwm.wallet.id)
                                }

                            // 2. Combine the latest emissions from all balance flows.
                            combine(balanceFlows) { latestBalances ->
                                // `latestBalances` is an Array containing the most recent Result from each flow.
                                // We map the original wallet models to UI models, combining them with their latest balance.
                                walletModels.mapIndexed { index, pwm ->
                                    createUiHomeWalletItem(pwm, latestBalances[index])
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        _state.update { it.copy(error = walletListResult.error.toUIText(), areWalletsLoading = false) }
                        emptyFlow()
                    }
                }
            }
            .onStart { _state.update { it.copy(areWalletsLoading = true) } }
            .collect { finalWalletsList ->
                // 3. This is now the SINGLE point of update for the entire wallets list.
                // It fires only when the combined list is ready.
                _state.update {
                    it.copy(
                        wallets = finalWalletsList,
                        areWalletsLoading = false // Loading is complete.
                    )
                }
            }
    }

    /**
     * Helper function to map a wallet model and its balance result to a UI item.
     * This keeps the mapping logic clean and reusable.
     */
    private fun createUiHomeWalletItem(
        pwm: PortfolioWalletModel,
        balanceResult: Result<TotalBalance, DataError>
    ): UIHomeWalletItem {
        val uiItem = UIHomeWalletItem(
            id = pwm.wallet.id,
            address = pwm.wallet.address,
            name = pwm.name,
            formatedBalanceQuote = null, // Default value
            formatedChangePercent = null,
            changePercentSign = null
        )

        balanceResult.onSuccess { totalBalance ->
            val formatedChangePercent = formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO)
            return uiItem.copy(
                formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                formatedChangePercent = formatedChangePercent,
                changePercentSign = formatedChangePercent.toSign()
            )
        }

        return uiItem
    }
}