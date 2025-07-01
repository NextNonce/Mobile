package com.nextnonce.app.home.presentation

import com.nextnonce.app.core.enums.NumberSign

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
