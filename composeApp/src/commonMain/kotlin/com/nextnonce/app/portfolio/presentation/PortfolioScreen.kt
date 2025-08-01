package com.nextnonce.app.portfolio.presentation

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
import com.nextnonce.app.theme.LocalNextNonceColorsPalette
import nextnonce.composeapp.generated.resources.Res
import nextnonce.composeapp.generated.resources.back
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


/**
 * Composable function to display the Portfolio screen.
 * It shows the portfolio's total balance, asset balances, and allows navigation back.
 *
 * @param id The unique identifier of the portfolio.
 * @param onBackClicked Callback function to handle back navigation.
 */
@Composable
fun PortfolioScreen(
    id: String,
    onBackClicked: () -> Unit = {},
) {
    val portfolioViewModel = koinViewModel<PortfolioViewModel> { parametersOf(id) }
    val state by portfolioViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            val portfolioInfo = state.uiPortfolioInfo
            PortfolioTopBar(
                name = portfolioInfo?.name,
                access = portfolioInfo?.access,
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
                        TotalPortfolioBalanceItem(state)
                    },
                    assetBalances = state.assetBalances,
                    onToggleItem = {
                        portfolioViewModel.expandUnifiedToken(it)
                    }
                )
            } else {
                LoadingOverlay()
            }
        }
    }
}

/**
 * Composable function to display the top bar for the Portfolio screen.
 * It includes the portfolio name, access type, and a back button.
 *
 * @param name The name of the portfolio (nullable).
 * @param access The access type of the portfolio (nullable).
 * @param onBackClicked Callback function to handle back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortfolioTopBar(
    name: String? = null,
    access: String? = null,
    onBackClicked: () -> Unit = {},
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
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f)) // Pushes the wallet type to the end
                if (access != null) {
                    Text(
                        text = access,
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
 * Composable function to display the total portfolio balance item.*
 * @param state The current state of the portfolio, containing the total balance and its change.
 */
@Composable
fun TotalPortfolioBalanceItem(
    state: PortfolioState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        val totalBalance = state.uiPortfolioTotalBalance
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