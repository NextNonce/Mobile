package com.nextnonce.app.home.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nextnonce.app.theme.LocalNextNonceColorsPalette
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    onPortfolioClicked: () -> Unit = {},
    onAddWalletClicked: () -> Unit = {},
    onWalletClicked: (String) -> Unit = {},
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PortfolioSummaryCard(
                    portfolio = dummyPortfolio,
                    onClick = onPortfolioClicked
                )
            }
            item {
                MyWalletsSection(
                    wallets = dummyWallets,
                    onAddWalletClicked = onAddWalletClicked,
                    onWalletClicked = onWalletClicked
                )
            }
        }
    }
}

// --- Reusable UI Components ---
@Composable
fun PortfolioSummaryCard(portfolio: PortfolioData, onClick: () -> Unit) {
    val cardShape = RoundedCornerShape(20.dp)
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(onClick = onClick),
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

            Text(
                text = formatCurrency(portfolio.totalValue),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )

            val percentageText: String
            val textColor: Color

            if (portfolio.changePercent != null) {
                val customColors = LocalNextNonceColorsPalette.current
                textColor = if (portfolio.changePercent < 0) customColors.lossRed else customColors.profitGreen
                val sign = if (portfolio.changePercent > 0) "+" else ""
                percentageText = "$sign${portfolio.changePercent}%"
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
fun MyWalletsSection(
    wallets: List<Wallet>,
    onAddWalletClicked: () -> Unit,
    onWalletClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
            IconButton(onClick = onAddWalletClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Wallet",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        wallets.forEach { wallet ->
            WalletItem(
                wallet = wallet,
                onClick = { onWalletClicked(wallet.id) }
            )
        }
    }
}

@Composable
fun WalletItem(wallet: Wallet, onClick: () -> Unit) {
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
                    text = wallet.address,
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

                if (wallet.totalBalance != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatCurrency(wallet.totalBalance),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (wallet.changePercent != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            val customColors = LocalNextNonceColorsPalette.current
                            val changeColor = if (wallet.changePercent < 0) customColors.lossRed else customColors.profitGreen
                            val sign = if (wallet.changePercent > 0) "+" else ""
                            Text(
                                text = "$sign${wallet.changePercent}%",
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

/**
 * A reusable composable that displays an animated shimmer effect.
 */
@Composable
private fun ShimmerLoadingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.surfaceContainerHighest,
            MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(brush)
    )
}
