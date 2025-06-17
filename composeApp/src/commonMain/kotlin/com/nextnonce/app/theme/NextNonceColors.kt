package com.nextnonce.app.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color


// basic red #e44c2d
// background red #342421
// basic green #2fa56b
// background green #243421

// light basic green #4AA34F
// ...

// for now they are the same as dark
val ProfitGreenColor = Color(color = 0xFF2FA56B)
val ProfitGreenBackgroundColor = Color(color = 0xFF243421)
val LossRedColor = Color(color = 0xFFE44C2D)
val LossRedBackgroundColor = Color(color = 0xFF342421)

val DarkProfitGreenColor = Color(color = 0xFF2FA56B)
val DarkProfitGreenBackgroundColor = Color(color = 0xFF243421)
val DarkLossRedColor = Color(color = 0xFFE44C2D)
val DarkLossRedBackgroundColor = Color(color = 0xFF342421)

@Immutable
class NextNonceColorsPalette(
    val profitGreen: Color = Color.Unspecified,
    val profitGreenBackground: Color = Color.Unspecified,
    val lossRed: Color = Color.Unspecified,
    val lossRedBackground: Color = Color.Unspecified,
)

val LightCustomColorsPalette = NextNonceColorsPalette(
    profitGreen = ProfitGreenColor,
    profitGreenBackground = ProfitGreenBackgroundColor,
    lossRed = LossRedColor,
    lossRedBackground = LossRedBackgroundColor,
)

val DarkCustomColorsPalette = NextNonceColorsPalette(
    profitGreen = DarkProfitGreenColor,
    profitGreenBackground = DarkProfitGreenBackgroundColor,
    lossRed = DarkLossRedColor,
    lossRedBackground = DarkLossRedBackgroundColor,
)

val LocalNextNonceColorsPalette = compositionLocalOf {
    NextNonceColorsPalette()
}