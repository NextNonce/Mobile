package com.nextnonce.app.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.balance.presentation.UIUnifiedTokenBalanceListItem
import com.nextnonce.app.balance.presentation.mapper.toUIAssetBalanceListItem
import com.nextnonce.app.core.domain.onError
import com.nextnonce.app.core.domain.onSuccess
import com.nextnonce.app.core.domain.wallet.toStringValue
import com.nextnonce.app.core.utils.formatPercentage
import com.nextnonce.app.core.utils.formatQuote
import com.nextnonce.app.core.utils.formatQuoteChange
import com.nextnonce.app.core.utils.toSign
import com.nextnonce.app.core.utils.toUIText
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.wallet.domain.GetWalletAssetBalancesUseCase
import com.nextnonce.app.wallet.domain.GetWalletTotalBalanceUseCase
import com.nextnonce.app.wallet.domain.GetWalletUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletViewModel(
    private val id: String,
    private val name: String? = null,
    private val getWalletUseCase: GetWalletUseCase,
    private val getWalletAssetBalancesUseCase: GetWalletAssetBalancesUseCase,
    private val getWalletTotalBalanceUseCase: GetWalletTotalBalanceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WalletState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WalletState()
        )

    init {
        // kick off loading without blocking the `state` flow
        viewModelScope.launch {
            launch { getWallet() }
            launch { getWalletTotalBalance() }
            launch { getAssetBalances() }
        }
    }

    private suspend fun getWallet() {
        val wallet = getWalletUseCase.execute(id)
        wallet.onSuccess { walletData ->
            AppLogger.d {
                "WalletViewModel: Successfully fetched wallet $id: ${walletData.wallet.address}"
            }
            _state.update { it ->
                it.copy(
                    uiWalletInfo = UIWalletInfo(
                        id = walletData.wallet.id,
                        address = walletData.wallet.address,
                        name = name,
                        walletType = walletData.walletType.toStringValue()
                    ),
                )
            }
        }.onError { error ->
            AppLogger.e {
                "WalletViewModel: Error fetching wallet $id: $error"
            }
            _state.update {
                it.copy(
                    error = error.toUIText()
                )
            }
        }
    }

    private suspend fun getWalletTotalBalance() {
        getWalletTotalBalanceUseCase.execute(id)
            .onStart {
                _state.update { it.copy(error = null) }
            }
            .collect { result ->
                result.onSuccess { totalBalance ->
                    AppLogger.d {
                        "WalletViewModel: Successfully fetched total balance for wallet $id: $totalBalance"
                    }
                    _state.update { it ->
                        it.copy(
                            uiWalletTotalBalance = UIWalletTotalBalance(
                                formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                                formattedBalanceQuoteChange = formatQuoteChange(totalBalance.balanceQuoteChange ?: BigDecimal.ZERO),
                                formatedChangePercent = formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO),
                                changePercentSign =  totalBalance.balanceQuoteChangePercent?.toSign()
                            ),
                        )
                    }
                }.onError { error ->
                    AppLogger.e {
                        "WalletViewModel: Error fetching total balance for wallet $id: $error"
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
        getWalletAssetBalancesUseCase.execute(id)
            .onStart {
                _state.update { it.copy(isLoading = true, error = null) }
            }
            .collect { result ->
                result.onSuccess { assetBalances ->
                    AppLogger.d {
                        "WalletViewModel: Successfully fetched asset balances for wallet $id: ${assetBalances.size} items"
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
                        "WalletViewModel: Error fetching asset balances for wallet $id: $error"
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.toUIText()
                        )
                    }
                }
            }
    }

    fun expandUnifiedToken(tokenId: String) {
        _state.update { currentState ->
            val updatedAssetBalances = currentState.assetBalances.map { assetBalance ->
                // Check if this is the item we want to expand/collapse
                if (assetBalance is UIUnifiedTokenBalanceListItem) {
                    if (assetBalance.id == tokenId) {
                        // If it is, create a new instance with the toggled 'isExpanded' state
                        assetBalance.copy(isExpanded = !assetBalance.isExpanded)
                    } else {
                        assetBalance.copy(isExpanded = false)
                    }
                } else {
                    // Otherwise, return the item as is
                    assetBalance
                }
            }
            currentState.copy(assetBalances = updatedAssetBalances)
        }
    }
}