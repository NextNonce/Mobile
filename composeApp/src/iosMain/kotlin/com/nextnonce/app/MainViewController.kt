package com.nextnonce.app

import androidx.compose.ui.window.ComposeUIViewController
import com.nextnonce.app.di.initKoin

fun MainViewController() = ComposeUIViewController (

    configure = {
        initKoin()
    }
) { App() }