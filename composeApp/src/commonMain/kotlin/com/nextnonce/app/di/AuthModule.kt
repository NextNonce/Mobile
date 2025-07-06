package com.nextnonce.app.di

import com.nextnonce.app.auth.data.AuthRepositoryImpl
import com.nextnonce.app.auth.domain.AuthRepository
import com.nextnonce.app.auth.domain.GetAuthUserUseCase
import com.nextnonce.app.auth.domain.GetBearerTokensUseCase
import com.nextnonce.app.auth.domain.IsNewUserUseCase
import com.nextnonce.app.auth.domain.IsUserSignedInUseCase
import com.nextnonce.app.auth.domain.RefreshBearerTokensUseCase
import com.nextnonce.app.auth.domain.SignInWithEmailUseCase
import com.nextnonce.app.auth.domain.SignInWithGoogleUseCase
import com.nextnonce.app.auth.domain.SignOutUserUseCase
import com.nextnonce.app.auth.domain.SignUpWithEmailUseCase
import com.nextnonce.app.auth.presentation.AuthViewModel
import com.nextnonce.app.core.auth.AuthProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.composeAuth
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * Koin module for authentication-related dependencies.
 */
val authModule = module {
    single<SupabaseClient> {
        AuthProvider.apply { init() }.client
    }
    single<Auth> { get<SupabaseClient>().auth }
    single<ComposeAuth> { get<SupabaseClient>().composeAuth }

    singleOf(::IsNewUserUseCase)
    singleOf(::IsUserSignedInUseCase)
    singleOf(::SignOutUserUseCase)
    singleOf(::GetAuthUserUseCase)
    singleOf(::SignUpWithEmailUseCase)
    singleOf(::SignInWithEmailUseCase)
    singleOf(::SignInWithGoogleUseCase)
    singleOf(::GetBearerTokensUseCase)
    singleOf(::RefreshBearerTokensUseCase)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    viewModel { AuthViewModel(get(), get(), get()) }
}