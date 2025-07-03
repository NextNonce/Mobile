package com.nextnonce.app.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
expect fun HighQualityPlatformImage(
    imageUrl: String,
    size: Dp,
    modifier: Modifier = Modifier
)