package com.nextnonce.app.auth.domain.model

import com.nextnonce.app.user.domain.model.UserModel

fun AuthUserModel.toUserModel(): UserModel =
    UserModel(email = this.email)