package com.nextnonce.app.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig

/**
 * AppLogger is a singleton instance of Logger configured for the application.
 * It uses a static configuration and is tagged with "App".
 *
 * Use [appLogger] function to create a logger with a specific feature tag.
 */
object AppLogger : Logger(
    config = StaticConfig(),
    tag = "App",
)

fun appLogger(featureTag: String): Logger =
    AppLogger.withTag("APP/$featureTag")