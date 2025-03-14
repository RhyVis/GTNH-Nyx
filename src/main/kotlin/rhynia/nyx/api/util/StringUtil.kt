package rhynia.nyx.api.util

fun String.firstCharUpperCase(): String {
  return replaceFirstChar { it.uppercase() }
}
