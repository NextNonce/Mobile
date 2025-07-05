package com.nextnonce.app.di

import com.nextnonce.app.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val homeModule = module {
    viewModel { HomeViewModel(get(), get(),get(),get()) }
}