package com.nextnonce.app

import android.app.Application
import com.nextnonce.app.di.initKoin
import org.koin.core.component.KoinComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class NextNonceApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@NextNonceApplication)
        }
    }
}