package com.nextnonce.app.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig

object AppLogger : Logger(
    config = StaticConfig(),
    tag = "App",
)

fun appLogger(featureTag: String): Logger =
    AppLogger.withTag("APP/$featureTag")