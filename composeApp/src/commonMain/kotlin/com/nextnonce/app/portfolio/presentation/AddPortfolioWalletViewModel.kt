package com.nextnonce.app.portfolio.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextnonce.app.core.domain.onError
import com.nextnonce.app.core.domain.onSuccess
import com.nextnonce.app.core.utils.toUIText
import com.nextnonce.app.logging.AppLogger
import com.nextnonce.app.portfolio.domain.CreatePortfolioWalletUseCase
import com.nextnonce.app.portfolio.domain.model.CreatePortfolioWalletCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state of adding a wallet to a portfolio.
 *
 * @property createPortfolioWalletUseCase Use case for creating a portfolio wallet.
 */
class AddPortfolioWalletViewModel(
    private val createPortfolioWalletUseCase: CreatePortfolioWalletUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddPortfolioWalletState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AddPortfolioWalletState(isLoading = false)
        )

    /**
     * Handles changes to the wallet address input field.
     *
     * @param newAddress The new wallet address input.
     */
    fun onAddressChange(newAddress: TextFieldValue) {
        if (newAddress.text.length <= 255) {
            _state.update { it.copy(walletAddress = newAddress) }
        }
    }

    /**
     * Handles changes to the wallet name input field.
     *
     * @param newName The new name for the wallet.
     */
    fun onNameChange(newName: String) {
        if (newName.length <= 18) {
            _state.update { it.copy(walletName = newName) }
        }
    }

    /**
     * Resets the wallet address selection when the focus is lost.
     */
    fun onAddressFocusLost() {
        _state.update {
            // Reset the selection to the beginning
            it.copy(walletAddress = it.walletAddress.copy(selection = TextRange.Zero))
        }
    }

    /**
     * Adds a wallet to the portfolio with the provided ID.
     * This function constructs a command with the wallet address and name,
     * then calls the use case to execute the addition.
     */
    fun addPortfolioWallet(
        portfolioId: String,
    ) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val walletAddress = _state.value.walletAddress.text
            val walletName = _state.value.walletName.takeIf { !it.isNullOrBlank() }
            AppLogger.d {
                "Adding portfolio wallet: address=$walletAddress, name=$walletName"
            }
            val command = CreatePortfolioWalletCommand(
                address = walletAddress,
                name = walletName
            )
            createPortfolioWalletUseCase.execute(portfolioId, command)
                .onSuccess {
                    AppLogger.d {
                        "Portfolio wallet added successfully: address=$walletAddress, name=$walletName"
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isWalletAdded = true
                    )
                }
                .onError { error ->
                    AppLogger.e {
                        "Error adding portfolio wallet: ${error}, address=$walletAddress, name=$walletName"
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.toUIText()
                    )
                }
        }
    }
}