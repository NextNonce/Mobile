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

    fun expandUnifiedToken(tokenId: String) {
        _state.update { it.copy(
            assetBalances = it.assetBalances.toggleUnified(tokenId)
        )}
    }
}

