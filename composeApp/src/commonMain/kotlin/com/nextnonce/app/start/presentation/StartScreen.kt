package com.nextnonce.app.start.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nextnonce.app.core.presentation.LoadingOverlay
import org.koin.compose.viewmodel.koinViewModel

/**
 * StartScreen composable function that checks if the user is signed in and navigates accordingly.
 *
 * @param onUserSignedIn Callback function to execute when the user is signed in.
 * @param onUserNotSignedIn Callback function to execute when the user is not signed in.
 */
@Composable
fun StartScreen(
    onUserSignedIn: () -> Unit,
    onUserNotSignedIn: () -> Unit,
) {
    val startViewModel = koinViewModel<StartViewModel>()
    val state by startViewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingOverlay()
    } else {
        if (state.isUserSignedIn) {
            onUserSignedIn()
        } else {
            onUserNotSignedIn()
        }
    }
}