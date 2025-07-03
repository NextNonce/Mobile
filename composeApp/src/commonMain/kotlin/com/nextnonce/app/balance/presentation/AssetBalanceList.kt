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
fun AssetBalanceList(
    assetBalances: List<UIAssetBalanceListItem>,
) {
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
    /*HighQualityAsyncImage(
        imageUrl = assetLogoUrl!!,
        //size = 48.dp,
        size = 48.dp,
        modifier = Modifier
            //.fillMax()
            .clip(CircleShape)
    )*/
    Box(
        modifier = modifier.size(48.dp)
    ) {
        // Main Token Icon (or placeholder)
        if (assetLogoUrl != null) {
            HighQualityPlatformImage(
                imageUrl = assetLogoUrl,
                size = 48.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd) // Position in the bottom-right corner
                .offset(x = 2.dp, y = 2.dp)
                .clip(CircleShape)
                .size(21.4.dp) // Total size of the chain icon including its border
                .background(MaterialTheme.colorScheme.background), // This creates the border
            contentAlignment = Alignment.Center // Center the actual icon inside this box
        ) {
            HighQualityPlatformImage(
                imageUrl = chainLogoUrl,
                //size = 18.dp,
                size = 72.dp,
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
            )

        }
    }
}

/*@Composable
private fun HighQualityAsyncImage(
    imageUrl: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val context = LocalPlatformContext.current
    val density = LocalDensity.current

    val imageRequest = remember(imageUrl, size, density) {
        // Calculate the exact pixel size needed for the current screen's density
        val targetSize = with(density) { size.roundToPx() }
        ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size(targetSize, targetSize)) // Request the image at the exact pixel size
            .precision(Precision.EXACT) // Instructs Coil to respect the exact size.
            .build()
    }

    /*AsyncImage(
        model = imageRequest,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier,
        filterQuality = FilterQuality.High // Use the best scaling algorithm
    )*/
    /*val painter = rememberAsyncImagePainter(model = imageRequest)

    // Draw the image on a Canvas only when it's successfully loaded
    if (painter.state is AsyncImagePainter.State.Success) {
        val imageBitmap = (painter.state as AsyncImagePainter.State.Success).result.image.asBitmap()
        Canvas(modifier = modifier) {
            drawImage(
                image = imageBitmap,
                // Ensure the drawn image size matches the canvas size
                dstSize = IntSize(this.size.width.toInt(), this.size.height.toInt()),
                // Use the highest quality filtering algorithm for downscaling
                filterQuality = FilterQuality.High
            )
        }
    } else {
        // Optional: Show a placeholder while loading or on error
        Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer))
    }*/
    Image(
        painter = rememberAsyncImagePainter(
            model = imageRequest,
            filterQuality = FilterQuality.None
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.None
    )
}
*/



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