package com.nextnonce.app.auth.domain.model

data class AuthUser(
    val email: String?,
    val accessToken: String,
    val refreshToken: String? = null,
)