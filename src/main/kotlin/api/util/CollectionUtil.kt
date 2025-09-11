package rhynia.nyx.api.util

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap

/**
 * Kotlin style helper to create an [Object2LongOpenHashMap].
 */
fun <K> objLongMapOf() = Object2LongOpenHashMap<K>()

/**
 * Kotlin style helper to create an [Int2ObjectOpenHashMap].
 */
fun <V> intObjMapOf() = Int2ObjectOpenHashMap<V>()
