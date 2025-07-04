package com.nextnonce.app.core.domain.wallet

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