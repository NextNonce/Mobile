package com.nextnonce.app.core.domain.wallet

import com.nextnonce.app.core.domain.chain.ChainType


/**
 * Represents a wallet with an address and a chain type.
 *
 * @property address The address of the wallet.
 * @property chainType The type of blockchain the wallet is associated with.
 * @property id A unique identifier for the wallet, combining the chain type and address.
 */
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