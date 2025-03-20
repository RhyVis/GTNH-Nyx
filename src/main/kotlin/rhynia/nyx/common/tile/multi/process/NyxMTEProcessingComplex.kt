package rhynia.nyx.common.tile.multi.process

import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.util.MultiblockTooltipBuilder
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.GOLD
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import rhynia.nyx.api.enums.CheckRecipeResultRef
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.process.OverclockType
import rhynia.nyx.api.util.ItemUtil
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.tile.base.NyxMTECubeBase

class NyxMTEProcessingComplex : NyxMTECubeBase<NyxMTEProcessingComplex> {
  constructor(aId: Int, aName: String, aNameRegional: String) : super(aId, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?) = NyxMTEProcessingComplex(mName)

  // region Process

  private var pMachineStack: ItemStack? = null
  private var pRunParallel = 1
  private var pOrdinal = 1
  private var pCalibrationSet = false
  private var pDisplayName = "NONE"

  override fun createProcessingLogic(): ProcessingLogic =
      object : ProcessingLogic() {
        override fun process(): CheckRecipeResult {
          resetState()
          pMachineStack = controllerSlot ?: return CheckRecipeResultRef.NO_RECIPE_MAP_SET
          pRunParallel = pMachineStack!!.stackSize * 16
          calculateState()

          getRecipeMapFromStack(pMachineStack!!)?.let { setRecipeMap(it) }
              ?: return CheckRecipeResultRef.NO_RECIPE_MAP_SET
          setEuModifier(rEuModifier)
          setMaxParallel(pRunParallel)
          setSpeedBonus(rDurationModifier)
          setOverclock(rOverclockType.timeDec, rOverclockType.powerInc)

          return super.process()
        }
      }

  override val rOverclockType: OverclockType
    get() = OverclockType.Perfect

  override val rMaxParallel: Int
    get() = pRunParallel

  private fun getRecipeMapFromStack(stack: ItemStack): RecipeMap<*>? {
    val mapArray = ProcessReference.ProcessingMap[stack.unlocalizedName] ?: return null
    val mapArraySize = mapArray.size
    if (mapArraySize == 1) return mapArray[0]
    mapArray[pOrdinal.coerceIn(1, mapArraySize) - 1].let {
      pDisplayName = StatCollector.translateToLocal(it.unlocalizedName)
      return it
    }
  }

  private fun calculateState() {
    mInputBusses.forEach { bus ->
      bus.realInventory.forEach { itemStack ->
        itemStack?.let {
          if (!pCalibrationSet && ItemUtil.isCalibration(it)) {
            pOrdinal = it.stackSize.coerceIn(1, 24)
            pCalibrationSet = true
          }
          if (ItemUtil.isAstralInfinityComplex(it)) {
            pRunParallel += it.stackSize * 2048
          }
        }
      }
    }
  }

  private fun resetState() {
    pMachineStack = null
    pRunParallel = 1
    pOrdinal = 1
    pCalibrationSet = false
    pDisplayName = "NONE"
  }

  internal object ProcessReference {
    val ProcessingMap = mutableMapOf<String, Array<RecipeMap<*>>>()

    fun generateMap() {
      ProcessType.entries.forEach { processType ->
        val tempStackName = processType.pMachineStack.unlocalizedName
        if (tempStackName in ProcessingMap) {
          ProcessingMap[tempStackName] = ProcessingMap[tempStackName]!! + processType.pRecipeMap
        } else {
          ProcessingMap[tempStackName] = arrayOf(processType.pRecipeMap)
        }
      }
    }
  }

