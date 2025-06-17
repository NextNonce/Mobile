package com.nextnonce.app.user.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto(
    val email: String? = null,
)