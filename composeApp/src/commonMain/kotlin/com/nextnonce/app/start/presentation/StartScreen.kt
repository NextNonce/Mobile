package com.nextnonce.app.start.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nextnonce.composeapp.generated.resources.Res
import nextnonce.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StartScreen(
    onUserSignedIn: () -> Unit,
    onUserNotSignedIn: () -> Unit,
) {
    val startViewModel = koinViewModel<StartViewModel>()
    val state by startViewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Text(
            text = stringResource(Res.string.loading),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    } else {
        if (state.isUserSignedIn) {
            onUserSignedIn()
        } else {
            onUserNotSignedIn()
        }
    }
}