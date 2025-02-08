package rhynia.constellation.common.container

import gregtech.api.enums.GTValues
import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTLog
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTUtility
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import rhynia.constellation.api.util.ItemUtil

@Suppress("unused")
enum class CelWirelessHatchList : IItemContainer {
  ExtLaserIV1(5, 256),
  ExtLaserIV2(5, 1024),
  ExtLaserIV3(5, 4096),
  ExtLaserIV4(5, 16384),
  ExtLaserIV5(5, 65536),
  ExtLaserIV6(5, 262144),
  ExtLaserIV7(5, 1048576),
  ExtLaserLuV1(6, 256),
  ExtLaserLuV2(6, 1024),
  ExtLaserLuV3(6, 4096),
  ExtLaserLuV4(6, 16384),
  ExtLaserLuV5(6, 65536),
  ExtLaserLuV6(6, 262144),
  ExtLaserLuV7(6, 1048576),
  ExtLaserZPM1(7, 256),
  ExtLaserZPM2(7, 1024),
  ExtLaserZPM3(7, 4096),
  ExtLaserZPM4(7, 16384),
  ExtLaserZPM5(7, 65536),
  ExtLaserZPM6(7, 262144),
  ExtLaserZPM7(7, 1048576),
  ExtLaserUV1(8, 256),
  ExtLaserUV2(8, 1024),
  ExtLaserUV3(8, 4096),
  ExtLaserUV4(8, 16384),
  ExtLaserUV5(8, 65536),
  ExtLaserUV6(8, 262144),
  ExtLaserUV7(8, 1048576),
  ExtLaserUHV1(9, 256),
  ExtLaserUHV2(9, 1024),
  ExtLaserUHV3(9, 4096),
  ExtLaserUHV4(9, 16384),
  ExtLaserUHV5(9, 65536),
  ExtLaserUHV6(9, 262144),
  ExtLaserUHV7(9, 1048576),
  ExtLaserUEV1(10, 256),
  ExtLaserUEV2(10, 1024),
  ExtLaserUEV3(10, 4096),
  ExtLaserUEV4(10, 16384),
  ExtLaserUEV5(10, 65536),
  ExtLaserUEV6(10, 262144),
  ExtLaserUEV7(10, 1048576),
  ExtLaserUIV1(11, 256),
  ExtLaserUIV2(11, 1024),
  ExtLaserUIV3(11, 4096),
  ExtLaserUIV4(11, 16384),
  ExtLaserUIV5(11, 65536),
  ExtLaserUIV6(11, 262144),
  ExtLaserUIV7(11, 1048576),
  ExtLaserUMV1(12, 256),
  ExtLaserUMV2(12, 1024),
  ExtLaserUMV3(12, 4096),
  ExtLaserUMV4(12, 16384),
  ExtLaserUMV5(12, 65536),
  ExtLaserUMV6(12, 262144),
  ExtLaserUMV7(12, 1048576),
  ;
  // endregion;

  private var mHasNotBeenSet: Boolean = true
  private var mDeprecated: Boolean = false
  private var mWarned: Boolean = false

  var tier: Int = 0
  var amp: Int = 0

  private lateinit var mStack: ItemStack

  val hasBeenSet: Boolean
    get() = !mHasNotBeenSet

  val tierName: String
    get() =
        when (tier) {
          5 -> "IV"
          6 -> "LuV"
          7 -> "ZPM"
          8 -> "UV"
          9 -> "UHV"
          10 -> "UEV"
          11 -> "UIV"
          12 -> "UMV"
          else -> "?"
        }

  constructor(tier: Int, amp: Int) {
    this.tier = tier
    this.amp = amp
    mHasNotBeenSet = true
  }

  constructor() {
    mHasNotBeenSet = true
  }

  constructor(deprecated: Boolean) {
    mDeprecated = deprecated
  }

  override fun getItem(): Item {
    safetyCheck()
    if (ItemUtil.isStackInvalid(mStack))
        throw NullPointerException("The ItemStack for $this is invalid!")
    return mStack.item
  }

