package com.nextnonce.app.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal

/**
 * Formats a BigDecimal amount of quote currency, optionally showing decimal places.
 * @param amount The amount to format.
 * @param showDecimal Whether to show decimal places in the formatted string.
 * @return A formatted string representing the amount in quote currency.
 */
expect fun formatQuote(amount: BigDecimal, showDecimal: Boolean = true): String

/**
 * Formats a BigDecimal amount of quote currency change, optionally showing decimal places.
 * @param amount The change amount to format.
 * @param showDecimal Whether to show decimal places in the formatted string.
 * @return A formatted string representing the change in quote currency.
 */
expect fun formatQuoteChange(amount: BigDecimal, showDecimal: Boolean = true): String

/**
 * Formats a BigDecimal amount of token, optionally showing decimal places.
 * @param amount The amount to format.
 * @param symbol The symbol of the native cryptocurrency (e.g., ETH, LINK).
 * @return A formatted string representing the amount in native currency.
 */
expect fun formatBalanceNative(amount: BigDecimal, symbol: String): String


/**
 * Formats a BigDecimal percentage change amount, typically used for displaying changes in token prices or balances.
 * @param amount The change amount to format.
 * @return A formatted string representing the percentage change.
 */
expect fun formatPercentage(amount: BigDecimal): String