package com.nextnonce.app.logging

import co.touchlab.kermit.Logger
import io.ktor.client.plugins.logging.Logger as KtorLogger

private val ktorKermit = Logger.withTag("Ktor")

object KermitLoggerAdapter : KtorLogger {
    override fun log(message: String) {
        ktorKermit.d { message }
    }
}