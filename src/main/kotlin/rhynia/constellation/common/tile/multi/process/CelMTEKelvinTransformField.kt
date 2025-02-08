package rhynia.constellation.common.tile.multi.process

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
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.blocks.BlockCasings2
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import kotlin.math.pow
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting.DARK_RED
import net.minecraft.util.EnumChatFormatting.RED
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.common.tile.base.CelMTEBase

class CelMTEKelvinTransformField : CelMTEBase<CelMTEKelvinTransformField> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return CelMTEKelvinTransformField(this.mName)
  }

  companion object {
    private const val H_OFFSET = 1
    private const val V_OFFSET = 1
    private const val D_OFFSET = 0
  }
  // endregion

  // region Processing Logic
  private var mRecipeMode: Byte = 0 // 0-sVacuumRecipes,1-?

  override val rMaxParallel: Int
    get() =
        if (mRecipeMode.toInt() == 0) 2048 else 64 + 16 * GTUtility.getTier(this.maxInputVoltage)

  override val rDurationModifier: Double
    get() =
        if (mRecipeMode.toInt() == 0) 0.95.pow(GTUtility.getTier(this.maxInputVoltage).toDouble())
        else 1.0

  override fun getRecipeMap(): RecipeMap<*>? =
      if (mRecipeMode.toInt() == 0) RecipeMaps.vacuumFreezerRecipes
      else GTPPRecipeMaps.advancedFreezerRecipes

  override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>> =
      listOf(RecipeMaps.vacuumFreezerRecipes, GTPPRecipeMaps.advancedFreezerRecipes)

  override fun getRecipeCatalystPriority(): Int = -10

  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    if (baseMetaTileEntity.isServerSide) {
      this.mRecipeMode = ((this.mRecipeMode + 1) % 2).toByte()
      GTUtility.sendChatToPlayer(
          aPlayer,
          StatCollector.translateToLocal(
              "constellation.KelvinTransformField.pRecipeMode." + this.mRecipeMode))
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
    this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, H_OFFSET, V_OFFSET, D_OFFSET)
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

  override fun genStructureDefinition(): IStructureDefinition<CelMTEKelvinTransformField> =
      StructureDefinition.builder<CelMTEKelvinTransformField>()
          .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
          .addElement('B', StructureUtility.ofBlock(GregTechAPI.sBlockCasings4, 7))
          .addElement(
              'C',
              HatchElementBuilder.builder<CelMTEKelvinTransformField>()
                  .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))
                  .adder(CelMTEKelvinTransformField::addToMachineList)
                  .dot(1)
                  .casingIndex((GregTechAPI.sBlockCasings2 as BlockCasings2).getTextureIndex(1))
                  .buildAndChain(GregTechAPI.sBlockCasings2, 1))
          .build()

  // spotless:off
  private val structureShape = arrayOf(
    arrayOf("CCC", "CCC", "CCC"),
    arrayOf("C~C", "CBC", "CCC"),
    arrayOf("CCC", "CCC", "CCC")
  )
  // spotless:on

  override val sControllerBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings2 to 1

  override val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_FRONT_VACUUM_FREEZER to OVERLAY_FRONT_VACUUM_FREEZER_GLOW

  override val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE to OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW

  // endregion

  // region Overrides
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("真空冷冻机 | 热动力学解析")
          .addInfo("开尔文变换场的控制器")
          .addInfo("${RED}万物都有${DARK_RED}终结${RED}，我不过是定义了它的到来.")
          .addInfo("指挥粒子做它该做的热运动.")
          .addInfo("真空冷冻机模式下，最大并行为2048.")
          .addInfo("且电压每提高1级, 降低5%配方耗时(叠乘计算).")
          .addInfo("热动力学解析模式下，基础最大并行为64.")
          .addInfo("且电压每提高1级, 增加16并行.")
          .addInfo(CelValues.CommonStrings.ChangeModeByScrewdriver)
          .addSeparator()
          .addInfo(CelValues.CommonStrings.StructureTooComplex)
          .addInfo(CelValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus(CelValues.CommonStrings.BluePrintInfo, 1)
          .addInputHatch(CelValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(CelValues.CommonStrings.BluePrintInfo, 1)
          .addOutputHatch(CelValues.CommonStrings.BluePrintInfo, 1)
          .addEnergyHatch(CelValues.CommonStrings.BluePrintInfo, 2)
          .toolTipFinisher(CelValues.CommonStrings.CelNuclear)

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    aNBT?.setInteger("mRecipeMode", mRecipeMode.toInt())
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    mRecipeMode = aNBT?.getInteger("mRecipeMode")?.toByte() ?: 0
    super.loadNBTData(aNBT)
  }
  // endregion
}
