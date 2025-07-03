package com.nextnonce.app.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.core.enums.NumberSign

/**
 * Determines the sign of a BigDecimal value using its built-in signum() method.
 * @return NumberSign.NEGATIVE if value < 0, NumberSign.ZERO if value == 0, NumberSign.POSITIVE if value > 0.
 * Returns null if the input value is null.
 */
fun BigDecimal?.toSign(): NumberSign? {
    return this?.let {
        when (it.signum()) { // <--- Using the built-in signum() method
            -1 -> NumberSign.NEGATIVE
            0 -> NumberSign.ZERO
            1 -> NumberSign.POSITIVE
            else -> throw IllegalStateException("BigDecimal.signum() returned unexpected value: ${it.signum()}") // Should not happen
        }
    }
}