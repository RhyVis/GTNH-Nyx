package vis.rhynia.nova.common.tile.base

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase
import gregtech.api.metatileentity.implementations.MTEHatch
import gregtech.api.metatileentity.implementations.MTEHatchDynamo
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.util.GTUtility
import gregtech.api.util.IGTHatchAdder
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.realms.Tezzelator.t
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid
import org.jetbrains.annotations.ApiStatus.OverrideOnly
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti
import vis.rhynia.nova.api.interfaces.ProcessExtension
import vis.rhynia.nova.api.process.logic.NovaProcessingLogic

abstract class NovaMTEBase<T : MTEExtendedPowerMultiBlockBase<T>> :
    MTEExtendedPowerMultiBlockBase<T>, IConstructable, ISurvivalConstructable, ProcessExtension {
  protected constructor(
      aId: Int,
      aName: String,
      aNameRegional: String
  ) : super(aId, aName, aNameRegional)

  protected constructor(aName: String) : super(aName)

  protected companion object {
    const val STRUCTURE_PIECE_MAIN = "Main" // Just a name

    val ExoticDynamo =
        object :
            HatchElement(
                adder = { t, aTileEntity, aBaseCasingIndex ->
                  t.addDynamoCompatible(aTileEntity, aBaseCasingIndex)
                },
                mteClasses = arrayOf(MTEHatchDynamoMulti::class.java)) {
          override fun name(): String = "ExoticDynamo"
          override fun count(t: NovaMTEBase<*>): Long = t.mExoticDynamoHatches.size.toLong()
        }
  }

  /** Remove maintenance requirement. */
  protected fun removeMaintenance() {
    mHardHammer = true
    mSoftHammer = true
    mScrewdriver = true
    mCrowbar = true
    mSolderingTool = true
    mWrench = true
  }

  override fun isCorrectMachinePart(aStack: ItemStack?): Boolean = true

  private val mExoticDynamoHatches: MutableList<MTEHatchDynamoMulti> = mutableListOf()

  protected abstract class HatchElement(
      private val adder: IGTHatchAdder<in NovaMTEBase<*>>,
      vararg mteClasses: Class<out IMetaTileEntity>
  ) : IHatchElement<NovaMTEBase<*>> {

    private val mteClasses: List<Class<out IMetaTileEntity>> = mteClasses.toList()

    override fun mteClasses(): List<Class<out IMetaTileEntity>> = mteClasses

    override fun adder(): IGTHatchAdder<in NovaMTEBase<*>> = adder

    abstract override fun name(): String
    abstract override fun count(t: NovaMTEBase<*>): Long
  }

  /** Universal Hatch Adder */
  override fun addToMachineList(aTileEntity: IGregTechTileEntity?, aBaseCasingIndex: Int): Boolean {
    return super.addToMachineList(aTileEntity, aBaseCasingIndex) ||
        addDynamoToMachineList(aTileEntity, aBaseCasingIndex)
  }

  override fun addDynamoToMachineList(
      aTileEntity: IGregTechTileEntity?,
      aBaseCasingIndex: Int
  ): Boolean {
    val mte = aTileEntity?.metaTileEntity ?: return false
    if (mte is MTEHatchDynamo) {
      mte.updateTexture(aBaseCasingIndex)
      return mDynamoHatches.add(mte)
    } else if (mte is MTEHatchDynamoMulti) {
      mte.updateTexture(aBaseCasingIndex)
      return mExoticDynamoHatches.add(mte)
    }
    return false
  }

  fun addMachineListCompatible(
      aTileEntity: IGregTechTileEntity?,
      aBaseCasingIndex: Short
  ): Boolean = addToMachineList(aTileEntity, aBaseCasingIndex.toInt())

  /** Add Exotic Dynamo to Machine List, Short type compatible */
  fun addDynamoCompatible(entity: IGregTechTileEntity, value: Short): Boolean {
    return addDynamoToMachineList(entity, value.toInt())
  }

  override fun addEnergyOutputMultipleDynamos(
      aEU: Long,
      aAllowMixedVoltageDynamos: Boolean
  ): Boolean {
    var tEU = aEU
    if (tEU < 0) tEU = -aEU

    fun processHatches(hatches: List<MTEHatch>, tEU: Long): Boolean {
      var remainingEU = tEU
      var freeCap: Long = 0

      GTUtility.filterValidMTEs(hatches).forEach {
        freeCap += it.maxEUStore() - it.baseMetaTileEntity.storedEU
        if (freeCap > 0) {
          if (remainingEU >= freeCap) {
            it.euVar = it.maxEUStore()
            remainingEU -= freeCap
          } else {
            it.euVar = it.baseMetaTileEntity.storedEU + remainingEU
            return true
          }
        }
      }
      return false
    }

    if (processHatches(mDynamoHatches, tEU)) return true
    if (processHatches(mExoticDynamoHatches, tEU)) return true

    return false
  }

  override fun getMaxEfficiency(aStack: ItemStack?): Int = 100_00

  override fun getDamageToComponent(aStack: ItemStack?): Int = 0

  override fun doRandomMaintenanceDamage(): Boolean = false

  override fun explodesOnComponentBreak(aStack: ItemStack?): Boolean = false

  override fun supportsVoidProtection(): Boolean = true

  override fun supportsInputSeparation(): Boolean = true

  override fun supportsBatchMode(): Boolean = true

  override fun supportsSingleRecipeLocking(): Boolean = true

  protected open val useAltLogic
    @OverrideOnly get() = false

  override fun createProcessingLogic(): ProcessingLogic {
    return if (useAltLogic)
        object : NovaProcessingLogic() {
          override fun process(): CheckRecipeResult {
            setEuModifier(rEuModifier)
            setMaxParallel(rMaxParallel)
            setSpeedBonus(rDurationModifier)
            setOverclock(if (rPerfectOverclock) 2.0 else 1.0, 2.0)
            return super.process()
          }
        }
    else
        object : ProcessingLogic() {
          override fun process(): CheckRecipeResult {
            setEuModifier(rEuModifier)
            setMaxParallel(rMaxParallel)
            setSpeedBonus(rDurationModifier)
            setOverclock(if (rPerfectOverclock) 2.0 else 1.0, 2.0)
            return super.process()
          }
        }
  }

  protected open val rPerfectOverclock
    @OverrideOnly get() = false

  protected open val rEuModifier
    @OverrideOnly get() = 1.0

  protected open val rDurationModifier
    @OverrideOnly get() = 1.0

  protected open val rMaxParallel
    @OverrideOnly get() = 1

  override fun consumeFluid(fluid: Fluid, amount: Int): Boolean {
    if (storedFluids.isNullOrEmpty()) return false
    var cost = amount
    storedFluids.forEach {
      if (it.getFluid() === fluid) {
        if (it.amount >= cost) {
          it.amount -= cost
          return true
        } else {
          cost -= it.amount
          it.amount = 0
        }
      }
    }
    return cost <= 0
  }

  override fun getInfoData(): Array<String> {
    val original = super.getInfoData()
    val originalSize = original.size
    return arrayOfNulls<String>(originalSize + 3).run {
      System.arraycopy(original, 0, this, 0, originalSize)
      this[originalSize] =
          "${EnumChatFormatting.AQUA}最大并行: ${EnumChatFormatting.GOLD}${GTUtility.formatNumbers(rMaxParallel.toLong())}"
      this[originalSize + 1] =
          "${EnumChatFormatting.AQUA}速度乘数: ${EnumChatFormatting.GOLD}${String.format("%.3f", rDurationModifier * 100)}%"
      this[originalSize + 2] =
          "${EnumChatFormatting.AQUA}功率乘数: ${EnumChatFormatting.GOLD}${String.format("%.3f", rEuModifier * 100)}%"
      this.filterNotNull().toTypedArray()
    }
  }

  override fun getWailaBody(
      itemStack: ItemStack?,
      currentTip: List<String?>?,
      accessor: IWailaDataAccessor?,
      config: IWailaConfigHandler?
  ) {
    super.getWailaBody(itemStack, currentTip, accessor, config)
  }

  override fun getWailaNBTData(
      player: EntityPlayerMP?,
      tile: TileEntity?,
      tag: NBTTagCompound?,
      world: World?,
      x: Int,
      y: Int,
      z: Int
  ) {
    super.getWailaNBTData(player, tile, tag, world, x, y, z)
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
  }
}
