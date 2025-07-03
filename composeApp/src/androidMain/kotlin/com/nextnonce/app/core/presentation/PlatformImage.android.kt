package com.nextnonce.app.core.presentation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Precision
import coil3.size.Size

@Composable
actual fun HighQualityPlatformImage(
    imageUrl: String,
    size: Dp,
    modifier: Modifier
) {
    val context = LocalPlatformContext.current
    val density = LocalDensity.current

    val imageRequest = remember(imageUrl, size, density) {
        val targetSize = with(density) { size.roundToPx() }
        ImageRequest.Builder(context)
            .data(imageUrl)
            .size(Size(targetSize, targetSize))
            .precision(Precision.EXACT)
            .build()
    }

    Image(
        painter = rememberAsyncImagePainter(
            model = imageRequest,
            filterQuality = FilterQuality.High
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}
