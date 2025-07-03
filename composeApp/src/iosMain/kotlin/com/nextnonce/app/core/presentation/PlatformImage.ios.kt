package com.nextnonce.app.core.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCache
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue

// A simple in-memory cache to store downloaded images.
private val imageCache = NSCache()

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun HighQualityPlatformImage(
    imageUrl: String,
    size: Dp,
    modifier: Modifier
) {
    // 1) Keep one NSURL instance per URL
    val nsUrl = remember(imageUrl) {
        NSURL.URLWithString(imageUrl)
    }

    UIKitView(
        factory = {
            UIImageView().apply {
                // aspect-fit to keep circle shape correct
                contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
            }
        },
        update = { imageView: UIImageView ->
            nsUrl?.let { url ->
                val key = url.absoluteString ?: return@let

                // 2) Check the cache on main thread
                @Suppress("UNCHECKED_CAST")
                val cached = imageCache.objectForKey(key) as? UIImage
                if (cached != null) {
                    imageView.image = cached
                } else {
                    // 3) Download on background queue
                    dispatch_async(
                        dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)
                    ) {
                        val data = NSData.dataWithContentsOfURL(url) ?: return@dispatch_async
                        val ui = UIImage.imageWithData(data) ?: return@dispatch_async

                        // 4) Back to main thread: update cache + UI
                        dispatch_async(dispatch_get_main_queue()) {
                            imageCache.setObject(ui, forKey = key)
                            imageView.image = ui
                        }
                    }
                }
            }
        },
        // 5) Apply your Compose modifier (including size)
        modifier = modifier.size(size)
    )
}