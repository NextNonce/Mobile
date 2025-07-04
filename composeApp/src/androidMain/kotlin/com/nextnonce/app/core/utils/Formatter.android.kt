package com.nextnonce.app.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import java.math.BigDecimal as JvmBigDecimal
import java.text.DecimalFormat

actual fun formatQuote(amount: BigDecimal, showDecimal: Boolean): String {
    val amountJvmBd = JvmBigDecimal(amount.toString())
    val pattern = when {
        showDecimal.not() -> "#,###"
        amountJvmBd >= JvmBigDecimal.ONE -> "#,###.00"
        amountJvmBd >= JvmBigDecimal("0.01") -> "0.00"
        amountJvmBd.compareTo(JvmBigDecimal.ZERO) == 0 -> "0.00"
        else -> "0.00000000"
    }

    val formatter = DecimalFormat(pattern)
    return "$" + formatter.format(amountJvmBd)
}

actual fun formatQuoteChange(amount: BigDecimal, showDecimal: Boolean): String {
    val amountAbs = amount.abs()
    val amountJvmBd = JvmBigDecimal(amountAbs.toString())
    if (amountJvmBd <= JvmBigDecimal("0.01")) {
        if (!showDecimal) {
            return "$0"
        }
        return "$0.00"
    }
    val prefix = if (amount > BigDecimal.ZERO) "+" else "-"
    val formattedQuote = formatQuote(amountAbs, showDecimal)
    return prefix + formattedQuote
}

actual fun formatBalanceNative(amount: BigDecimal, symbol: String): String {
    val amountJvmBd = JvmBigDecimal(amount.toString())
    val pattern = when {
        amountJvmBd >= JvmBigDecimal.ONE -> "#,###.000"
        amountJvmBd.compareTo(JvmBigDecimal.ZERO) == 0 -> "0.000"
        else -> "0.00000000"
    }

    val formatter = DecimalFormat(pattern)
    return formatter.format(amountJvmBd) + " $symbol"
}

actual fun formatPercentage(amount: BigDecimal): String {
    val amountJvmBd = JvmBigDecimal(amount.toString())
    if (amountJvmBd.abs() <= JvmBigDecimal("0.01")) {
        return "0.00%"
    }
    val prefix = if (amount > BigDecimal.ZERO) "+" else ""
    return prefix + DecimalFormat("0.00").format(amountJvmBd) + "%"
}