package com.nextnonce.app.core.data.remote.dto.token

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TokenPriceDto(
    @Contextual
    val priceQuote: BigDecimal,
    @Contextual
    val change: BigDecimal? = null,
    val timestamp: Instant
)