package com.nextnonce.app.portfolio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.balance.presentation.mapper.toUIAssetBalanceListItem
import com.nextnonce.app.balance.presentation.toggleUnified
import com.nextnonce.app.core.domain.onError
import com.nextnonce.app.core.domain.onSuccess
import com.nextnonce.app.core.domain.portfolio.toStringValue
import com.nextnonce.app.core.utils.formatPercentage
import com.nextnonce.app.core.utils.formatQuote
import com.nextnonce.app.core.utils.formatQuoteChange
import com.nextnonce.app.core.utils.toSign
import com.nextnonce.app.core.utils.toUIText
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.portfolio.domain.GetDefaultPortfolioUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioAssetBalancesUseCase
import com.nextnonce.app.portfolio.domain.GetPortfolioTotalBalanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state of a portfolio, fetching portfolio details,
 * total balance, and asset balances.
 *
 * @property id The unique identifier of the portfolio.
 * @property getDefaultPortfolioUseCase Use case for fetching the default portfolio.
 * @property getPortfolioAssetBalancesUseCase Use case for fetching asset balances in the portfolio.
 * @property getPortfolioTotalBalanceUseCase Use case for fetching the total balance of the portfolio.
 */
class PortfolioViewModel(
    private val id: String,
    private val getDefaultPortfolioUseCase: GetDefaultPortfolioUseCase,
    private val getPortfolioAssetBalancesUseCase: GetPortfolioAssetBalancesUseCase,
    private val getPortfolioTotalBalanceUseCase: GetPortfolioTotalBalanceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PortfolioState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PortfolioState()
        )

    init {
        viewModelScope.launch {
            launch { getDefaultPortfolio() }
            launch { getPortfolioTotalBalance() }
            launch { getAssetBalances() }
        }
    }

    /**
     * Fetches the default portfolio and updates the state with the portfolio information.
     * If successful, it updates the UIPortfolioInfo with the portfolio's ID, name, and access level.
     * If an error occurs, it updates the state with the error message.
     */
    private suspend fun getDefaultPortfolio() {
        val portfolio = getDefaultPortfolioUseCase.execute()
        portfolio
            .collect { result ->
                result.onSuccess { portfolioData ->
                    val portfolio = portfolioData.portfolio
                    AppLogger.d {
                        "PortfolioViewModel: Successfully fetched default portfolio: ${portfolio.name}"
                    }
                    _state.update { it ->
                        it.copy(
                            uiPortfolioInfo = UIPortfolioInfo(
                                id = portfolio.id,
                                name = portfolio.name,
                                access = portfolio.access.toStringValue()
                            )
                        )
                    }
                }.onError { error ->
                    AppLogger.e {
                        "PortfolioViewModel: Error fetching default portfolio: $error"
                    }
                    _state.update {
                        it.copy(
                            error = error.toUIText()
                        )
                    }
                }
            }
    }

    /**
     * Fetches the total balance for the portfolio and updates the state with the results.
     * If successful, it updates the UIPortfolioTotalBalance with formatted values.
     * If an error occurs, it updates the state with the error message.
     */
    private suspend fun getPortfolioTotalBalance() {
        getPortfolioTotalBalanceUseCase.execute(id)
            .collect { result ->
                result.onSuccess { totalBalance ->
                    AppLogger.d {
                        "PortfolioViewModel: Successfully fetched total balance for portfolio $id: $totalBalance"
                    }
                    _state.update { it ->
                        val formatedChangePercent = formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO)
                        it.copy(
                            uiPortfolioTotalBalance = UIPortfolioTotalBalance(
                                formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                                formattedBalanceQuoteChange = formatQuoteChange(totalBalance.balanceQuoteChange ?: BigDecimal.ZERO),
                                formatedChangePercent = formatedChangePercent,
                                changePercentSign = formatedChangePercent.toSign()
                            ),
                        )
                    }
                }.onError { error ->
                    AppLogger.e {
                        "PortfolioViewModel: Error fetching total balance for portfolio $id: $error"
                    }
                    _state.update {
                        it.copy(
                            error = error.toUIText()
                        )
                    }
                }
            }
    }

    /**
     * Fetches the asset balances for the portfolio and updates the state with the results.
     */
    private suspend fun getAssetBalances() {
        getPortfolioAssetBalancesUseCase.execute(id)
            .onStart {
                _state.update { it.copy(isLoading = true, error = null) }
            }
            .collect { result ->
                result.onSuccess { assetBalances ->
                    AppLogger.d {
                        "PortfolioViewModel:  Successfully fetched asset balances for portfolio $id: ${assetBalances.size} items"
                    }
                    _state.update { it ->
                        it.copy(
                            isLoading = false,
                            assetBalances = assetBalances.map { assetBalance ->
                                assetBalance.toUIAssetBalanceListItem()
                            },
                            error = null
                        )
                    }
                }.onError { error ->
                    AppLogger.e {
                        "PortfolioViewModel: Error fetching asset balances for portfolio $id: $error"
                    }
                    _state.update {
                        it.copy(
                            error = error.toUIText()
                        )
                    }
                }
            }
    }

    /**
     * Expands or collapses a unified token balance item by toggling its expanded state.
     * This function updates the state of the portfolio to reflect the change in the unified token's expanded state.
     * @param tokenId The unique identifier of the unified token to be expanded or collapsed.
     */
    fun expandUnifiedToken(tokenId: String) {
        _state.update { it.copy(
            assetBalances = it.assetBalances.toggleUnified(tokenId)
        )}
    }
}

