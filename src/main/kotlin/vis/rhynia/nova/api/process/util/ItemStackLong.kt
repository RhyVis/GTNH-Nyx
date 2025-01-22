package vis.rhynia.nova.api.process.util

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class ItemStackLong(private val innerStack: ItemStack, private var size: Long) :
    Iterable<ItemStack> {
  val stackOrigin
    get() = innerStack
  val stackSize
    get() = size
  val item: Item
    get() = innerStack.item

  operator fun plus(other: ItemStackLong): ItemStackLong {
    if (innerStack.item != other.innerStack.item)
        throw IllegalArgumentException("Cannot add two ItemStackLongs with different items")
    return ItemStackLong(innerStack, size + other.size)
  }

  override fun iterator(): Iterator<ItemStack> =
      object : Iterator<ItemStack> {
        override fun hasNext(): Boolean {
          return size > 0
        }

        override fun next(): ItemStack {
          val currentSize = if (size > Int.MAX_VALUE) Int.MAX_VALUE else size.toInt()
          size -= currentSize
          return ItemStack(item, currentSize, innerStack.itemDamage)
        }
      }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    other as ItemStackLong

    if (item != other.innerStack.item) return false
    if (size != other.size) return false

    return true
  }

  override fun hashCode(): Int {
    var result = size.hashCode()
    result = 31 * result + innerStack.item.hashCode()
    result = 31 * result + stackSize.hashCode()
    return result
  }
}
