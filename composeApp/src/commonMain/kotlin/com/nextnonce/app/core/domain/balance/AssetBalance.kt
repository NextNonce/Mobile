package com.nextnonce.app.core.domain.balance

import com.nextnonce.app.core.domain.token.Asset

data class AssetBalance (
    val asset: Asset,
    val balance: Balance
)