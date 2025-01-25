package vis.rhynia.nova.common.tile.base

import gregtech.api.enums.Textures
import gregtech.api.interfaces.IIconContainer
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.metatileentity.implementations.MTEHatchInput
import gregtech.api.objects.GTRenderedTexture
import gregtech.api.objects.XSTR
import gregtech.api.util.GTUtility
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.api.enums.NovaValues

@Suppress("unused", "SpellCheckingInspection")
abstract class NovaMTEHatchFluidGenerator : MTEHatchInput {
  companion object {
    protected val floatGen: XSTR = XSTR()
  }

  var mProgresstime: Int = 0
  var mMaxProgresstime: Int = 0
  constructor(
      aID: Int,
      aName: String?,
      aNameRegional: String?,
      aTier: Int
  ) : super(aID, aName, aNameRegional, aTier)

  constructor(
      aName: String,
      aTier: Int,
      aDescription: Array<String>,
      aTextures: Array<Array<Array<ITexture>>>
  ) : super(aName, aTier, aDescription, aTextures)

  abstract fun getCustomTooltip(): Array<String?>

  abstract fun getFluidToGenerate(): Fluid?

  abstract fun getAmountOfFluidToGenerate(): Int

  abstract fun getMaxTickTime(): Int

  @Synchronized
  override fun getDescription(): Array<String?> {
    mDescriptionArray[1] = "容量: " + GTUtility.formatNumbers(getCapacity().toLong()) + "L"
    val hatchTierString: Array<String> =
        arrayOf<String>("仓室等级: ${GTUtility.getColoredTierNameFromTier(mTier)}")

    val aCustomTips = getCustomTooltip()
    val desc = arrayOfNulls<String>(mDescriptionArray.size + aCustomTips.size + 2)
    System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.size)
    System.arraycopy(hatchTierString, 0, desc, mDescriptionArray.size, 1)
    System.arraycopy(aCustomTips, 0, desc, mDescriptionArray.size + 1, aCustomTips.size)
    desc[mDescriptionArray.size + aCustomTips.size] = NovaValues.CommonStrings.AddByNova
    return desc
  }

  @Suppress("DEPRECATION")
  override fun getTexturesActive(aBaseTexture: ITexture?): Array<ITexture?> {
    return arrayOf<ITexture?>(
        aBaseTexture, GTRenderedTexture(Textures.BlockIcons.OVERLAY_MUFFLER as IIconContainer))
  }

  @Suppress("DEPRECATION")
  override fun getTexturesInactive(aBaseTexture: ITexture?): Array<ITexture?> {
    return arrayOf<ITexture?>(
        aBaseTexture, GTRenderedTexture(Textures.BlockIcons.OVERLAY_MUFFLER as IIconContainer))
  }

  override fun isSimpleMachine(): Boolean {
    return true
  }

  override fun isFacingValid(facing: ForgeDirection?): Boolean {
    return true
  }

  override fun isAccessAllowed(aPlayer: EntityPlayer?): Boolean {
    return true
  }

  override fun isValidSlot(aIndex: Int): Boolean {
    return false
  }

  abstract override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity?

  override fun allowPullStack(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aIndex: Int,
      side: ForgeDirection?,
      aStack: ItemStack?
  ): Boolean {
    return false
  }

  override fun allowPutStack(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aIndex: Int,
      side: ForgeDirection?,
      aStack: ItemStack?
  ): Boolean {
    return false
  }

  override fun onPostTick(aBaseMetaTileEntity: IGregTechTileEntity, aTick: Long) {
    super.onPostTick(aBaseMetaTileEntity, aTick)
    if (!aBaseMetaTileEntity.isAllowedToWork) {
      aBaseMetaTileEntity.isActive = false
      mProgresstime = 0
      mMaxProgresstime = 0
    } else {
      aBaseMetaTileEntity.isActive = true
      mMaxProgresstime = getMaxTickTime()
      if (++mProgresstime >= mMaxProgresstime) {
        if (this.canTankBeFilled()) {
          addFluidToHatch(aTick)
        }
        mProgresstime = 0
      }
    }
  }

  override fun getProgresstime(): Int {
    return mProgresstime
  }

  override fun maxProgresstime(): Int {
    return mMaxProgresstime
  }

  override fun increaseProgress(aProgress: Int): Int {
    mProgresstime += aProgress
    return mMaxProgresstime - mProgresstime
  }

  abstract fun generateParticles(aWorld: World?, name: String?)

  override fun getTankPressure(): Int {
    return 100
  }

  override fun getCapacity(): Int {
    return 2000000000
  }

  override fun canTankBeEmptied(): Boolean {
    return true
  }

  abstract fun doesHatchMeetConditionsToGenerate(): Boolean

  fun addFluidToHatch(aTick: Long): Boolean {
    if (!doesHatchMeetConditionsToGenerate()) {
      return false
    }
    val aFillAmount =
        this.fill(
            FluidUtils.getFluidStack(getFluidToGenerate(), getAmountOfFluidToGenerate()), true)
    if (aFillAmount > 0) {
      if (this.baseMetaTileEntity.isClientSide) {
        generateParticles(this.baseMetaTileEntity.world, "cloud")
      }
    }
    return aFillAmount > 0
  }

  override fun canTankBeFilled(): Boolean {
    return true
  }

  override fun doesEmptyContainers(): Boolean {
    return false
  }

  override fun doesFillContainers(): Boolean {
    return true
  }

  override fun fill(aFluid: FluidStack?, doFill: Boolean): Int {
    if (aFluid == null ||
        aFluid.getFluid().id <= 0 ||
        aFluid.amount <= 0 ||
        aFluid.getFluid() !== getFluidToGenerate() ||
        !canTankBeFilled()) {
      return 0
    }

    if (fillableStack == null || fillableStack.getFluid().id <= 0) {
      if (aFluid.amount <= getCapacity()) {
        if (doFill) {
          setFillableStack(aFluid.copy())
          baseMetaTileEntity.markDirty()
        }
        return aFluid.amount
      }
      if (doFill) {
        setFillableStack(aFluid.copy())
        fillableStack.amount = getCapacity()
        baseMetaTileEntity.markDirty()
      }
      return getCapacity()
    }

    if (!fillableStack.isFluidEqual(aFluid)) return 0

    val space = getCapacity() - fillableStack.amount
    if (aFluid.amount <= space) {
      if (doFill) {
        fillableStack.amount += aFluid.amount
        baseMetaTileEntity.markDirty()
      }
      return aFluid.amount
    }
    if (doFill) fillableStack.amount = getCapacity()
    return space
  }

  override fun canFill(aSide: ForgeDirection?, aFluid: Fluid?): Boolean {
    return false
  }

  override fun fill(arg0: ForgeDirection?, arg1: FluidStack?, arg2: Boolean): Int {
    return 0
  }

  override fun fill_default(aSide: ForgeDirection?, aFluid: FluidStack?, doFill: Boolean): Int {
    return 0
  }

  override fun saveNBTData(aNBT: NBTTagCompound) {
    aNBT.setInteger("mProgresstime", mProgresstime)
    aNBT.setInteger("mMaxProgresstime", mMaxProgresstime)
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound) {
    mProgresstime = aNBT.getInteger("mProgresstime")
    mMaxProgresstime = aNBT.getInteger("mMaxProgresstime")
    super.loadNBTData(aNBT)
  }
}
