package com.nextnonce.app.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import platform.Foundation.NSDecimalNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun formatQuote(amount: BigDecimal, showDecimal: Boolean): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle

        when {
            !showDecimal -> {
                minimumFractionDigits = 0uL
                maximumFractionDigits = 0uL
            }
            amount >= 0.01 || amount.compareTo(BigDecimal.ZERO) == 0  -> {
                minimumFractionDigits = 2uL
                maximumFractionDigits = 2uL
            }
            else -> {
                minimumFractionDigits = 8uL
                maximumFractionDigits = 8uL
            }
        }
    }

    val decimalNumber = NSDecimalNumber(string = amount.toString())
    return formatter.stringFromNumber(decimalNumber)?.let { "$$it" } ?: ""
}

actual fun formatQuoteChange(amount: BigDecimal, showDecimal: Boolean): String {
    if (amount.abs() <= "0.01".toBigDecimal()) {
        if (!showDecimal) {
            return "$0"
        }
        return "$0.00"
    }
    val prefix = if (amount > BigDecimal.ZERO) "+" else "-"
    val formattedQuote = formatQuote(amount.abs(), showDecimal)
    return "$prefix$formattedQuote"
}

actual fun formatBalanceNative(amount: BigDecimal, symbol: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        when {
            amount >= 1 || amount.compareTo(BigDecimal.ZERO) == 0  -> {
                minimumFractionDigits = 3uL
                maximumFractionDigits = 3uL
            }
            else -> {
                minimumFractionDigits = 8uL
                maximumFractionDigits = 8uL
            }
        }
    }
    val decimalNumber = NSDecimalNumber(string = amount.toString())
    val formattedAmount = formatter.stringFromNumber(decimalNumber) ?: ""

    return "$formattedAmount $symbol"
}

actual fun formatPercentage(amount: BigDecimal): String {
    if (amount.abs() <= "0.01".toBigDecimal()) {
        return "0.00%"
    }
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        minimumFractionDigits = 2uL
        maximumFractionDigits = 2uL
    }
    val decimalNumber = NSDecimalNumber(double = amount.doubleValue(exactRequired = false))
    val prefix = if (amount > BigDecimal.ZERO) "+" else ""
    val formattedAmount = formatter.stringFromNumber(decimalNumber) ?: ""
    return "$prefix$formattedAmount%"
}