package com.nextnonce.app.core.domain.token

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.Instant

data class TokenPrice(
    val priceQuote: BigDecimal,
    val change: BigDecimal? = null,
    val timestamp: Instant
)
