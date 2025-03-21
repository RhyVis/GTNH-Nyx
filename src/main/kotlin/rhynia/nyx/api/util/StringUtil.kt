package rhynia.nyx.api.util

fun String.firstCharUpperCase(): String = replaceFirstChar { it.uppercase() }
