package com.nextnonce.app

interface Platform {
    val name: String
    val fullName: String
}

expect fun getPlatform(): Platform