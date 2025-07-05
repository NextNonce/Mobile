package com.nextnonce.app.balance.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowConversionToBitmap
import coil3.request.maxBitmapSize
import coil3.size.Precision
import coil3.size.Size
import com.nextnonce.app.core.enums.NumberSign
import com.nextnonce.app.core.presentation.HighQualityPlatformImage
import com.nextnonce.app.theme.LocalNextNonceColorsPalette

@Composable
fun BalanceScreen(
    totalBalance: @Composable () -> Unit,
    assetBalances: List<UIAssetBalanceListItem>,
) {
    val kamelConfig: KamelConfig = koinInject()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (assetBalances.isEmpty()) {
            EmptySection()
            return@Box
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    count = assetBalances.size,
                    key = { index -> assetBalances[index].id }
                ) { index ->
                    val assetBalance = assetBalances[index]
                    AssetBalanceListItem(
                        assetBalance = assetBalance
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetBalanceListItem(
    assetBalance: UIAssetBalanceListItem,
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
//            .combinedClickable(
//                onClick = { onTokenClick(token.id) },
//                onLongClick = { onTokenLongPressed(token.id) }
//            )
            .padding(6.dp)
    ) {
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