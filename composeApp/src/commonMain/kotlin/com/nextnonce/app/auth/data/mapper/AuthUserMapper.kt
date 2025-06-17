package com.nextnonce.app.auth.data.mapper

import com.nextnonce.app.auth.domain.model.AuthUser
import io.github.jan.supabase.auth.user.UserSession

fun UserSession.toAuthUser(): AuthUser {
    return AuthUser(
        email = this.user?.email,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}