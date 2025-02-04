package vis.rhynia.nova.common.tile.multi.process

import bartworks.API.recipe.BartWorksRecipeMaps
import gregtech.api.GregTechAPI
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTUtility
import gregtech.api.util.MultiblockTooltipBuilder
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.common.tile.base.NovaMTECubeBase

class NovaMTEDenseEndpoint : NovaMTECubeBase<NovaMTEDenseEndpoint> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NovaMTEDenseEndpoint(this.mName)
  }

  // region Process
  private var pRecipeMode: Byte = 0

  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    if (baseMetaTileEntity.isServerSide) {
      this.pRecipeMode = ((this.pRecipeMode + 1) % 3).toByte()
      GTUtility.sendChatToPlayer(
          aPlayer,
          StatCollector.translateToLocal("nova.DenseEndpoint.pRecipeMode." + this.pRecipeMode))
    }
  }

  override fun getRecipeMap(): RecipeMap<*>? {
    return when (pRecipeMode.toInt()) {
      0 -> BartWorksRecipeMaps.electricImplosionCompressorRecipes
      1 -> RecipeMaps.nanoForgeRecipes
      2 -> NovaRecipeMaps.quarkRefactoringRecipes
      else -> RecipeMaps.assemblerRecipes
    }
  }

  override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>?> {
    return listOf<RecipeMap<*>?>(
        BartWorksRecipeMaps.electricImplosionCompressorRecipes,
        RecipeMaps.nanoForgeRecipes,
        NovaRecipeMaps.quarkRefactoringRecipes)
  }

  override fun getRecipeCatalystPriority(): Int = -10

  override val rPerfectOverclock: Boolean
    get() = true

  override val rMaxParallel: Int
    get() {
      val parallel = 128L * maxInputAmps
      return if (parallel <= Int.Companion.MAX_VALUE) {
        parallel.toInt()
      } else {
        Int.Companion.MAX_VALUE
      }
    }

  override fun setProcessingLogicPower(logic: ProcessingLogic) {
    if (pRecipeMode == 1.toByte()) {
      // Nano Forge use full EU import
      logic.setAvailableVoltage(maxInputEu)
      logic.setAvailableAmperage(1)
      logic.setAmperageOC(false)
    } else {
      super.setProcessingLogicPower(logic)
    }
  }
  // endregion

  // region Structure
  override val sCasingBlock: Block
    get() = GregTechAPI.sBlockCasings8

  override val sCasingBlockMeta: Int
    get() = 10
  // endregion

  // region Info
  override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("电动聚爆压缩机 | 纳米锻炉 | 夸克重构机")
          .addInfo("致密极点的控制器")
          .addInfo("压缩到奇点, 再释放出来?")
          .addInfo("最大并行为128*最大输入电流.")
          .addInfo("执行无损超频.")
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addInputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addEnergyHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NovaValues.CommonStrings.NovaNuclear)

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    aNBT?.setByte("pRecipeMode", pRecipeMode)
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    pRecipeMode = aNBT?.getByte("pRecipeMode") ?: 0
    super.loadNBTData(aNBT)
  }
  // endregion
}
