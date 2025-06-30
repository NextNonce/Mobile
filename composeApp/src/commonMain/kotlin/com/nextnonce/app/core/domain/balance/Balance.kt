package com.nextnonce.app.core.domain.balance

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class Balance(
    val balanceNative: BigDecimal,
    val balanceQuote: BigDecimal,
    val balanceQuoteChange: BigDecimal?,
    val balanceQuoteChangePercent: BigDecimal?
)