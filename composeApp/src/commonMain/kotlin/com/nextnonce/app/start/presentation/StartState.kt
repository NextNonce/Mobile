package com.nextnonce.app.start.presentation

import org.jetbrains.compose.resources.StringResource

/**
 * Represents the state of the Start screen.
 *
 * @property isLoading Indicates whether the start process is currently loading.
 * @property isUserSignedIn Indicates whether a user is signed in.
 * @property error An optional error message to display if an error occurs during the start process.
 */
data class StartState(
    val isLoading: Boolean = true,
    val isUserSignedIn: Boolean = false,
    val error: StringResource? = null,
)
