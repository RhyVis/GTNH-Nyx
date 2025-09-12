@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import rhynia.nyx.common.NyxItemList

/**
 * A token representing an [Item] and its meta value (damage value).
 */
class MetaItemToken(
    val item: Item,
    val meta: Int,
) {
    constructor(itemStack: ItemStack) : this(itemStack.item, itemStack.itemDamage)

    operator fun component1(): Item = item

    operator fun component2(): Int = meta

    fun isEmpty(): Boolean = this == EMPTY

    fun createStack(size: Int = 1): ItemStack = ItemStack(item, size, meta)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MetaItemToken) return false

        if (item != other.item) return false
        if (meta != other.meta) return false

        return true
    }

    override fun hashCode(): Int {
        var result = item.hashCode()
        result = 31 * result + meta
        return result
    }

    override fun toString(): String = "MetaItemToken(item=$item, meta=$meta)"

    companion object {
        val EMPTY by lazy { MetaItemToken(NyxItemList.TestItem01.get(1)) }

        fun matches(
            i: ItemStack,
            t: MetaItemToken,
        ): Boolean = i.item == t.item && i.itemDamage == t.meta
    }
}

/**
 * Creates a [MetaItemToken] from this [ItemStack].
 */
inline fun ItemStack.asToken(): MetaItemToken = MetaItemToken(this)

/**
 * Checks if this [ItemStack] matches the given [MetaItemToken].
 */
inline infix fun ItemStack?.matches(t: MetaItemToken): Boolean = this != null && MetaItemToken.matches(this, t)
