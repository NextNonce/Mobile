package com.nextnonce.app.balance.presentation

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.nextnonce.app.core.enums.NumberSign
import com.nextnonce.app.theme.LocalNextNonceColorsPalette
import io.kamel.core.config.KamelConfig
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.kamel.image.config.LocalKamelConfig
import org.koin.compose.koinInject

@Composable
fun BalanceScreen(
    totalBalance: @Composable () -> Unit,
    assetBalances: List<UIAssetBalanceListItem>,
    onToggleItem: (String) -> Unit = {}, // The parent is responsible for handling this
    onTokenClick: (String) -> Unit = {},
) {
    val kamelConfig: KamelConfig = koinInject()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (assetBalances.isEmpty()) {
            EmptySection()
            return@Box
        } else {
            CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    overscrollEffect = null,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        totalBalance()
                    }
                    assetBalanceList(
                        assetBalances = assetBalances,
                        onTokenClick = onTokenClick,
                        onToggleItem = onToggleItem
                    )
                }
            }
        }
    }
}

fun LazyListScope.assetBalanceList(
    assetBalances: List<UIAssetBalanceListItem>,
    onTokenClick: (String) -> Unit,
    onToggleItem: (String) -> Unit
) {
    items(
        items = assetBalances,
    ) { assetBalance ->
        when (assetBalance) {
            is UITokenBalanceListItem -> {
                // Single token
                AssetBalanceListItem(
                    assetBalance = assetBalance,
                    onClick = { onTokenClick(assetBalance.id) }
                )
            }
            is UIUnifiedTokenBalanceListItem -> {
                // Expandable Unified Token
                ExpandableUnifiedTokenItem(
                    unifiedToken = assetBalance,
                    onToggle = { onToggleItem(assetBalance.id) }
                )
            }
        }
    }
}

@Composable
private fun ExpandableUnifiedTokenItem(
    unifiedToken: UIUnifiedTokenBalanceListItem,
    onToggle: () -> Unit
) {
    Column {
        // The main clickable row for the unified token
        AssetBalanceListItem(
            assetBalance = unifiedToken,
            onClick = onToggle
        )

        // The animated, nested list of sub-tokens
        AnimatedVisibility(visible = unifiedToken.isExpanded) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                unifiedToken.tokens.forEach { subToken ->
                    AssetBalanceListItem(
                        assetBalance = subToken,
                        isInnerItem = true
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetBalanceListItem(
    assetBalance: UIAssetBalanceListItem,
    onClick: () -> Unit = {},
    isInnerItem: Boolean = false, // Used to determine if this is a sub-token item
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        if (isInnerItem) {
            // Indent the inner item
            Spacer(modifier = Modifier.width(12.dp))
        }
        TokenIconWithChain(
            assetLogoUrl = assetBalance.assetLogoUrl,
            chainLogoUrl = assetBalance.chainLogoUrl
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = assetBalance.name,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = assetBalance.formatedBalanceNative,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                //color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = assetBalance.formatedBalanceQuote,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))

            val formatedChangePercent = assetBalance.formatedChangePercent
            val changePercentSign = assetBalance.changePercentSign
            val percentageText: String
            val textColor: Color
            if (formatedChangePercent != null && changePercentSign != null) {
                val customColors = LocalNextNonceColorsPalette.current
                textColor = when (changePercentSign) {
                    NumberSign.POSITIVE -> customColors.profitGreen
                    NumberSign.NEGATIVE -> customColors.lossRed
                    NumberSign.ZERO -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                percentageText = formatedChangePercent
            } else {
                textColor = MaterialTheme.colorScheme.onSurfaceVariant
                percentageText = "0.00%"
            }

            Text(
                text = percentageText,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun TokenIconWithChain(
    assetLogoUrl: String?,
    chainLogoUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(48.dp)
    ) {
        val density = LocalDensity.current
        val targetPx = remember(density) { with(density) { 48.dp.roundToPx() } }
        if (assetLogoUrl != null) {
            val tokenResource = asyncPainterResource(
                data = assetLogoUrl,
                maxBitmapDecodeSize = IntSize(targetPx, targetPx),
            )
            KamelImage(
                resource = { tokenResource },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                alpha = DefaultAlpha
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 2.dp, y = 2.dp)
                .clip(CircleShape)
                .size(21.4.dp) // Total size of the chain icon including its border
                .background(MaterialTheme.colorScheme.background), // This creates the border
            contentAlignment = Alignment.Center // Center the actual icon inside this box
        ) {
            val chainResource = asyncPainterResource(
                data = chainLogoUrl,
                filterQuality = FilterQuality.High
            )
            KamelImage(
                resource = { chainResource },
                contentDescription = null,
                modifier = Modifier
                    .size(18.4.dp),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun EmptySection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = "There are no tokens",
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}