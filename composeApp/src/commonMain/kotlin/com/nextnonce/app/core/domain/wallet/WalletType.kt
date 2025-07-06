package com.nextnonce.app.core.domain.wallet

/**
 * Represents the type of wallet.
 *
 * @property SIMPLE A simple wallet type.
 * @property SMART A smart wallet type.
 */
enum class WalletType {
    SIMPLE,
    SMART
}

fun WalletType.toStringValue(): String {
    return when (this) {
        WalletType.SIMPLE -> "Simple"
        WalletType.SMART -> "Smart"
    }
}