package com.nextnonce.app.core.domain.token

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.Instant

/**
 * Represents the price of a token in a specific quote currency.
 *
 * @property priceQuote The price of the token in the quote currency (e.g., USD, EUR).
 * @property change The change in the token's price, if available.
 * @property timestamp The timestamp when the price was last updated.
 */
data class TokenPrice(
    val priceQuote: BigDecimal,
    val change: BigDecimal? = null,
    val timestamp: Instant
)
