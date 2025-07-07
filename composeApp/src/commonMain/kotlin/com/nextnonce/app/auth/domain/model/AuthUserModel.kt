package com.nextnonce.app.auth.domain.model

/**
 * Represents an authenticated user in the application.
 *
 * @property email The email address of the user, or null if not available.
 * @property accessToken The access token for the user session.
 * @property refreshToken The refresh token for the user session, or null if not available.
 */
data class AuthUserModel(
    val email: String?,
    val accessToken: String,
    val refreshToken: String? = null,
)