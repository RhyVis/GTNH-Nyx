package rhynia.nyx.api.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

/**
 * A token representing an [Item] and its metadata (damage value).
 */
class MetaItemToken(
    val item: Item,
    val meta: Int,
) {
    constructor(itemStack: ItemStack) : this(itemStack.item, itemStack.itemDamage)

    operator fun component1(): Item = item

    operator fun component2(): Int = meta

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
}

/**
 * Creates a [MetaItemToken] from this [ItemStack].
 */
fun ItemStack.asToken(): MetaItemToken = MetaItemToken(this)
