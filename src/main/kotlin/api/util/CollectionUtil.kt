@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.util

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet

/**
 * Kotlin style helper to create an [IntOpenHashSet].
 */
inline fun shortSetOf() = ShortOpenHashSet()

/**
 * Kotlin style helper to create an [IntOpenHashSet].
 */
inline fun intSetOf() = IntOpenHashSet()

/**
 * Kotlin style helper to create a [LongOpenHashSet].
 */
inline fun longSetOf() = LongOpenHashSet()

/**
 * Kotlin style helper to create an [Object2IntOpenHashMap].
 */
inline fun <K> objIntMapOf() = Object2IntOpenHashMap<K>()

/**
 * Kotlin style helper to create an [Object2LongOpenHashMap].
 */
inline fun <K> objLongMapOf() = Object2LongOpenHashMap<K>()

/**
 * Kotlin style helper to create a [Short2ObjectOpenHashMap].
 */
inline fun <V> shortObjMapOf() = Short2ObjectOpenHashMap<V>()

/**
 * Kotlin style helper to create an [Int2ObjectOpenHashMap].
 */
inline fun <V> intObjMapOf() = Int2ObjectOpenHashMap<V>()

/**
 * Kotlin style helper to create a [Long2ObjectOpenHashMap].
 */
inline fun <V> longObjMapOf() = Long2ObjectOpenHashMap<V>()
