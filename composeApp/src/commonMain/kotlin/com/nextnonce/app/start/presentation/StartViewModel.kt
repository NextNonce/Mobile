package com.nextnonce.app.start.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextnonce.app.auth.domain.IsUserSignedInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for managing the start screen state, checking if the user is signed in.
 *
 * @property isUserSignedInUseCase Use case for checking if the user is signed in.
 */
class StartViewModel (
    private val isUserSignedInUseCase: IsUserSignedInUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(StartState())
    val state = _state
        .onStart {
            checkUserSignedIn()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = StartState()
        )

    private suspend fun checkUserSignedIn() {
        val isSignedIn = isUserSignedInUseCase.execute()
        if (isSignedIn) {
            _state.value = _state.value.copy(isUserSignedIn = true, isLoading = false)
        } else {
            _state.value = _state.value.copy(isUserSignedIn = false, isLoading = false)
        }
    }
}