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
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

/**
 * ViewModel for the Home screen, responsible for managing the state of the home portfolio and wallets.
 * It fetches the default portfolio, its total balance, and the wallets associated with it.
 *
 * @property getDefaultPortfolioUseCase Use case to fetch the default portfolio.
 * @property getPortfolioTotalBalanceUseCase Use case to fetch the total balance of a portfolio.
 * @property getPortfolioWalletsUseCase Use case to fetch wallets associated with a portfolio.
 * @property getWalletTotalBalanceUseCase Use case to fetch the total balance of a wallet.
 */
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

    /**
     * Observes the default portfolio and updates the state with the portfolio information.
     * It also kicks off the observation of the portfolio's total balance and wallets.
     */
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

    /**
     * Observes the total balance of the portfolio and updates the state with the formatted balance and change percent.
     * @param portfolioId The ID of the portfolio to observe.
     */
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
     * Observes the wallets associated with the portfolio and updates the state with the list of wallets.
     * HYBRID LOGIC:
     * 1. Fetches the list of wallets.
     * 2. IMMEDIATELY displays that list with "loading" placeholders for balances.
     * 3. Launches a SINGLE background job that uses `combine` to efficiently fetch all balances.
     * 4. Continuously collects updates from the combined flow to show cached, then fresh data.
     * This provides immediate feedback while ensuring efficient state updates.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePortfolioWallets(portfolioId: String) = viewModelScope.launch {
        getPortfolioWalletsUseCase.execute(portfolioId).flatMapLatest { walletListResult ->
            when (walletListResult) {
                is Result.Error -> {
                    _state.update { it.copy(error = walletListResult.error.toUIText(), areWalletsLoading = false) }
                    // Return a flow that does nothing to stop the chain.
                    emptyFlow()
                }
                is Result.Success -> {
                    val walletModels = walletListResult.data

                    if (walletModels.isEmpty()) {
                        // If the list is empty, just emit an empty list for the UI.
                        flowOf(emptyList<UIHomeWalletItem>())
                    } else {
                        // Create the balance flows for the new list of wallets.
                        val balanceFlows = walletModels.map { pwm ->
                            getWalletTotalBalanceUseCase.execute(pwm.wallet.id)
                        }

                        // Combine them to get the latest list of balances.
                        val combinedBalancesFlow = combine(balanceFlows) { latestBalances ->
                            walletModels.mapIndexed { index, pwm ->
                                createUiHomeWalletItem(pwm, latestBalances[index])
                            }
                        }

                        // Create the initial placeholder list to show immediately.
                        val initialPlaceholderList = walletModels.map { createUiHomeWalletItem(it, null) }

                        // Use onStart to emit the placeholder list first, then continue with the combined flow.
                        combinedBalancesFlow.onStart { emit(initialPlaceholderList) }
                    }
                }
            }
        }
            .onStart { _state.update { it.copy(areWalletsLoading = true) } }
            .collect { finalWalletsList ->
                // This single collect block now receives the initial placeholder list,
                // and then any subsequent updates from the combined flow (cache then network).
                _state.update {
                    it.copy(
                        wallets = finalWalletsList,
                        areWalletsLoading = false // Loading is complete after the first emission.
                    )
                }
            }
    }


    /**
     * Helper function to map a wallet model and its balance result to a UI item.
     * Handles a nullable balanceResult to create loading states.
     * @param pwm The PortfolioWalletModel to map.
     * @param balanceResult The Result<TotalBalance, DataError> containing the balance data or null if loading.
     * @return A UIHomeWalletItem representing the wallet with its balance and change percent.
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