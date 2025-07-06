package com.nextnonce.app.di

import com.nextnonce.app.start.presentation.StartViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * Koin module for Start-related dependencies.
 */
val startModule = module {
    viewModel { StartViewModel(get()) }
}