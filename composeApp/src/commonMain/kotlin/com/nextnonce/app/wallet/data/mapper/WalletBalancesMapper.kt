package com.nextnonce.app.wallet.data.mapper

import com.nextnonce.app.core.data.mapper.toAssetBalance
import com.nextnonce.app.core.data.mapper.toTotalBalance
import com.nextnonce.app.wallet.data.remote.dto.WalletBalancesDto
import com.nextnonce.app.wallet.domain.model.WalletBalancesModel

fun WalletBalancesDto.toWalletBalancesModel() = WalletBalancesModel(
    totalBalance = totalBalance.toTotalBalance(),
    assetBalances = assetBalances.map { it.toAssetBalance() }
)