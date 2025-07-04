package com.nextnonce.app.core.domain.wallet

import com.nextnonce.app.core.domain.chain.ChainType

data class Wallet(
    val address: String,
    val chainType: ChainType,
) {
    val id get() = "$chainType:$address"
}

fun String.toWallet(): Wallet? {
    val parts = this.split(":")
    return if (parts.size == 2) {
        val chainType = ChainType.valueOf(parts[0])
        val address = parts[1]
        Wallet(address, chainType)
    } else {
        null
    }
}