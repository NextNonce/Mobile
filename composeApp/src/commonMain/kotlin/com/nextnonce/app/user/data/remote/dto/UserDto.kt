package com.nextnonce.app.user.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)