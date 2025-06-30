package com.nextnonce.app.core.data.remote.dto.token

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TokenMetadataDto(
    val symbol: String,
    val name: String,
    val decimals: Int,
    val logoUrl: String? = null,
    val description: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
)
