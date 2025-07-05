package com.nextnonce.app.balance.presentation

import com.nextnonce.app.core.enums.NumberSign

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