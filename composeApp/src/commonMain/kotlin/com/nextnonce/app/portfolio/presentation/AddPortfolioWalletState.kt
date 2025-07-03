package com.nextnonce.app.portfolio.presentation

import androidx.compose.ui.text.input.TextFieldValue
import org.jetbrains.compose.resources.StringResource

data class AddPortfolioWalletState(
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val walletAddress: TextFieldValue = TextFieldValue(""),
    val walletName: String? = null,
    val isWalletAdded: Boolean = false,
)