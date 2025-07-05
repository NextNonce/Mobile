package com.nextnonce.app.core.network

import com.nextnonce.app.core.utils.KAMEL_CACHE_SIZE_MULTIPLIER
import io.kamel.core.config.DefaultCacheSize
import io.kamel.core.config.DefaultHttpCacheSize
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpUrlFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default

val nnKamelConfig = KamelConfig {
    takeFrom(KamelConfig.Default)
    imageBitmapCacheSize = KAMEL_CACHE_SIZE_MULTIPLIER * DefaultCacheSize

    httpUrlFetcher {
        httpCache(
            size = KAMEL_CACHE_SIZE_MULTIPLIER * DefaultHttpCacheSize
        )
    }
}