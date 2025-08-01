package com.nextnonce.app.core.data.remote.dto.balance

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TotalBalanceDto(
    @Contextual
    val balanceQuote: BigDecimal,
    @Contextual
    val balanceQuoteChange: BigDecimal?,
    @Contextual
    val balanceQuoteChangePercent: BigDecimal?
)