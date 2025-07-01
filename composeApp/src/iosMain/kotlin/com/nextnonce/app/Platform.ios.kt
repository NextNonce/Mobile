package com.nextnonce.app

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = "iOS"
    override val fullName: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()