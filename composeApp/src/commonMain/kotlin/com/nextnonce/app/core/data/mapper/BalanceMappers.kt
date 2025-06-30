package com.nextnonce.app.core.data.mapper

import com.nextnonce.app.core.data.remote.dto.balance.AssetBalanceDto
import com.nextnonce.app.core.data.remote.dto.balance.BalanceDto
import com.nextnonce.app.core.data.remote.dto.balance.TotalBalanceDto
import com.nextnonce.app.core.domain.balance.AssetBalance
import com.nextnonce.app.core.domain.balance.Balance
import com.nextnonce.app.core.domain.balance.TotalBalance

fun AssetBalanceDto.toAssetBalance() = AssetBalance(
    asset = asset.toAsset(),
    balance = balance.toBalance()
)

fun BalanceDto.toBalance() = Balance(
    balanceNative = balanceNative,
    balanceQuote = balanceQuote,
    balanceQuoteChange = balanceQuoteChange,
    balanceQuoteChangePercent = balanceQuoteChangePercent
)

fun TotalBalanceDto.toTotalBalance() = TotalBalance(
    balanceQuote = balanceQuote,
    balanceQuoteChange = balanceQuoteChange,
    balanceQuoteChangePercent = balanceQuoteChangePercent
)