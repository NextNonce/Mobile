package com.nextnonce.app.wallet.domain.model

import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.balance.TotalBalance

/**
 * Represents the wallet balances model containing total balance and asset balances.
 *
 * @property totalBalance The total balance of the wallet.
 * @property assetBalances A list of asset balances in the wallet.
 */
data class WalletBalancesModel(
    val totalBalance: TotalBalance,
    val assetBalances: List<AssetBalance> = emptyList()
)
