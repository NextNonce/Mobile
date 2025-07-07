package com.nextnonce.app.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.balance.presentation.mapper.toUIAssetBalanceListItem
import com.nextnonce.app.balance.presentation.toggleUnified
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

/**
 * ViewModel for managing the state of a wallet, fetching wallet details,
 * total balance, and asset balances.
 *
 * @property id The unique identifier of the wallet.
 * @property name The name of the wallet (nullable).
 * @property getWalletUseCase Use case for fetching wallet details.
 * @property getWalletAssetBalancesUseCase Use case for fetching asset balances in the wallet.
 * @property getWalletTotalBalanceUseCase Use case for fetching the total balance of the wallet.
 */
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
        viewModelScope.launch {
            launch { getWallet() }
            launch { getWalletTotalBalance() }
            launch { getAssetBalances() }
        }
    }

    /**
     * Fetches the wallet details using the provided ID and updates the state with the results.
     * If successful, it updates the UIWalletInfo with the wallet's address, name, and type.
     * If an error occurs, it updates the state with the error message.
     */
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

    /**
     * Fetches the total balance for the wallet and updates the state with the results.
     */
    private suspend fun getWalletTotalBalance() {
        getWalletTotalBalanceUseCase.execute(id)
            .collect { result ->
                result.onSuccess { totalBalance ->
                    AppLogger.d {
                        "WalletViewModel: Successfully fetched total balance for wallet $id: $totalBalance"
                    }
                    _state.update { it ->
                        val formatedChangePercent = formatPercentage(totalBalance.balanceQuoteChangePercent ?: BigDecimal.ZERO)
                        it.copy(
                            uiWalletTotalBalance = UIWalletTotalBalance(
                                formatedBalanceQuote = formatQuote(totalBalance.balanceQuote),
                                formattedBalanceQuoteChange = formatQuoteChange(totalBalance.balanceQuoteChange ?: BigDecimal.ZERO),
                                formatedChangePercent = formatedChangePercent,
                                changePercentSign =  formatedChangePercent.toSign()
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

    /**
     * Fetches the asset balances for the wallet and updates the state with the results.
     */
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

    /**
     * Expands or collapses a unified token balance item by toggling its expanded state.
     * This function updates the state of the wallet to reflect the change in the unified token's expanded state.
     * @param tokenId The unique identifier of the unified token to be expanded or collapsed.
     */
    fun expandUnifiedToken(tokenId: String) {
        _state.update { it.copy(
            assetBalances = it.assetBalances.toggleUnified(tokenId)
        )}
    }
}