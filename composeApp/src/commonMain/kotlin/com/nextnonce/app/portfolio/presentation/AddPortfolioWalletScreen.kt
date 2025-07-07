package com.nextnonce.app.portfolio.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nextnonce.composeapp.generated.resources.Res
import nextnonce.composeapp.generated.resources.address
import nextnonce.composeapp.generated.resources.back
import nextnonce.composeapp.generated.resources.continue_text
import nextnonce.composeapp.generated.resources.enter_wallet_address
import nextnonce.composeapp.generated.resources.wallet_name
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel



/**
 * Composable function to display the Add Portfolio Wallet screen.
 * Allows users to add a wallet to a specific portfolio.
 *
 * @param portfolioId The unique identifier of the portfolio to which the wallet will be added.
 * @param onBackClicked Callback function to handle back navigation.
 * @param onWalletAdded Callback function to handle actions after a wallet is successfully added.
 */
@Composable
fun AddPortfolioWalletScreen(
    portfolioId: String,
    onBackClicked: () -> Unit,
    onWalletAdded: () -> Unit
) {
    val viewModel = koinViewModel<AddPortfolioWalletViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // This effect triggers navigation exactly once when a wallet is successfully added.
    LaunchedEffect(state.isWalletAdded) {
        if (state.isWalletAdded) {
            onWalletAdded()
        }
    }

    Scaffold(
        topBar = {
            AddWalletTopBar(
                onBackClicked = onBackClicked,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            AddWalletContent(
                walletAddress = state.walletAddress,
                walletName = state.walletName,
                onAddressChange = viewModel::onAddressChange,
                onNameChange = viewModel::onNameChange,
                error = state.error,
                onAddressFocusLost = viewModel::onAddressFocusLost
            )

            AddWalletContinueButton(
                isLoading = state.isLoading,
                isEnabled = state.walletAddress.text.isNotEmpty() && !state.isLoading,
                onClick = {
                    viewModel.addPortfolioWallet(
                        portfolioId = portfolioId,
                    )
                }
            )
        }
    }
}

// --- Private UI Components for AddWalletScreen ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddWalletTopBar(
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { /* No title */ },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back),
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

/**
 * Composable function to display the content of the Add Portfolio Wallet screen.
 * Contains input fields for wallet address and name, and displays any error messages.
 *
 * @param walletAddress The current value of the wallet address input field.
 * @param walletName The name of the wallet being added, if provided.
 * @param onAddressChange Callback to handle changes in the wallet address input field.
 * @param onNameChange Callback to handle changes in the wallet name input field.
 * @param error An optional error message to display if an error occurs while adding the wallet.
 * @param onAddressFocusLost Callback to reset the address selection when focus is lost.
 */
@Composable
private fun AddWalletContent(
    walletAddress: TextFieldValue,
    walletName: String?,
    onAddressChange: (TextFieldValue) -> Unit,
    onNameChange: (String) -> Unit,
    error: StringResource?,
    onAddressFocusLost: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.enter_wallet_address),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp)) // Adjusted spacer

        // Address Input Field
        AddressInputField(
            value = walletAddress,
            onValueChange = onAddressChange,
            isError = error != null,
            onAddressFocusLost
        )

        // Wallet Name Input Field
        OutlinedTextField(
            value = walletName ?: "",
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { Text(stringResource(Res.string.wallet_name)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
        )

        if (error != null) {
            Text(
                text = stringResource(error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * Composable function to display the continue button at the bottom of the Add Portfolio Wallet screen.
 * The button is enabled only when the wallet address is not empty and not loading.
 *
 * @param isLoading Indicates whether the wallet addition process is currently loading.
 * @param isEnabled Indicates whether the continue button should be enabled.
 * @param onClick Callback function to handle the click event of the continue button.
 */
@Composable
private fun BoxScope.AddWalletContinueButton(
    isLoading: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .imePadding()
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = isEnabled,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(Res.string.continue_text),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Composable function to display the address input field for adding a wallet.
 * It includes a placeholder, handles focus changes, and displays an error state if applicable.
 *
 * @param value The current value of the address input field.
 * @param onValueChange Callback to handle changes in the address input field.
 * @param isError Indicates whether the input field is in an error state.
 * @param onFocusLost Callback to reset the address selection when focus is lost.
 */
@Composable
private fun AddressInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isError: Boolean,
    onFocusLost: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            // When the field loses focus, move the cursor to the beginning.
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    onFocusLost()
                }
            },
        placeholder = { Text(stringResource(Res.string.address), style = MaterialTheme.typography.bodyLarge) },
        shape = RoundedCornerShape(12.dp),
        isError = isError,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}
