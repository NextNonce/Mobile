package com.nextnonce.app.core.network

import com.ionspin.kotlin.bignum.serialization.kotlinx.humanReadableSerializerModule
import com.nextnonce.app.core.data.remote.dto.token.AssetDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus

/**
 * Provides a JSON configuration for the application.
 * This configuration is used for serializing and deserializing JSON data.
 * It includes settings for pretty printing, ignoring unknown keys,
 * leniency, and explicit nulls.
 * It also includes BigDecimal and BigInteger serialization support
 * and the AssetDto serializer module.
 */
val JsonHumanReadable = Json {
    prettyPrint       = true
    ignoreUnknownKeys = true
    isLenient         = true
    explicitNulls     = false

    serializersModule = humanReadableSerializerModule + AssetDto.module
}