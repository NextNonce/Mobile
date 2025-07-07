package com.nextnonce.app.home.presentation

import com.nextnonce.app.core.enums.NumberSign

/**
 * Represents a wallet item in the home screen.
 *
 * @property id Unique identifier for the wallet.
 * @property address The address of the wallet.
 * @property name Optional name of the wallet.
 * @property formatedBalanceQuote Formatted balance in quote currency, or null.
 * @property formatedChangePercent Formatted change percentage, or null.
 * @property changePercentSign Sign of the change percentage (positive, negative, or zero).
 * @property isLoading Indicates whether the wallet item is currently loading data.
 */
data class UIHomeWalletItem(
    val id: String,
    val address: String,
    val name: String?,
    val formatedBalanceQuote: String?,
    val formatedChangePercent: String?,
    val changePercentSign: NumberSign?,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}
