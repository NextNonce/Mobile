package com.nextnonce.app.start.presentation

import org.jetbrains.compose.resources.StringResource

data class StartState(
    val isLoading: Boolean = true,
    val isUserSignedIn: Boolean = false,
    val error: StringResource? = null,
)
