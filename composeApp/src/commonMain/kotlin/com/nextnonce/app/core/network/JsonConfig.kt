package com.nextnonce.app.core.network

import com.ionspin.kotlin.bignum.serialization.kotlinx.humanReadableSerializerModule
import com.nextnonce.app.core.data.remote.dto.token.AssetDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus

/** JSON engine that can handle BigInteger/BigDecimal as decimal strings. */
val JsonHumanReadable = Json {
    prettyPrint       = true
    ignoreUnknownKeys = true
    isLenient         = true
    explicitNulls     = false

    serializersModule = humanReadableSerializerModule + AssetDto.module
}