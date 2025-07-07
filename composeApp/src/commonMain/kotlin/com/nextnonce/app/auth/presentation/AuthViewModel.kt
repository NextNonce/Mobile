package com.nextnonce.app.auth.presentation

import com.nextnonce.app.core.domain.Result
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextnonce.app.auth.domain.SignInWithEmailUseCase
import com.nextnonce.app.auth.domain.SignInWithGoogleUseCase
import com.nextnonce.app.auth.domain.SignUpWithEmailUseCase
import com.nextnonce.app.core.utils.toUIText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for handling authentication operations such as sign up and sign in.
 */
class AuthViewModel(
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AuthState()
        )

    fun signUpWithEmail(email: String, password: String) = launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = signUpWithEmailUseCase.execute(email, password)) {
            is Result.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSignUp = true
                )
            }
            is Result.Error -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.error.toUIText()
                )
            }
        }
    }

    fun signInWithEmail(email: String, password: String) = launch {
        _state.value = _state.value.copy(isLoading = true)
        when (val result = signInWithEmailUseCase.execute(email, password)) {
            is Result.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSignIn = true
                )
            }
            is Result.Error -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.error.toUIText()
                )
            }
        }
    }

    fun onSuccessGoogleSignIn() = launch {
        when (val result = signInWithGoogleUseCase.execute()) {
            is Result.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSignIn = true
                )
            }
            is Result.Error -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.error.toUIText()
                )
            }
        }
    }

    private fun launch(block: suspend () -> Unit) =
        viewModelScope.launch { block() }
}