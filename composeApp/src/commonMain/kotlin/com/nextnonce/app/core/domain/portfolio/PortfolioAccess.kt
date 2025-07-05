package com.nextnonce.app.core.domain.portfolio

enum class PortfolioAccess {
    PRIVATE,
    PUBLIC,
    UNLISTED,
}

fun PortfolioAccess.toStringValue(): String {
    return when (this) {
        PortfolioAccess.PRIVATE -> "Private"
        PortfolioAccess.PUBLIC -> "Public"
        PortfolioAccess.UNLISTED -> "Unlisted"
    }
}