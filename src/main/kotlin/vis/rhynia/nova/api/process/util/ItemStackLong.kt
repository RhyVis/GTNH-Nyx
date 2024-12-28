package vis.rhynia.nova.api.process.util

import net.minecraft.item.ItemStack

class ItemStackLong(private val innerStack: ItemStack, private val size: Long) {
  val stackOrigin
    get() = innerStack
  val stackSize
    get() = size

  operator fun plus(other: ItemStackLong): ItemStackLong {
    if (innerStack.item != other.innerStack.item)
        throw IllegalArgumentException("Cannot add two ItemStackLongs with different items")
    return ItemStackLong(innerStack, size + other.size)
  }
}
