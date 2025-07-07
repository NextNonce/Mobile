package com.nextnonce.app.wallet.presentation

import com.nextnonce.app.balance.presentation.UIAssetBalanceListItem
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the state of the wallet screen.
 * @property isLoading Indicates if the wallet data is currently loading.
 * @property uiWalletInfo The UI representation of the wallet information.
 * @property uiWalletTotalBalance The UI representation of the total balance in the wallet.
 * @property assetBalances A list of UI representations of asset balances in the wallet.
 * @property error An optional error message to display if there was an issue loading the wallet data.
 */
data class WalletState(
    val isLoading: Boolean = true,
    val uiWalletInfo: UIWalletInfo? = null,
    val uiWalletTotalBalance: UIWalletTotalBalance = UIWalletTotalBalance(),
    val assetBalances: List<UIAssetBalanceListItem> = emptyList(),
    val error: StringResource? = null,
)
