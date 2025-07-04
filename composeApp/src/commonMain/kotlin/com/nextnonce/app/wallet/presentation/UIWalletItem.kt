package com.nextnonce.app.wallet.presentation

import com.nextnonce.app.core.enums.NumberSign

data class UIWalletInfo(
    val id: String,
    val address: String,
    val name: String?,
    val walletType: String,
)

data class UIWalletTotalBalance(
    val formatedBalanceQuote: String? = null,
    val formattedBalanceQuoteChange: String? = null,
    val formatedChangePercent: String? = null,
    val changePercentSign: NumberSign? = null,
) {
    val isLoading: Boolean = formatedBalanceQuote == null && formatedChangePercent == null
}