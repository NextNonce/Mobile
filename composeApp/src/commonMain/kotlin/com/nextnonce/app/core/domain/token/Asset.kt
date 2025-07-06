package com.nextnonce.app.core.domain.token

import com.nextnonce.app.core.domain.balance.Balance

/**
 * Represents a generic asset in the system, which can be either a token or a unified token.
 * This interface defines the common properties that all assets must have.
 * @property id A unique identifier for the asset, typically a combination of chain name and address for tokens.
 * @property tokenMetadata Metadata associated with the token, such as name, symbol, and decimals.
 * @property tokenPrice The current price of the token.
 */
sealed interface Asset {
    val id: String
    val tokenMetadata: TokenMetadata
    val tokenPrice: TokenPrice
}

/**
 * Represents a token on a specific blockchain.
 * @property chainName The name of the blockchain the token belongs to.
 * @property address The contract address of the token on the blockchain.
 * @property tokenMetadata Metadata associated with the token, such as name, symbol, and decimals.
 * @property tokenPrice The current price of the token.
 */
data class Token(
    val chainName: String,
    val address: String,
    override val tokenMetadata: TokenMetadata,
    override val tokenPrice: TokenPrice,
): Asset {
    override val id get() = "$chainName:$address"
}

/**
 * Represents a unified token that can consist of multiple tokens and their balances.
 * @property tokens A list of tokens that make up the unified token.
 * @property balances A list of balances corresponding to the tokens.
 * @property tokenMetadata Metadata associated with the unified token.
 * @property tokenPrice The current price of the unified token.
 */
data class UnifiedToken(
    val tokens: List<Token>,
    val balances: List<Balance>,
    override val tokenMetadata: TokenMetadata,
    override val tokenPrice: TokenPrice
): Asset {
    override val id get() = tokens.joinToString(separator = ":") { it.id }
}