package com.nextnonce.app.core.network

import com.ionspin.kotlin.bignum.serialization.kotlinx.humanReadableSerializerModule
import kotlinx.serialization.json.Json

/** JSON engine that can handle BigInteger/BigDecimal as decimal strings. */
val JsonHumanReadable = Json {
    prettyPrint       = true
    ignoreUnknownKeys = true
    isLenient         = true
    explicitNulls     = false

    serializersModule = humanReadableSerializerModule
}