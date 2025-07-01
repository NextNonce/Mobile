package com.nextnonce.app

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val fullName: String = "$name ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()