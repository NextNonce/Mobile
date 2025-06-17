package com.nextnonce.app.auth.presentation

import org.jetbrains.compose.resources.StringResource

data class AuthState(
    val isLoading: Boolean = false,
    val userEmail: String? = null,
    val userPassword: String? = null,
    val error: StringResource? = null,
    val isSignUp: Boolean = false,
    val isSignIn: Boolean = false,
)