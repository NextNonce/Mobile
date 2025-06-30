package com.nextnonce.app.core.domain.token

data class TokenMetadata(
    val symbol: String,
    val name: String,
    val decimals: Int,
    val logoUrl: String? = null,
    val description: String? = null,
)
