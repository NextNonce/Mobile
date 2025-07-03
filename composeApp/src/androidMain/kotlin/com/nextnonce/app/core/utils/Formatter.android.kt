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

actual fun formatBalanceNative(amount: BigDecimal, symbol: String): String {
    val amountJvmBd = JvmBigDecimal(amount.toString())
    val pattern = when {
        amountJvmBd >= JvmBigDecimal.TEN -> "#,###.000"
        amountJvmBd.compareTo(JvmBigDecimal.ZERO) == 0 -> "0.000"
        else -> "0.00000000"
    }

    val formatter = DecimalFormat(pattern)
    return formatter.format(amountJvmBd) + " $symbol"
}

actual fun formatPercentage(amount: BigDecimal): String {
    val prefix = if (amount > BigDecimal.ZERO) "+" else ""
    val amountJvmBd = JvmBigDecimal(amount.toString())
    return prefix + DecimalFormat("0.00").format(amountJvmBd) + "%"
}