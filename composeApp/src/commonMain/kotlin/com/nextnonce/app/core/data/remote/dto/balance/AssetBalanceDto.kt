package com.nextnonce.app.core.data.remote.dto.balance

import com.nextnonce.app.core.data.remote.dto.token.AssetDto
import kotlinx.serialization.Serializable

@Serializable
data class AssetBalanceDto(
    val asset: AssetDto,
    val balance: BalanceDto
)