package vis.rhynia.nova.api.util

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import vis.rhynia.nova.Log

@Suppress("unused")
object ItemUtil {
  fun getItemStack(item: Item?, amount: Int): ItemStack {
    if (item != null) {
      return ItemStack(item, amount)
    } else {
      throw IllegalArgumentException("Null item!")
    }
  }

  fun getItemStack(item: Item?, amount: Int, meta: Int): ItemStack {
    if (item != null) {
      return ItemStack(item, amount, meta)
    } else {
      throw IllegalArgumentException("Null item!")
    }
  }

  // region ItemStack
  fun metaItemEqual(a: ItemStack?, b: ItemStack?): Boolean {
    if (a == b) return true
    if (a == null || b == null) return false
    return a.item === b.item && a.itemDamage == b.itemDamage
  }

  /**
   *
   * @param isa1 The ItemStack Array 1.
   * @param isa2 The ItemStack Array 2.
   * @return The elements of these two arrays are identical and in the same order.
   */
  fun itemStackArrayEqualAbsolutely(isa1: Array<ItemStack>, isa2: Array<ItemStack>): Boolean {
    if (isa1.size != isa2.size) return false
    for (i in isa1.indices) {
      if (!metaItemEqual(isa1[i], isa2[i])) return false
      if (isa1[i].stackSize != isa2[i].stackSize) return false
    }
    return true
  }

  fun itemStackArrayEqualFuzzy(isa1: Array<ItemStack?>, isa2: Array<ItemStack?>): Boolean {
    if (isa1.size != isa2.size) return false
    for (itemStack1 in isa1) {
      var flag = false
      for (itemStack2 in isa2) {
        if (metaItemEqual(itemStack1, itemStack2)) {
          flag = true
          break
        }
      }
      if (!flag) return false
    }
    return true
  }

  fun copyItemStackArray(vararg array: ItemStack?): Array<ItemStack?> {
    val result = arrayOfNulls<ItemStack>(array.size)
    for (i in result.indices) {
      if (array[i] == null) continue
      result[i] = array[i]!!.copy()
    }
    return result
  }

  fun copyAmount(aAmount: Int, aStack: ItemStack): ItemStack {
    var aN = aAmount
    val rStack = aStack.copy()
    if (isStackInvalid(rStack)) throw IllegalArgumentException("Invalid ItemStack!")
    else if (aAmount == -1) aN = 111 else if (aAmount < 0) aN = 0
    rStack.stackSize = aN
    return rStack
  }

  fun isStackValid(aStack: ItemStack?): Boolean {
    return (aStack != null) && aStack.item != null && aStack.stackSize >= 0
  }

  fun isStackInvalid(aStack: ItemStack?): Boolean {
    return aStack == null || aStack.item == null || aStack.stackSize < 0
  }

  fun setStackSize(itemStack: ItemStack?, amount: Int): ItemStack? {
    if (itemStack == null) return null
    if (amount < 0) {
      Log.info(
          "Error! Trying to set a item stack size lower than zero! $itemStack to amount $amount")
      return itemStack
    }
    itemStack.stackSize = amount
    return itemStack
  }
}
