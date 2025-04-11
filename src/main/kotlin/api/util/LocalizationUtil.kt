package rhynia.nyx.api.util

import net.minecraft.util.StatCollector.translateToLocal
import net.minecraft.util.StatCollector.translateToLocalFormatted

/**
 * Shorthand for [translateToLocal]
 */
fun localize(key: String): String = translateToLocal(key)

/**
 * Shorthand for [translateToLocalFormatted]
 */
fun localize(
    key: String,
    vararg args: Any,
): String = translateToLocalFormatted(key, args)
