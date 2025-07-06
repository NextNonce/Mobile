package com.nextnonce.app.core.domain.token

/**
 * Represents metadata for a token, including its symbol, name, decimals, logo URL, and an optional description.
 *
 * @property symbol The symbol of the token (e.g., "ETH", "LINK").
 * @property name The full name of the token (e.g., "Ethereum", "Chainlink").
 * @property decimals The number of decimal places the token can be divided into.
 * @property logoUrl An optional URL to the token's logo image.
 * @property description An optional description of the token.
 */
data class TokenMetadata(
    val symbol: String,
    val name: String,
    val decimals: Int,
    val logoUrl: String? = null,
    val description: String? = null,
)
