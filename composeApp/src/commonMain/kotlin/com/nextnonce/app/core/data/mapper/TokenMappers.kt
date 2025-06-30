package com.nextnonce.app.core.data.mapper

import com.nextnonce.app.core.data.remote.dto.token.AssetDto
import com.nextnonce.app.core.data.remote.dto.token.TokenDto
import com.nextnonce.app.core.data.remote.dto.token.TokenMetadataDto
import com.nextnonce.app.core.data.remote.dto.token.TokenPriceDto
import com.nextnonce.app.core.data.remote.dto.token.UnifiedTokenDto
import com.nextnonce.app.core.domain.token.Token
import com.nextnonce.app.core.domain.token.TokenMetadata
import com.nextnonce.app.core.domain.token.TokenPrice
import com.nextnonce.app.core.domain.token.UnifiedToken


fun AssetDto.toAsset() = when (this) {
    is TokenDto -> toToken()
    is UnifiedTokenDto -> toUnifiedToken()
}

fun TokenDto.toToken() = Token(
    chainName = chainName,
    address = address,
    tokenMetadata = tokenMetadata.toTokenMetadata(),
    tokenPrice = tokenPrice.toTokenPrice()
)

fun UnifiedTokenDto.toUnifiedToken() = UnifiedToken(
    tokens = tokens.map { it.toToken() },
    balances = balances.map { it.toBalance() },
    tokenMetadata = tokenMetadata.toTokenMetadata(),
    tokenPrice = tokenPrice.toTokenPrice()
)

fun TokenMetadataDto.toTokenMetadata() = TokenMetadata(
    symbol = symbol,
    name = name,
    decimals = decimals,
    logoUrl = logoUrl,
    description = description
)

fun TokenPriceDto.toTokenPrice() = TokenPrice(
    priceQuote = priceQuote,
    change = change,
    timestamp = timestamp
)