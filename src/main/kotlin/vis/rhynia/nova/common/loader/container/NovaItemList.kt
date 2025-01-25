package vis.rhynia.nova.common.loader.container

import gregtech.api.enums.GTValues
import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTLog
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTUtility
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import vis.rhynia.nova.api.util.ItemUtil

@Suppress("unused")
enum class NovaItemList : IItemContainer {
  ItemUltimate,

  // region MetaItem 01

  // Functional Items
  TestItem01,
  LapotronMatrix,
  CrystalMatrix,
  DenseMicaInsulatorFoil,
  PreTesseract,

  // Astrium Items
  AstriumInfinityGem,
  AstriumInfinityComplex,
  AstriumInfinityGauge,

  // Lens Items
  LensAstriumInfinity,
  LensAstriumMagic,
  LensPrimoium,
  LensOriginium,

  // endregion

  // region MetaItem 02

  Calibration,
  AssemblyDTPF,

  // endregion

  // region Block

  TestMetaBlock01,
  EOHCoreT1,
  EOHCoreT2,
  EOHCoreT3,
  EOHCoreT4,
  EOHCoreT5,
  EOHCoreT6,
  EOHCoreT7,
  EOHCoreT8,
  EOHCoreT9,

  // endregion

  // region Machine Controller
  AstralForge,
  AtomMacro,
  AssemblyMatrix,
  KelvinTransformField,
  SelectedEnergyGenerator,
  Creator,
  EyeOfUltimate,
  DenseEndpoint,
  // endregion

  // Sig Block
  InfiniteLiquidAirHatch,
  InfiniteDistilledWaterHatch,
  InfiniteLavaHatch,
  InfiniteOilHatch,
  InfiniteLubricantHatch,
  InfiniteSteamHatch,
  ZeroGenerator,
  HumongousCalibrationInputHatch,
  HumongousCalibrationHalfInputHatch,
  ;
  // endregion;

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
          ItemUtil.copyAmount(aAmount.toInt(), TestItem01.get(1))
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
