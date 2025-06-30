package com.nextnonce.app.start.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(Res.string.loading),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    } else {
        if (state.isUserSignedIn) {
            onUserSignedIn()
        } else {
            onUserNotSignedIn()
        }
    }
}