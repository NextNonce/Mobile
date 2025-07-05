package com.nextnonce.app.balance.presentation.mapper

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.nextnonce.app.balance.presentation.UIAssetBalanceListItem
import com.nextnonce.app.balance.presentation.UITokenBalanceListItem
import com.nextnonce.app.balance.presentation.UIUnifiedTokenBalanceListItem
import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.token.Token
import com.nextnonce.app.core.domain.token.UnifiedToken
import com.nextnonce.app.core.utils.CHAIN_LOGO_BASE
import com.nextnonce.app.core.utils.formatBalanceNative
import com.nextnonce.app.core.utils.formatPercentage
import com.nextnonce.app.core.utils.formatQuote
import com.nextnonce.app.core.utils.toSign

fun Token.chainLogoUrl(): String =
    "$CHAIN_LOGO_BASE/${chainName.lowercase()}.png"

fun UnifiedToken.chainLogoUrl(): String =
    "$CHAIN_LOGO_BASE/unified.png"

fun AssetBalance.toUIAssetBalanceListItem(): UIAssetBalanceListItem {
    val chainLogoUrl = when (val asset = asset) {
        is Token -> asset.chainLogoUrl()
        is UnifiedToken -> asset.chainLogoUrl()
    }

    val formatedBalanceNative = formatBalanceNative(
        balance.balanceNative,
        asset.tokenMetadata.symbol
    )

    val formatedBalanceQuote = formatQuote(balance.balanceQuote)

    val formatedChangePercent = formatPercentage(
        balance.balanceQuoteChangePercent ?: BigDecimal.ZERO
    )

    val changePercentSign = formatedChangePercent.toSign()


    return when (val asset = asset) {
        is Token -> UITokenBalanceListItem(
            id = asset.id,
            name = asset.tokenMetadata.name,
            assetLogoUrl = asset.tokenMetadata.logoUrl,
            chainLogoUrl = chainLogoUrl,
            formatedBalanceNative = formatedBalanceNative,
            formatedBalanceQuote = formatedBalanceQuote,
            formatedChangePercent = formatedChangePercent,
            changePercentSign = changePercentSign,
        )
        is UnifiedToken -> {
            UIUnifiedTokenBalanceListItem(
            id = asset.id,
            name = asset.tokenMetadata.name,
            assetLogoUrl = asset.tokenMetadata.logoUrl,
            chainLogoUrl = chainLogoUrl,
            formatedBalanceNative = formatedBalanceNative,
            formatedBalanceQuote = formatedBalanceQuote,
            formatedChangePercent = formatedChangePercent,
            changePercentSign = changePercentSign,
            tokens = buildList { // Use buildList block
                for (index in asset.tokens.indices) {
                    val individualTokenAssetBalance = AssetBalance(
                        asset = asset.tokens[index],
                        balance = asset.balances[index]
                    )
                    // Add to the list being built by buildList
                    add(individualTokenAssetBalance.toUIAssetBalanceListItem() as UITokenBalanceListItem)
                }
            },
            isExpanded = false
        ) }
    }
}