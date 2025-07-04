package com.nextnonce.app.wallet.data.mapper

import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.wallet.data.remote.dto.WalletDto
import com.nextnonce.app.wallet.domain.model.WalletModel

fun WalletDto.toWalletModel() = WalletModel(
    wallet = Wallet(
        address = address,
        chainType = chainType,
    ),
    walletType = walletType,
)