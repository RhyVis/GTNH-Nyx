package vis.rhynia.nova.common

import gregtech.api.util.GTLog
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import vis.rhynia.nova.api.util.ItemUtil

enum class NovaItemList {
  // region MetaItem 01
  TestItem01,
  LapotronMatrix,
  CrystalMatrix,
  DenseMicaInsulatorFoil,
  PreTesseract,
  // endregion

  // region Machine Controller
  AstralForge,
  // endregion

  ;

  private var mHasNotBeenSet: Boolean = true
  private var mDeprecated: Boolean = false
  private var mWarned: Boolean = false

  private lateinit var mStack: ItemStack

  val hasBeenSet: Boolean
    get() = !mHasNotBeenSet

  constructor() {
    mHasNotBeenSet = true
  }

  constructor(deprecated: Boolean) {
    mDeprecated = deprecated
  }

  fun getItem(): Item {
    safetyCheck()
    if (ItemUtil.isStackInvalid(mStack))
        throw NullPointerException("The ItemStack for $this is invalid!")
    return mStack.item
  }

  fun getBlock(): Block {
    safetyCheck()
    return Block.getBlockFromItem(getItem())
  }

  fun get(amount: Int): ItemStack {
    safetyCheck()
    if (ItemUtil.isStackInvalid(mStack))
        GTLog.out.let {
          println("The ItemStack for $this is invalid!")
          NullPointerException().printStackTrace(it)
          ItemUtil.copyAmount(amount, TestItem01.get(1))
        }
    return ItemUtil.copyAmount(amount, mStack)
  }

  fun getMeta(): Int = mStack.itemDamage

  fun set(item: Item?): NovaItemList {
    if (item == null) return this
    mHasNotBeenSet = false
    mStack = ItemUtil.copyAmount(1, ItemStack(item, 1, 0))
    return this
  }

  fun set(itemStack: ItemStack?): NovaItemList {
    if (itemStack == null) return this
    mHasNotBeenSet = false
    mStack = ItemUtil.copyAmount(1, itemStack)
    return this
  }

  private fun safetyCheck() {
    if (mHasNotBeenSet)
        throw IllegalAccessError("The Enum '$name' has not been set to an Item at this time!")
    if (mDeprecated && !mWarned) {
      Exception("$this is now deprecated").printStackTrace(GTLog.err)
      mWarned = true
    }
  }
}
