package vis.rhynia.nova.common.tile.multi.process

import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.multitileentity.multiblock.casing.Glasses.chainAllGlasses
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.blocks.BlockCasings2
import kotlin.math.pow
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.api.util.ItemUtil
import vis.rhynia.nova.common.tile.base.NovaMTEBase

class NovaMTEAssemblyMatrix : NovaMTEBase<NovaMTEAssemblyMatrix> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NovaMTEAssemblyMatrix(this.mName)
  }

  companion object {
    private const val H_OFFSET = 1
    private const val V_OFFSET = 1
    private const val D_OFFSET = 0
  }

  // region Processing Logic
  private var mRecipeMode: Byte = 0 // 0-sAssemblyMatrixRecipes,1-sMicroAssemblyRecipes,2-SC recipes
  private var pModifier = 1

  override fun createProcessingLogic(): ProcessingLogic {
    return object : ProcessingLogic() {
      override fun process(): CheckRecipeResult {
        pModifier = 1
        var perfectOC = false
        val stack = controllerSlot
        if (stack != null) {
          if (ItemUtil.isAstralInfinityGem(stack)) {
            pModifier = stack.stackSize
          } else if (ItemUtil.isAstralInfinityComplex(stack)) {
            pModifier = stack.stackSize * 128
            perfectOC = true
          }
        }
        setMaxParallel(rMaxParallel)
        setSpeedBonus(rDurationModifier)
        setOverclock(if (perfectOC) 2.0 else 1.0, 2.0)
        return super.process()
      }
    }
  }

  override val rMaxParallel: Int
    get() =
        when (mRecipeMode.toInt()) {
          0 -> 4096 * pModifier
          1 -> 1024 * pModifier
          2 -> 2048 * pModifier
          else -> 1
        }

  override val rDurationModifier: Double
    get() = 0.95.pow(GTUtility.getTier(this.maxInputVoltage).toDouble())

  override fun getRecipeMap(): RecipeMap<*> =
      when (mRecipeMode.toInt()) {
        0 -> NovaRecipeMaps.integratedAssemblyRecipes
        1 -> NovaRecipeMaps.microAssemblyRecipes
        2 -> NovaRecipeMaps.superconductingFormingRecipes
        else -> RecipeMaps.nanoForgeRecipes
      }

  override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>> =
      listOf(
          NovaRecipeMaps.integratedAssemblyRecipes,
          NovaRecipeMaps.microAssemblyRecipes,
          NovaRecipeMaps.superconductingFormingRecipes)

  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    if (baseMetaTileEntity.isServerSide) {
      this.mRecipeMode = ((this.mRecipeMode + 1) % 3).toByte()
      GTUtility.sendChatToPlayer(
          aPlayer,
          StatCollector.translateToLocal("nova.AssemblyMatrix.mRecipeMode." + this.mRecipeMode))
    }
  }
  // endregion

  // region Structure
  override fun checkMachine(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aStack: ItemStack?
  ): Boolean {
    removeMaintenance()
    return checkPiece(STRUCTURE_PIECE_MAIN, H_OFFSET, V_OFFSET, D_OFFSET)
  }

  override fun construct(stackSize: ItemStack?, hintsOnly: Boolean) {
    buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, H_OFFSET, V_OFFSET, D_OFFSET)
  }

  override fun survivalConstruct(
      stackSize: ItemStack?,
      elementBudget: Int,
      env: ISurvivalBuildEnvironment?
  ): Int {
    if (mMachine) return -1
    return survivialBuildPiece(
        STRUCTURE_PIECE_MAIN,
        stackSize,
        H_OFFSET,
        V_OFFSET,
        D_OFFSET,
        elementBudget,
        env,
        false,
        true)
  }

  override fun genStructureDefinition(): IStructureDefinition<NovaMTEAssemblyMatrix> =
      StructureDefinition.builder<NovaMTEAssemblyMatrix>()
          .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
          .addElement('A', chainAllGlasses())
          .addElement('B', StructureUtility.ofBlock(GregTechAPI.sBlockCasings2, 5))
          .addElement(
              'C',
              HatchElementBuilder.builder<NovaMTEAssemblyMatrix>()
                  .atLeast(InputBus, InputHatch, Energy.or(ExoticEnergy))
                  .adder(NovaMTEAssemblyMatrix::addToMachineList)
                  .dot(1)
                  .casingIndex((GregTechAPI.sBlockCasings2 as BlockCasings2).getTextureIndex(9))
                  .buildAndChain(GregTechAPI.sBlockCasings2, 9))
          .addElement('D', StructureUtility.ofBlock(GregTechAPI.sBlockCasings3, 10))
          .addElement(
              'F',
              HatchElementBuilder.builder<NovaMTEAssemblyMatrix>()
                  .atLeast(OutputBus)
                  .adder(NovaMTEAssemblyMatrix::addToMachineList)
                  .dot(2)
                  .casingIndex((GregTechAPI.sBlockCasings2 as BlockCasings2).getTextureIndex(9))
                  .buildAndChain(GregTechAPI.sBlockCasings2, 9))
          .addElement('T', StructureUtility.ofBlock(GregTechAPI.sBlockCasings2, 9))
          .build()

  // spotless:off
  private val structureShape = arrayOf(
    arrayOf("CCC", "CDC", "CDC", "CDC", "CDC", "CDC", "FFF"),
    arrayOf("C~C", "ABA", "ABA", "ABA", "ABA", "ABA", "FFF"),
    arrayOf("CCC", "TTT", "TTT", "TTT", "TTT", "TTT", "FFF")
  )
  // spotless:on

  override val sControllerBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings2 to 9
  // endregion

  // region Tooltip
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("集成装配线 | 微加工装配线 | 超导成型机")
          .addInfo("组装矩阵的控制器")
          .addInfo("现代化的组装机构.")
          .addInfo("高效组装各类基础元件.")
          .addInfo("在控制器内放入星极和星矩来决定并行.")
          .addInfo("电压每提高1级, 加速5%(叠乘计算).")
          .addInfo(NovaValues.CommonStrings.ChangeModeByScrewdriver)
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.StructureTooComplex)
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(3, 3, 7, false)
          .addInputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NovaValues.CommonStrings.BluePrintInfo, 2)
          .addEnergyHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NovaValues.CommonStrings.NovaGigaFac)

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      it.setInteger("mRecipeMode", mRecipeMode.toInt())
      it.setInteger("pModifier", pModifier)
    }
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      mRecipeMode = it.getInteger("mRecipeMode").toByte()
      pModifier = it.getInteger("pModifier")
    }
    super.loadNBTData(aNBT)
  }
  // endregion
}
