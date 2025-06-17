package com.nextnonce.app.core.platform

sealed interface Platform {
    data object Android : Platform
    data object IOS   : Platform
}

expect val currentPlatform: Platform