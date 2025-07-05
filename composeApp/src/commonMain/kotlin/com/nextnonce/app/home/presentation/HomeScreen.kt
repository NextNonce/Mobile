package com.nextnonce.app.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nextnonce.app.core.enums.NumberSign
import com.nextnonce.app.core.presentation.LoadingOverlay
import com.nextnonce.app.core.presentation.ShimmerLoadingIndicator
import com.nextnonce.app.core.utils.formatAddress
import com.nextnonce.app.theme.LocalNextNonceColorsPalette
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    onPortfolioClicked: (String) -> Unit = {},
    onAddWalletClicked: (String) -> Unit = {},
    onWalletClicked: (String, String?) -> Unit = { walletId, walletName -> }
) {
    val homeViewModel = koinViewModel<HomeViewModel>()
    val state by homeViewModel.state.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Use Scaffold's padding
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Space between items
        ) {
            PortfolioSummaryCard(
                state = state,
                onClick = onPortfolioClicked
            )

            MyWalletsHeader(state = state, onAddWalletClicked = onAddWalletClicked)

            if (state.areWalletsLoading) {
                LoadingOverlay()
            } else {
                WalletItems(
                    state = state,
                    onWalletClicked = onWalletClicked
                )
            }
        }
    }
}

// --- Reusable UI Components ---
@Composable
fun PortfolioSummaryCard(state: HomeState, onClick: (String) -> Unit) {
    val cardShape = RoundedCornerShape(20.dp)
    val portfolio = state.portfolio
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(
                onClick = {
                    if (!portfolio.isLoading && portfolio.id != null) {
                        onClick(portfolio.id)
                    }
                }
            ),
        shape = cardShape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Portfolio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View Portfolio Details",
                    modifier = Modifier.size(22.dp).padding(start = 4.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (portfolio.formatedBalanceQuote == null) {
                ShimmerLoadingIndicator(modifier = Modifier.fillMaxWidth(0.25f), height = 40.dp)
            } else {
                Text(
                    text = portfolio.formatedBalanceQuote,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
            }

            val percentageText: String
            val textColor: Color

            if (portfolio.formatedChangePercent != null && portfolio.changePercentSign != null) {
                val customColors = LocalNextNonceColorsPalette.current
                textColor = when (portfolio.changePercentSign) {
                    NumberSign.POSITIVE -> customColors.profitGreen
                    NumberSign.NEGATIVE -> customColors.lossRed
                    NumberSign.ZERO -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                percentageText = portfolio.formatedChangePercent
            } else {
                textColor = MaterialTheme.colorScheme.onSurfaceVariant
                percentageText = "0.00%"
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = percentageText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun MyWalletsHeader(
    state: HomeState,
    onAddWalletClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Wallets",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        IconButton(
            onClick = {
                if (state.portfolio.id != null) {
                    onAddWalletClicked(state.portfolio.id)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Wallet",
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun WalletItems(
    state: HomeState,
    onWalletClicked: (String, String?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = state.wallets.size,
            key = { index -> state.wallets[index].id }
        ) { index ->
            val wallet = state.wallets[index]
            WalletItem(
                walletItem = wallet,
                onClick = {
                    if (!wallet.isLoading) {
                        onWalletClicked(wallet.id, wallet.name)
                    }
                }
            )
        }
    }
}

@Composable
fun WalletItem(walletItem: UIHomeWalletItem, onClick: () -> Unit) {
    val cardShape = RoundedCornerShape(16.dp)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(onClick = onClick),
        shape = cardShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                var addressWidth by remember { mutableStateOf<Dp?>(null) }
                val density = LocalDensity.current

                Text(
                    text = walletItem.name ?: formatAddress(walletItem.address),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (addressWidth == null) {
                            addressWidth = with(density) { coordinates.size.width.toDp() }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (walletItem.formatedBalanceQuote != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = walletItem.formatedBalanceQuote,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (walletItem.formatedChangePercent != null && walletItem.changePercentSign != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            val customColors = LocalNextNonceColorsPalette.current
                            val changeColor = when (walletItem.changePercentSign) {
                                NumberSign.POSITIVE -> customColors.profitGreen
                                NumberSign.NEGATIVE -> customColors.lossRed
                                NumberSign.ZERO -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                            Text(
                                text = walletItem.formatedChangePercent,
                                style = MaterialTheme.typography.bodyMedium,
                                color = changeColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    // Only apply the width modifier if it has been measured
                    val shimmerModifier = addressWidth?.let { Modifier.width(it) } ?: Modifier.fillMaxWidth(0.6f)
                    ShimmerLoadingIndicator(modifier = shimmerModifier)
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View Wallet Details",
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
