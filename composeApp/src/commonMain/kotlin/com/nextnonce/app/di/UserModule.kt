package com.nextnonce.app.di

import com.nextnonce.app.user.data.UserRepositoryImpl
import com.nextnonce.app.user.data.remote.RemoteUserDataSource
import com.nextnonce.app.user.data.remote.impl.RemoteUserDataSourceImpl
import com.nextnonce.app.user.domain.CreateUserUseCase
import com.nextnonce.app.user.domain.GetUserUseCase
import com.nextnonce.app.user.domain.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * Koin module for user-related dependencies.
 * This module provides the repository, use cases, and data sources for managing user data.
 */
val userModule = module {
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::CreateUserUseCase)
    singleOf(::GetUserUseCase)
    singleOf(::RemoteUserDataSourceImpl).bind<RemoteUserDataSource>()
}