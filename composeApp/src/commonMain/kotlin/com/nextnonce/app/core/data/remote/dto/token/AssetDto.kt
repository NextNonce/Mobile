package com.nextnonce.app.core.data.remote.dto.token

import com.nextnonce.app.core.data.remote.dto.balance.BalanceDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
sealed interface AssetDto {
    companion object {
        val module = SerializersModule {
            polymorphic(AssetDto::class) {
                subclass(TokenDto::class)
                subclass(UnifiedTokenDto::class)
            }
        }
    }
}

@Serializable
@SerialName("single")
data class TokenDto(
    val chainName: String,
    val address: String,
    val tokenMetadata: TokenMetadataDto,
    val tokenPrice: TokenPriceDto,
): AssetDto

@Serializable
@SerialName("unified")
data class UnifiedTokenDto(
    val tokens: List<TokenDto>,
    val balances: List<BalanceDto>,
    val tokenMetadata: TokenMetadataDto,
    val tokenPrice: TokenPriceDto
): AssetDto
