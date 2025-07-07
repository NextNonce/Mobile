package com.nextnonce.app.balance.presentation

import com.nextnonce.app.core.enums.NumberSign

/**
 * Represents a UI model for displaying asset balance items in the user interface.
 * This interface defines the common properties that all asset balance items should have.
 * It is implemented by both `UITokenBalanceListItem` and `UIUnifiedTokenBalanceListItem`.
 * @property id The unique identifier of the asset.
 * @property name The name of the asset.
 * @property assetLogoUrl The URL of the asset's logo, if available.
 * @property chainLogoUrl The URL of the blockchain's logo on which the asset resides.
 * @property formatedBalanceNative The formatted balance in native currency.
 * @property formatedBalanceQuote The formatted balance in quote currency.
 * @property formatedChangePercent The formatted percentage change of quote balance, if available.
 * @property changePercentSign The sign of the change percentage, indicating whether it is positive, negative, or zero.
 */
sealed interface UIAssetBalanceListItem {
    val id: String
    val name: String
    val assetLogoUrl: String?
    val chainLogoUrl: String
    val formatedBalanceNative: String
    val formatedBalanceQuote: String
    val formatedChangePercent: String?
    val changePercentSign: NumberSign?
}

/**
 * Represents a UI model for displaying a token balance in the user interface.
 * @property id The unique identifier of the token.
 * @property name The name of the token.
 * @property assetLogoUrl The URL of the token's logo.
 * @property chainLogoUrl The URL of the blockchain's logo on which the token resides.
 * @property formatedBalanceNative The formatted balance in native currency.
 * @property formatedBalanceQuote The formatted balance in quote currency.
 * @property formatedChangePercent The formatted percentage change of quote balance, if available.
 * @property changePercentSign The sign of the change percentage.
 */
data class UITokenBalanceListItem(
    override val id: String,
    override val name: String,
    override val assetLogoUrl: String?,
    override val chainLogoUrl: String,
    override val formatedBalanceNative: String,
    override val formatedBalanceQuote: String,
    override val formatedChangePercent: String? = null,
    override val changePercentSign: NumberSign? = null,
) : UIAssetBalanceListItem


/**
 * Represents a UI model for displaying a unified token balance in the user interface.
 * @property id The unique identifier of the token.
 * @property name The name of the token.
 * @property assetLogoUrl The URL of the unified token's logo.
 * @property chainLogoUrl The URL of unified logo.
 * @property formatedBalanceNative The formatted balance in native currency.
 * @property formatedBalanceQuote The formatted balance in quote currency.
 * @property formatedChangePercent The formatted percentage change of quote balance, if available.
 * @property changePercentSign The sign of the change percentage.
 * @property tokens A list of individual token balances under this unified token.
 * @property isExpanded Indicates whether this unified item is expanded to show its tokens.
 */
data class UIUnifiedTokenBalanceListItem(
    override val id: String,
    override val name: String,
    override val assetLogoUrl: String?,
    override val chainLogoUrl: String,
    override val formatedBalanceNative: String,
    override val formatedBalanceQuote: String,
    override val formatedChangePercent: String? = null,
    override val changePercentSign: NumberSign? = null,
    val tokens: List<UITokenBalanceListItem>,
    var isExpanded: Boolean = false,
) : UIAssetBalanceListItem


fun List<UIAssetBalanceListItem>.toggleUnified(tokenId: String): List<UIAssetBalanceListItem> =
    map { item ->
        when (item) {
            is UIUnifiedTokenBalanceListItem ->
                if (item.id == tokenId) item.copy(isExpanded = !item.isExpanded)
                else                 item.copy(isExpanded = false)

            else -> item
        }
    }