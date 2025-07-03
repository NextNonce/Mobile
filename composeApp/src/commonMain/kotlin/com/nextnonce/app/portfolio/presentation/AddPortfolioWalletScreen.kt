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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


// --- AddWalletScreen Composable ---
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
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

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
            text = "Enter a wallet address",
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
            placeholder = { Text("Name (Optional)") },
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
                    text = "Continue",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

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
        placeholder = { Text("Address", style = MaterialTheme.typography.bodyLarge) },
        shape = RoundedCornerShape(12.dp),
        isError = isError,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}
