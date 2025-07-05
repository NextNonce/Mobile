package com.nextnonce.app.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Start

@Serializable
object Auth

@Serializable
object Home

@Serializable
data class AddWallet(val portfolioId: String)

@Serializable
data class Portfolio(val portfolioId: String)

@Serializable
data class Wallet(val walletId: String, val walletName: String? = null)