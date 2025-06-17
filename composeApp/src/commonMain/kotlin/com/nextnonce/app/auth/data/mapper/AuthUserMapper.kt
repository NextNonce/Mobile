package com.nextnonce.app.auth.data.mapper

import com.nextnonce.app.auth.domain.model.AuthUserModel
import io.github.jan.supabase.auth.user.UserSession

fun UserSession.toAuthUser(): AuthUserModel {
    return AuthUserModel(
        email = this.user?.email,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}