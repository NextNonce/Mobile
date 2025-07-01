package com.nextnonce.app.portfolio.data.mapper

import com.nextnonce.app.core.domain.wallet.Wallet
import com.nextnonce.app.portfolio.data.remote.dto.CreatePortfolioWalletDto
import com.nextnonce.app.portfolio.data.remote.dto.PortfolioWalletDto
import com.nextnonce.app.portfolio.domain.model.CreatePortfolioWalletCommand
import com.nextnonce.app.portfolio.domain.model.PortfolioWalletModel

fun CreatePortfolioWalletCommand.toCreatePortfolioWalletDto() = CreatePortfolioWalletDto(
    address = address,
    name = name
)

fun PortfolioWalletDto.toPortfolioWalletModel() = PortfolioWalletModel(
    wallet = Wallet(
        address = address,
        chainType = chainType,
    ),
    name = name,
    walletType = walletType,
    createdAt = createdAt,
    updatedAt = updatedAt
)