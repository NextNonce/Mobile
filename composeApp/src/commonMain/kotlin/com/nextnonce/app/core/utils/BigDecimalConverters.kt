package com.nextnonce.app.core.utils

import androidx.room.TypeConverter
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode

object BigDecimalConverters {
    @TypeConverter
    fun fromString(value: String?): BigDecimal? {
        return value?.let { BigDecimal.parseString(it) }
    }

    @TypeConverter
    fun toString(value: BigDecimal?): String? {
        return value?.toString()
    }
}

object NNBigDecimalModes {
    val TOKENS = DecimalMode(
        decimalPrecision = 38,
        RoundingMode.ROUND_HALF_AWAY_FROM_ZERO,
        scale = 18
    )
}