  override fun getBlock(): Block {
    safetyCheck()
    return Block.getBlockFromItem(getItem())
  }

  override fun isStackEqual(aStack: Any?): Boolean {
    return isStackEqual(aStack, false, false)
  }

  @Suppress("DEPRECATION")
  override fun isStackEqual(aStack: Any?, aWildcard: Boolean, aIgnoreNBT: Boolean): Boolean {
    if (mDeprecated && !mWarned) {
      Exception("$this is now deprecated").printStackTrace(GTLog.err)
      mWarned = true
    }
    if (GTUtility.isStackInvalid(aStack)) return false
    return GTUtility.areUnificationsEqual(
        aStack as ItemStack, if (aWildcard) getWildcard(1) else get(1), aIgnoreNBT)
  }

  override fun get(aAmount: Long, vararg aReplacements: Any?): ItemStack {
    safetyCheck()
    if (ItemUtil.isStackInvalid(mStack))
        GTLog.out.let {
          println("The ItemStack for $this is invalid!")
          NullPointerException().printStackTrace(it)
          ItemUtil.copyAmount(aAmount.toInt(), CelItemList.TestItem01.get(1))
        }
    return ItemUtil.copyAmount(aAmount.toInt(), mStack)
  }

  @Suppress("DEPRECATION")
  override fun getWildcard(aAmount: Long, vararg aReplacements: Any?): ItemStack {
    safetyCheck()
    if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, *aReplacements)
    return GTUtility.copyAmountAndMetaData(
        aAmount.toInt(), GTValues.W.toInt(), GTOreDictUnificator.get(mStack))
  }

  @Suppress("DEPRECATION")
  override fun getUndamaged(aAmount: Long, vararg aReplacements: Any?): ItemStack {
    safetyCheck()
    if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, *aReplacements)
    return GTUtility.copyAmountAndMetaData(aAmount, 0, GTOreDictUnificator.get(mStack))
  }

  @Suppress("DEPRECATION")
  override fun getAlmostBroken(aAmount: Long, vararg aReplacements: Any?): ItemStack? {
    safetyCheck()
    if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, *aReplacements)
    return GTUtility.copyAmountAndMetaData(
        aAmount.toInt(), mStack.maxDamage - 1, GTOreDictUnificator.get(mStack))
  }

  @Suppress("DEPRECATION")
  override fun getWithDamage(
      aAmount: Long,
      aMetaValue: Long,
      vararg aReplacements: Any?
  ): ItemStack? {
    safetyCheck()
    if (GTUtility.isStackInvalid(mStack)) return GTUtility.copyAmount(aAmount, *aReplacements)
    return GTUtility.copyAmountAndMetaData(aAmount, aMetaValue, GTOreDictUnificator.get(mStack))
  }

  override fun set(item: Item?): IItemContainer {
    if (item == null) return this
    mHasNotBeenSet = false
    mStack = ItemUtil.copyAmount(1, ItemStack(item, 1, 0))
    return this
  }

  override fun set(itemStack: ItemStack?): IItemContainer {
    if (itemStack == null) return this
    mHasNotBeenSet = false
    mStack = ItemUtil.copyAmount(1, itemStack)
    return this
  }

  override fun registerOre(vararg aOreNames: Any?): IItemContainer = this

  override fun registerWildcardAsOre(vararg aOreNames: Any?): IItemContainer = this

  override fun getWithCharge(aAmount: Long, aEnergy: Int, vararg aReplacements: Any?): ItemStack {
    return this.get(aAmount, *aReplacements)
  }

  override fun getWithName(
      aAmount: Long,
      aDisplayName: String?,
      vararg aReplacements: Any?
  ): ItemStack? {
    return this.get(aAmount, *aReplacements)
  }

  override fun hasBeenSet(): Boolean = !mHasNotBeenSet

  private fun safetyCheck() {
    if (mHasNotBeenSet)
        throw IllegalAccessError("The Enum '$name' has not been set to an Item at this time!")
    if (mDeprecated && !mWarned) {
      Exception("$this is now deprecated").printStackTrace(GTLog.err)
      mWarned = true
    }
  }
}
