package com.nextnonce.app.wallet.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nextnonce.app.balance.presentation.BalanceScreen
import com.nextnonce.app.core.enums.NumberSign
import com.nextnonce.app.core.presentation.LoadingOverlay
import com.nextnonce.app.core.utils.fontSizeByLength
import com.nextnonce.app.core.utils.formatAddress
import com.nextnonce.app.theme.LocalNextNonceColorsPalette
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WalletScreen(
    id: String,
    name: String? = null,
    onBackClicked: () -> Unit,
) {
    val walletViewModel = koinViewModel<WalletViewModel>{ parametersOf(id, name) }
    val state by walletViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            val walletInfo = state.uiWalletInfo
            WalletTopBar(
                address = walletInfo?.address,
                name = walletInfo?.name,
                walletType = walletInfo?.walletType,
                onBackClicked = onBackClicked
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.error != null){
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text      = stringResource(state.error!!),
                        color     = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (!state.isLoading) {
                BalanceScreen(
                    totalBalance = {
                        TotalWalletBalanceItem(state)
                    },
                    assetBalances = state.assetBalances,
                    onToggleItem = {
                        walletViewModel.expandUnifiedToken(it)
                    }
                )
            } else {
                LoadingOverlay()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletTopBar(
    address: String? = null,
    name: String? = null,
    walletType: String? = null,
    onBackClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    if (name != null) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    if (address != null) {
                        Text(
                            text = formatAddress(address, 8, 6),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f)) // Pushes the wallet type to the end
                if (walletType != null) {
                    Text(
                        text = walletType,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
        },
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
fun TotalWalletBalanceItem(
    state: WalletState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        val totalBalance = state.uiWalletTotalBalance
        val totalBalanceText = totalBalance.formatedBalanceQuote ?: ""

        val totalBalanceChangeText =
            (totalBalance.formattedBalanceQuoteChange ?: "") +
                    (if (totalBalance.formatedChangePercent == null)
                        ""
                    else {
                        " (${totalBalance.formatedChangePercent})"
                    })
        Text(
            text = totalBalanceText,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = totalBalanceText.fontSizeByLength,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 2.dp)
                .align(Alignment.CenterHorizontally)
        )
        val customColors = LocalNextNonceColorsPalette.current
        Text(
            text = totalBalanceChangeText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = when (totalBalance.changePercentSign) {
                NumberSign.POSITIVE -> customColors.profitGreen
                NumberSign.NEGATIVE -> customColors.lossRed
                NumberSign.ZERO -> MaterialTheme.colorScheme.onSurfaceVariant
                null -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier
                .padding(top = 2.dp, bottom = 20.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}