package com.nextnonce.app.wallet.presentation

import com.nextnonce.app.core.enums.NumberSign

/**
 * Represents a UI model for displaying wallet information in the user interface.
 * @property id The unique identifier of the wallet.
 * @property address The blockchain address of the wallet.
 * @property name The name of the wallet, if available.
 * @property walletType The type of the wallet (e.g., SIMPLE, SMART).
 */
data class UIWalletInfo(
    val id: String,
    val address: String,
    val name: String?,
    val walletType: String,
)

/**
 * Represents a UI model for displaying the total balance of a wallet.
 * @property formatedBalanceQuote The formatted total balance in quote currency.
 * @property formattedBalanceQuoteChange The formatted change in the total balance in quote currency.
 * @property formatedChangePercent The formatted percentage change in the total balance.
 * @property changePercentSign The sign of the change percentage (e.g., positive or negative).
 * @property isLoading Indicates whether the total balance data is currently loading.
 */
data class UIWalletTotalBalance(
    val formatedBalanceQuote: String? = null,
    val formattedBalanceQuoteChange: String? = null,
    val formatedChangePercent: String? = null,
    val changePercentSign: NumberSign? = null,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}