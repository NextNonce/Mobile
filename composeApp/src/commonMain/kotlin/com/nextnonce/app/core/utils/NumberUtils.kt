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

/**
 * Converts a String representation of a number to its corresponding NumberSign.
 * The first character of the string determines the sign:
 * - If it starts with '-', it returns NumberSign.NEGATIVE.
 * - If it starts with '+', it returns NumberSign.POSITIVE.
 * - If it starts with any other character or is empty, it returns NumberSign.ZERO.
 *
 * @return NumberSign corresponding to the first character of the string, or null if the string is empty.
 */
fun String.toSign(): NumberSign? {
    if (this.isEmpty()) return null
    return this.let {
        when(it[0]) {
            '-' -> NumberSign.NEGATIVE
            '+' -> NumberSign.POSITIVE
            else -> {
                NumberSign.ZERO
            }
        }
    }
}