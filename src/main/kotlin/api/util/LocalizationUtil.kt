@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.util

import net.minecraft.util.StatCollector.canTranslate
import net.minecraft.util.StatCollector.translateToLocal
import net.minecraft.util.StatCollector.translateToLocalFormatted

/**
 * Shorthand for [canTranslate]
 */
inline fun hasLocalization(key: String): Boolean = canTranslate(key)

/**
 * Shorthand for [translateToLocal]
 */
inline fun localize(key: String): String = translateToLocal(key)

/**
 * Shorthand for [translateToLocalFormatted]
 */
inline fun localize(
    key: String,
    vararg args: Any,
): String = translateToLocalFormatted(key, args)

/**
 * Shorthand for [translateToLocal]
 */
inline fun String.localized(): String = translateToLocal(this)
