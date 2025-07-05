package com.nextnonce.app.core.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val String.fontSizeByLength: TextUnit
    get() = when (this.length) { // 'this' refers to the String instance
        in 0..7 -> 56.sp
        in 8..11 -> 48.sp
        in 12..14 -> 40.sp
        in 15..17 -> 32.sp
        else -> 24.sp
    }