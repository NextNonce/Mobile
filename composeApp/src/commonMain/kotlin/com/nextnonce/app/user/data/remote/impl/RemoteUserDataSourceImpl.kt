package com.nextnonce.app.user.data.remote.impl

import com.nextnonce.app.core.domain.Result
import com.nextnonce.app.core.domain.DataError
import com.nextnonce.app.core.network.backendHttpClient
import com.nextnonce.app.core.network.safeCall
import com.nextnonce.app.user.data.remote.RemoteUserDataSource
import com.nextnonce.app.user.data.remote.dto.CreateUserDto
import com.nextnonce.app.user.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemoteUserDataSourceImpl : RemoteUserDataSource, KoinComponent {

    private val backend: HttpClient by inject(backendHttpClient)

    override suspend fun get(): Result<UserDto, DataError.Remote> {
        return safeCall {
            backend.get("users/me")
        }
    }

    override suspend fun create(createUserDto: CreateUserDto): Result<UserDto, DataError.Remote> {
        return safeCall {
            backend.post("users") {
                setBody(createUserDto)
            }
        }
    }
}