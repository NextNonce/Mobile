package com.nextnonce.app.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nextnonce.app.core.platform.Platform
import com.nextnonce.app.core.platform.currentPlatform
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import nextnonce.composeapp.generated.resources.Res
import nextnonce.composeapp.generated.resources.email
import nextnonce.composeapp.generated.resources.password
import nextnonce.composeapp.generated.resources.sign_in
import nextnonce.composeapp.generated.resources.sign_in_with_google
import nextnonce.composeapp.generated.resources.sign_up
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onDone: () -> Unit,
    composeAuth: ComposeAuth = koinInject<ComposeAuth>(),
) {
    val viewModel = koinViewModel<AuthViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navigate exactly once when sign‑in/up completes
    LaunchedEffect(state.isSignIn || state.isSignUp) {
        if (state.isSignIn || state.isSignUp) onDone()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(top = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AuthInputField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(Res.string.email)
                )
                AuthInputField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(Res.string.password),
                    isPassword = true
                )
            }

            // Sign Up
            AuthButton(text = stringResource(Res.string.sign_up)) {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signUpWithEmail(email, password)
                }
            }

            // Sign In
            AuthButton(text = stringResource(Res.string.sign_in)) {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInWithEmail(email, password)
                }
            }

            // Continue with Google (Android only)
            if (currentPlatform is Platform.Android) {
                val action = composeAuth.rememberSignInWithGoogle(
                    onResult = { res ->
                        if (res is NativeSignInResult.Success) {
                            viewModel.onSuccessGoogleSignIn()
                        }
                    }
                )
                AuthButton(text = stringResource(Res.string.sign_in_with_google)) {
                    action.startFlow()
                }
            }

            // Error message
            state.error?.let { err ->
                Text(
                    stringResource(err),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun AuthInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    val shape: Shape = RoundedCornerShape(10.dp)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp),
        placeholder = { Text(label, style = MaterialTheme.typography.bodyLarge) },
        shape = shape,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email),
    )
}

@Composable
private fun AuthButton(
    text: String,
    onClick: () -> Unit
) {
    val shape: Shape = RoundedCornerShape(10.dp)
    Button(
        onClick = onClick,
        shape = shape,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(48.dp)
    ) {
        Text(
            text = text,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}