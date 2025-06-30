package com.nextnonce.app.core.domain.balance

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class TotalBalance(
    val balanceQuote: BigDecimal,
    val balanceQuoteChange: BigDecimal?,
    val balanceQuoteChangePercent: BigDecimal?
)
