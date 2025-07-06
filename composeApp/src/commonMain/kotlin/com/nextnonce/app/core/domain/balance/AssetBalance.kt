package com.nextnonce.app.core.domain.balance

import com.nextnonce.app.core.domain.token.Asset

/**
 * Represents the balance of a specific asset.
 *
 * @property asset The asset for which the balance is being represented.
 * @property balance The balance of the asset.
 */
data class AssetBalance (
    val asset: Asset,
    val balance: Balance
)