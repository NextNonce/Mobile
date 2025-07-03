package com.nextnonce.app.core.utils

/**
 * Formats a given wallet address by shortening it with an ellipsis in the middle.
 *
 * @param address The full wallet address (e.g., "0x12584dc82a7077d8558b7c8374918fc939574528").
 * @param leadingChars The number of characters to show from the beginning of the address.
 * Defaults to 6 (for "0x" plus 4 chars).
 * @param trailingChars The number of characters to show from the end of the address.
 * Defaults to 6.
 * @return The formatted address string (e.g., "0x1258...74528"). Returns the original
 * address if it's shorter than the combined leading/trailing chars + ellipsis.
 */
fun formatAddress(address: String?, leadingChars: Int = 6, trailingChars: Int = 6): String {
    if (address == null || address.isEmpty()) {
        return ""
    }

    // Ensure we don't try to format if the address is too short
    val minLength = leadingChars + trailingChars + 3 // +3 for "..."
    if (address.length <= minLength) {
        return address // Return original if already short enough
    }

    val start = address.substring(0, leadingChars)
    val end = address.substring(address.length - trailingChars, address.length)

    return "$start...$end"
}
