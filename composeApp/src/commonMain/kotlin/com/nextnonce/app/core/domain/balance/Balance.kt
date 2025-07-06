package com.nextnonce.app.core.domain.balance

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Represents the balance of an asset in both native and quote currencies.
 *
 * @property balanceNative The balance in the native currency (e.g., ETH, LINK).
 * @property balanceQuote The balance in the quote currency (e.g., USD, EUR).
 * @property balanceQuoteChange The change in the quote balance, if available.
 * @property balanceQuoteChangePercent The percentage change in the quote balance, if available.
 */
data class Balance(
    val balanceNative: BigDecimal,
    val balanceQuote: BigDecimal,
    val balanceQuoteChange: BigDecimal?,
    val balanceQuoteChangePercent: BigDecimal?
)