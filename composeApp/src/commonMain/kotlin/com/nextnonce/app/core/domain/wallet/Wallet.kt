package com.nextnonce.app.core.domain.wallet

import com.nextnonce.app.core.domain.chain.ChainType

data class Wallet(
    val address: String,
    val chainType: ChainType,
) {
    val id get() = "$chainType-$address"
}