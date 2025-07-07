package com.nextnonce.app.portfolio.domain.model

/**
 * Command to create a new portfolio wallet.
 *
 * @property address The address of the wallet.
 * @property name An optional name for the wallet.
 */
data class CreatePortfolioWalletCommand(
    val address: String,
    val name: String?,
)
