package com.nextnonce.app.auth.presentation

import org.jetbrains.compose.resources.StringResource

/**
 * Represents the state of the authentication screen.
 *
 * @property isLoading Indicates whether the authentication process is currently loading.
 * @property userEmail The email of the user attempting to authenticate.
 * @property userPassword The password of the user attempting to authenticate.
 * @property error An optional error message to display if authentication fails.
 * @property isSignUp Indicates whether the sign-up was successful.
 * @property isSignIn Indicates whether the sign-in was successful.
 */
data class AuthState(
    val isLoading: Boolean = false,
    val userEmail: String? = null,
    val userPassword: String? = null,
    val error: StringResource? = null,
    val isSignUp: Boolean = false,
    val isSignIn: Boolean = false,
)