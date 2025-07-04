package com.nextnonce.app.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal

expect fun formatQuote(amount: BigDecimal, showDecimal: Boolean = true): String

expect fun formatQuoteChange(amount: BigDecimal, showDecimal: Boolean = true): String

expect fun formatBalanceNative(amount: BigDecimal, symbol: String): String

expect fun formatPercentage(amount: BigDecimal): String