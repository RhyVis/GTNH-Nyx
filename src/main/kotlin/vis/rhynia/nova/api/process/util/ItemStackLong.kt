package vis.rhynia.nova.api.process.util

import net.minecraft.item.ItemStack

class ItemStackLong(private val innerStack: ItemStack, private var size: Long) :
    Iterable<ItemStack> {
  val stackOrigin
    get() = innerStack
  val stackSize
    get() = size

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
          return ItemStack(innerStack.item, currentSize, innerStack.itemDamage)
        }
      }
}
