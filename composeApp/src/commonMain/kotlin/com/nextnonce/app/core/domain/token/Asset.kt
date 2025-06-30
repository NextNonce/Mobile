package com.nextnonce.app.core.domain.token

import com.nextnonce.app.core.domain.balance.Balance

sealed interface Asset {
    val id: String
    val tokenMetadata: TokenMetadata
    val tokenPrice: TokenPrice
}

data class Token(
    val chainName: String,
    val address: String,
    override val tokenMetadata: TokenMetadata,
    override val tokenPrice: TokenPrice,
): Asset {
    override val id get() = "$chainName:$address"
}

data class UnifiedToken(
    val tokens: List<Token>,
    val balances: List<Balance>,
    override val tokenMetadata: TokenMetadata,
    override val tokenPrice: TokenPrice
): Asset {
    override val id get() = tokens.joinToString(separator = ":") { it.id }
}