package rhynia.nyx.api.util

import net.minecraft.item.ItemStack
import rhynia.nyx.Log
import rhynia.nyx.common.container.NyxItemList

@Suppress("unused")
object ItemUtil {

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

  private val astralInfinityGem by lazy { NyxItemList.AstriumInfinityGem.get(1) }
  private val astralInfinityComplex by lazy { NyxItemList.AstriumInfinityComplex.get(1) }
  private val astralInfinityGauge by lazy { NyxItemList.AstriumInfinityGauge.get(1) }
  private val calibration by lazy { NyxItemList.Calibration.get(1) }

  fun isAstralInfinityGem(itemStack: ItemStack?): Boolean {
    return itemStack?.isItemEqual(astralInfinityGem) == true
  }

  fun isAstralInfinityComplex(itemStack: ItemStack?): Boolean {
    return itemStack?.isItemEqual(astralInfinityComplex) == true
  }

  fun isAstralInfinityGauge(itemStack: ItemStack?): Boolean {
    return itemStack?.isItemEqual(astralInfinityGauge) == true
  }

  fun isCalibration(itemStack: ItemStack?): Boolean {
    return itemStack?.isItemEqual(calibration) == true
  }
}
