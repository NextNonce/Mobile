package com.nextnonce.app.portfolio.presentation

import androidx.compose.ui.text.input.TextFieldValue
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the state of the Add Portfolio's wallet screen.
 *
 * @property isLoading Indicates whether the wallet addition process is currently loading.
 * @property error An optional error message to display if an error occurs while adding the wallet.
 * @property walletAddress The current value of the wallet address input field.
 * @property walletName The name of the wallet being added, if provided.
 * @property isWalletAdded Indicates whether the wallet has been successfully added.
 */
data class AddPortfolioWalletState(
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val walletAddress: TextFieldValue = TextFieldValue(""),
    val walletName: String? = null,
    val isWalletAdded: Boolean = false,
)