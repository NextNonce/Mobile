package com.nextnonce.app.user.data.mapper

import com.nextnonce.app.user.data.remote.dto.CreateUserDto
import com.nextnonce.app.user.data.remote.dto.UserDto
import com.nextnonce.app.user.domain.model.UserModel

fun UserDto.toUserModel() = UserModel(
    email = email
)

fun UserModel.toCreateUserDto() = CreateUserDto(
    email = email
)