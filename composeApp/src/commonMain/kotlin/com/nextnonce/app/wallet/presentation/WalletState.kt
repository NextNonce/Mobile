package com.nextnonce.app.wallet.presentation

import com.nextnonce.app.balance.presentation.UIAssetBalanceListItem
import org.jetbrains.compose.resources.StringResource

data class WalletState(
    val isLoading: Boolean = true,
    val assetBalances: List<UIAssetBalanceListItem> = emptyList(),
    val error: StringResource? = null,
)
