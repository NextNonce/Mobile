package com.nextnonce.app.core.auth

import com.nextnonce.app.BuildKonfig
import com.nextnonce.app.getPlatform
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient

object AuthProvider {
    lateinit var client: SupabaseClient
        private set

    fun init() {
        client = createSupabaseClient(
            BuildKonfig.SUPABASE_URL,
            BuildKonfig.SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(ComposeAuth) {
                if (getPlatform().name == "Android") {
                    googleNativeLogin(BuildKonfig.GOOGLE_WEB_CLIENT_ID)
                }
            }
        }
    }
}