  @Suppress("SpellCheckingInspection")
  internal enum class ProcessType(
      val pRecipeMap: RecipeMap<*>,
      val pMachineStack: ItemStack,
  ) {
    // spotless:off
        // GT++
        AlloySmelter(
            GTPPRecipeMaps.alloyBlastSmelterRecipes,
            GregtechItemList.Industrial_AlloySmelter.get(1)
        ),
        ChemicalDehydrator(
            GTPPRecipeMaps.chemicalDehydratorNonCellRecipes,
            GregtechItemList.Controller_Vacuum_Furnace.get(1)
        ),
        VacuumFurnace(
            GTPPRecipeMaps.vacuumFurnaceRecipes,
            GregtechItemList.Controller_Vacuum_Furnace.get(1)
        ),
        ArcFurnace(
            RecipeMaps.arcFurnaceRecipes,
            GregtechItemList.Industrial_Arc_Furnace.get(1)
        ),
        PlasmaArcFurnace(
            RecipeMaps.plasmaArcFurnaceRecipes,
            GregtechItemList.Industrial_Arc_Furnace.get(1)
        ),
        FluidHeater(
            RecipeMaps.fluidHeaterRecipes,
            GregtechItemList.Controller_IndustrialFluidHeater.get(1)
        ),
        MolecularTransformer(
            GTPPRecipeMaps.molecularTransformerRecipes,
            GregtechItemList.Controller_MolecularTransformer.get(1)
        ),
        OreWash(
            RecipeMaps.oreWasherRecipes,
            GregtechItemList.Industrial_WashPlant.get(1)
        ),
        SimpleWash(
            GTPPRecipeMaps.simpleWasherRecipes,
            GregtechItemList.Industrial_WashPlant.get(1)
        ),
        ChemicalWash(
            RecipeMaps.chemicalBathRecipes,
            GregtechItemList.Industrial_WashPlant.get(1)
        ),

        // GT5U
        DTPF(RecipeMaps.plasmaForgeRecipes, NyxItemList.AssemblyDTPF.get(1))
        // spotless:on
  }

  // endregion

  // region Structure

  override val sCasingBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings8 to 7

  override val sCasingHatch: Array<IHatchElement<in NyxMTEProcessingComplex>>
    get() = arrayOf(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))

  // endregion

  // region Information

  override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("处理矩阵")
          .addInfo("处理矩阵的控制器")
          .addInfo("将多方块机器压缩到这个矩阵中来.")
          .addInfo("在控制器放入机器组或机器.")
          .addInfo("在一个机器组对应多个配方时, 用标定指示的数量决定配方.")
          .addInfo("控制器中每个机器组对应16并行, 放入星矩等效为2048并行.")
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus()
          .addInputHatch()
          .addOutputBus()
          .addOutputHatch()
          .addEnergyHatch()
          .toolTipFinisher(NyxValues.CommonStrings.NyxGigaFac)

  override fun getInfoDataExtra(): Array<String> = arrayOf("${AQUA}执行配方: ${GOLD}$pDisplayName")

  override fun getWailaBody(
      itemStack: ItemStack?,
      currentTip: List<String?>?,
      accessor: IWailaDataAccessor?,
      config: IWailaConfigHandler?
  ) {
    super.getWailaBody(itemStack, currentTip, accessor, config)
    val name = accessor?.nbtData?.getString("pDisplayName") ?: return
    if (name.isNotEmpty() && name != "NONE") currentTip?.plus("${AQUA}执行配方: ${GOLD}$name")
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
    if (baseMetaTileEntity != null) tag?.setString("pDisplayName", pDisplayName)
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
    if (aNBT == null) return
    aNBT.setString("pDisplayName", pDisplayName)
    aNBT.setInteger("pRunParallel", pRunParallel)
    aNBT.setInteger("pOrdinal", pOrdinal)
    aNBT.setBoolean("pCalibrationSet", pCalibrationSet)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
    if (aNBT == null) return
    pDisplayName = aNBT.getString("pDisplayName")
    pRunParallel = aNBT.getInteger("pRunParallel")
    pOrdinal = aNBT.getInteger("pOrdinal")
    pCalibrationSet = aNBT.getBoolean("pCalibrationSet")
  }

  // endregion
}
