package com.nextnonce.app.core.domain.balance

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Represents the total balance in a quote currency, including changes and percentage changes.
 *
 * @property balanceQuote The total balance in the quote currency (e.g., USD, EUR).
 * @property balanceQuoteChange The change in the total balance in the quote currency, if available.
 * @property balanceQuoteChangePercent The percentage change in the total balance in the quote currency, if available.
 */
data class TotalBalance(
    val balanceQuote: BigDecimal,
    val balanceQuoteChange: BigDecimal?,
    val balanceQuoteChangePercent: BigDecimal?
)
