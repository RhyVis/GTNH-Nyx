@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.util

/**
 * Makes the first character of the string uppercase.
 */
inline fun String.firstCharUpperCase(): String = replaceFirstChar { it.uppercase() }
