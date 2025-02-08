package rhynia.constellation.common.tile.base

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable
import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase
import gregtech.api.metatileentity.implementations.MTEHatch
import gregtech.api.metatileentity.implementations.MTEHatchDynamo
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.render.TextureFactory
import gregtech.api.util.GTUtility
import gregtech.api.util.IGTHatchAdder
import gregtech.common.tileentities.machines.MTEHatchOutputBusME
import gregtech.common.tileentities.machines.MTEHatchOutputME
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.GOLD
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import org.jetbrains.annotations.ApiStatus.OverrideOnly
import rhynia.constellation.api.util.FluidUtil.idEqual
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti

@Suppress("UNUSED")
abstract class CelMTEBase<T : MTEExtendedPowerMultiBlockBase<T>> :
    MTEExtendedPowerMultiBlockBase<T>, IConstructable, ISurvivalConstructable {

  protected constructor(
      aId: Int,
      aName: String,
      aNameRegional: String
  ) : super(aId, aName, aNameRegional)
  protected constructor(aName: String) : super(aName)

  protected companion object {
    const val STRUCTURE_PIECE_MAIN = "Main" // Just a name

    /** Simulated Hatch Element for Exotic Energy Hatch which has multiple allowed ampere. */
    val ExoticDynamo =
        object :
            HatchElement(
                adder = { t, aTileEntity, aBaseCasingIndex ->
                  t.addDynamoCompatible(aTileEntity, aBaseCasingIndex)
                },
                mteClasses = arrayOf(MTEHatchDynamoMulti::class.java)) {
          override fun name(): String = "ExoticDynamo"
          override fun count(t: CelMTEBase<*>): Long = t.mExoticDynamoHatches.size.toLong()
        }

    /**
     * Structure Definition for the machine, set when first time calling getStructureDefinition().
     */
    val cachedStructureDefs: MutableMap<Class<out CelMTEBase<*>>, IStructureDefinition<*>> =
        mutableMapOf()
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
      private val adder: IGTHatchAdder<in CelMTEBase<*>>,
      vararg mteClasses: Class<out IMetaTileEntity>
  ) : IHatchElement<CelMTEBase<*>> {

    private val mteClasses: List<Class<out IMetaTileEntity>> = mteClasses.toList()

    override fun mteClasses(): List<Class<out IMetaTileEntity>> = mteClasses

    override fun adder(): IGTHatchAdder<in CelMTEBase<*>> = adder

    abstract override fun name(): String
    abstract override fun count(t: CelMTEBase<*>): Long
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

  /** Add everything to Machine List, Short type compatible */
  fun addToMachineList(aTileEntity: IGregTechTileEntity?, aBaseCasingIndex: Short): Boolean {
    return addToMachineList(aTileEntity, aBaseCasingIndex.toInt())
  }

  /** Add everything to Machine List, Short type compatible */
  protected fun addMachineListCompatible(
      aTileEntity: IGregTechTileEntity?,
      aBaseCasingIndex: Short
  ): Boolean = addToMachineList(aTileEntity, aBaseCasingIndex.toInt())

  /** Add Exotic Dynamo to Machine List, Short type compatible */
  protected fun addDynamoCompatible(entity: IGregTechTileEntity, value: Short): Boolean {
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

  /** Set the structure definition for the machine. */
  protected abstract fun genStructureDefinition(): IStructureDefinition<T>

  @Suppress("UNCHECKED_CAST")
  final override fun getStructureDefinition(): IStructureDefinition<T> =
      cachedStructureDefs.getOrPut(this::class.java) { genStructureDefinition() }
          as IStructureDefinition<T>

  /** Controller Block and Meta, used for calculating casing texture index. */
  protected abstract val sControllerBlock: Pair<Block, Int>

  protected open val sControllerCasingIndex: Int
    get() = GTUtility.getCasingTextureIndex(sControllerBlock.first, sControllerBlock.second)

  /** Controller Icon for active state, left is normal, right is glow. */
  protected open val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE to OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW

  /** Controller Icon for inactive state, left is normal, right is glow. */
  protected open val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_FRONT_ASSEMBLY_LINE to OVERLAY_FRONT_ASSEMBLY_LINE_GLOW

  override fun getTexture(
      baseMetaTileEntity: IGregTechTileEntity,
      side: ForgeDirection,
      facing: ForgeDirection,
      colorIndex: Int,
      active: Boolean,
      redstoneLevel: Boolean
  ): Array<ITexture> =
      if (side != facing) arrayOf(Textures.BlockIcons.getCasingTextureForId(sControllerCasingIndex))
      else if (active)
          arrayOf(
              Textures.BlockIcons.getCasingTextureForId(sControllerCasingIndex),
              TextureFactory.builder().addIcon(sControllerIconActive.first).extFacing().build(),
              TextureFactory.builder()
                  .addIcon(sControllerIconActive.second)
                  .extFacing()
                  .glow()
                  .build())
      else
          arrayOf(
              Textures.BlockIcons.getCasingTextureForId(sControllerCasingIndex),
              TextureFactory.builder().addIcon(OVERLAY_FRONT_ASSEMBLY_LINE).extFacing().build(),
              TextureFactory.builder()
                  .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                  .extFacing()
                  .glow()
                  .build())

  override fun getMaxEfficiency(aStack: ItemStack?): Int = 100_00

  override fun getDamageToComponent(aStack: ItemStack?): Int = 0

  override fun doRandomMaintenanceDamage(): Boolean = false

  override fun explodesOnComponentBreak(aStack: ItemStack?): Boolean = false

  override fun supportsVoidProtection(): Boolean = true

  override fun supportsInputSeparation(): Boolean = true

  override fun supportsBatchMode(): Boolean = true

  override fun supportsSingleRecipeLocking(): Boolean = true

  override fun createProcessingLogic(): ProcessingLogic? =
      object : ProcessingLogic() {
        override fun process(): CheckRecipeResult {
          setEuModifier(rEuModifier)
          setMaxParallel(rMaxParallel)
          setSpeedBonus(rDurationModifier)
          setOverclock(if (rPerfectOverclock) 2.0 else 1.0, 2.0)
          return super.process()
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

  protected fun consumeFluid(fluid: Fluid, amount: Int): Boolean {
    if (storedFluids.isNullOrEmpty()) return false
    var amount = amount

    // Check if there is enough fluid stored.
    storedFluids
        .filter { it.idEqual(fluid) }
        .sumOf { it.amount }
        .let { if (it < amount) return false }

    storedFluids.forEach {
      if (it.getFluid().idEqual(fluid)) {
        if (it.amount >= amount) {
          it.amount -= amount
          return true
        } else {
          amount -= it.amount
          it.amount = 0
        }
      }
    }

    return amount <= 0
  }

  protected fun outputItemToAENetwork(item: ItemStack?, amount: Long) {
    if ((item == null) || (amount <= 0)) return
    var amount = amount

    mOutputBusses
        .firstOrNull { it is MTEHatchOutputBusME }
        ?.let {
          it as MTEHatchOutputBusME
          if (amount < Int.MAX_VALUE) {
            it.store(item.copy().apply { stackSize = amount.toInt() })
          } else {
            // For item stacks > Int max.
            while (amount >= Int.MAX_VALUE) {
              it.store(item.copy().apply { stackSize = Int.MAX_VALUE })
              amount -= Int.MAX_VALUE.toLong()
            }

            if (amount > 0) {
              it.store(item.copy().apply { stackSize = amount.toInt() })
            }
          }
        }
  }

  protected fun outputFluidToAENetwork(fluid: FluidStack?, amount: Long) {
    if ((fluid == null) || (amount <= 0)) return
    var amount = amount

    mOutputHatches
        .firstOrNull { it is MTEHatchOutputME }
        ?.let {
          it as MTEHatchOutputME
          if (amount < Int.MAX_VALUE) {
            it.tryFillAE(fluid.copy().apply { this.amount = amount.toInt() })
          } else {
            // For fluidStacks > Int max.
            while (amount >= Int.MAX_VALUE) {
              it.tryFillAE(fluid.copy().apply { this.amount = Int.MAX_VALUE })
              amount -= Int.MAX_VALUE.toLong()
            }

            if (amount > 0) {
              it.tryFillAE(fluid.copy().apply { this.amount = amount.toInt() })
            }
          }
        }
  }

  /** Format Double number as % */
  protected fun Double.formatPercent() = "%.3f%%".format(this * 100)

  final override fun getInfoData(): Array<String> =
      super.getInfoData() +
          arrayOf(
              "${AQUA}最大并行: ${GOLD}${GTUtility.formatNumbers(rMaxParallel.toLong())}",
              "${AQUA}速度乘数: ${GOLD}${rDurationModifier.formatPercent()}",
              "${AQUA}功率乘数: ${GOLD}${rEuModifier.formatPercent()}",
          ) +
          getInfoDataExtra()

  /** Extra information added after getInfoData() */
  @OverrideOnly protected open fun getInfoDataExtra(): Array<String> = arrayOf()

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

  /** Mode Container for switching between modes. */
  protected open class ModeContainerPrimitive(val size: Int) {
    /** Current index of the mode. */
    var index = 0
      protected set

    /** Get the next index, return to 0 if reached the end. */
    fun next(): Int {
      index = (index + 1) % size
      return index
    }

    /** Get the previous index, return to the end if reached 0. */
    fun prev(): Int {
      index = (index - 1 + size) % size
      return index
    }

    /** Save the mode index to NBT. */
    fun saveNBTData(aNBT: NBTTagCompound, key: String) {
      aNBT.setInteger(key, index)
    }

    /** Load the mode index from NBT. */
    fun loadNBTData(aNBT: NBTTagCompound, key: String) {
      index = aNBT.getInteger(key)
    }
  }

  /**
   * Mode Container for switching between modes, with additional type support.
   *
   * @param T Type of the modes.
   */
  protected class ModeContainer<T>(private val modes: Array<out T>) :
      ModeContainerPrimitive(modes.size) {

    companion object {
      /** Create a ModeContainer with vararg modes. */
      inline fun <reified T> of(vararg modes: T): ModeContainer<T> = ModeContainer(modes)
    }

    /** Current mode. */
    val current: T
      get() = modes[index]

    val all: Collection<T>
      get() = modes.toCollection(mutableListOf())

    /** Get the next mode, return to the first mode if reached the end. */
    fun nextMode(): T {
      next()
      return modes[index]
    }

    /** Get the previous mode, return to the last mode if reached the first. */
    fun prevMode(): T {
      prev()
      return modes[index]
    }
  }
}
