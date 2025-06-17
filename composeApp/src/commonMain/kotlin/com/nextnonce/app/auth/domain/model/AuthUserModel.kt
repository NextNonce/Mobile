package com.nextnonce.app.auth.domain.model

data class AuthUserModel(
    val email: String?,
    val accessToken: String,
    val refreshToken: String? = null,
)