package vis.rhynia.nova.common.tile.multi.process

import com.google.common.collect.ImmutableList
import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.ITierConverter
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.HeatingCoilLevel
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.render.TextureFactory
import gregtech.api.util.GTStructureUtility.ofCoil
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.blocks.BlockCasings1
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.math.min
import kotlin.math.pow
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.BOLD
import net.minecraft.util.EnumChatFormatting.GOLD
import net.minecraft.util.EnumChatFormatting.GRAY
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import org.apache.commons.lang3.tuple.Pair as ApPair
import tectech.thing.CustomItemList
import tectech.thing.casing.TTCasingsContainer
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.api.util.MathUtil
import vis.rhynia.nova.common.tile.base.NovaMTEBase

class NovaMTEAtomMacro : NovaMTEBase<NovaMTEAtomMacro> {
  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NovaMTEAtomMacro(this.mName)
  }

  companion object {
    private const val H_OFFSET = 7
    private const val V_OFFSET = 0
    private const val D_OFFSET = 7
  }

  // region Processing Logic
  private var mRecipeMode: Byte = 0 // 0-sUltimateHeaterRecipes,1-sTranscendentReactorRecipes
  private var mCoilLevel: HeatingCoilLevel? = null

  private var uSpacetimeCompressionCount = 0
  private var uTimeAccelerationField = 0
  private var uStarArrayCount = 0

  override fun createProcessingLogic(): ProcessingLogic {
    return object : ProcessingLogic() {
      override fun process(): CheckRecipeResult {
        uStarArrayCount = 0
        val u = controllerSlot
        if (u != null && u.isItemEqual(CustomItemList.astralArrayFabricator.get(1))) {
          uStarArrayCount += u.stackSize
        }
        setEuModifier(rEuModifier)
        setMaxParallel(rMaxParallel)
        setSpeedBonus(rDurationModifier)
        setOverclock(if (rPerfectOverclock) 2.0 else 1.0, 2.0)
        return super.process()
      }
    }
  }

  override val rPerfectOverclock: Boolean
    get() = (mCoilLevel?.tier ?: -1) > 11

  override val rMaxParallel: Int
    get() = (1 + uStarArrayCount) * (1 + uSpacetimeCompressionCount)

  override val rDurationModifier: Double
    get() =
        min(
            0.1,
            (0.97.pow(mCoilLevel?.tier?.toDouble() ?: 0.0) *
                0.93.pow((uTimeAccelerationField + 1).toDouble())))

  public override val rEuModifier: Double
    get() =
        (1.0 - MathUtil.clampVal((0.0005 * uSpacetimeCompressionCount), 0.0, 0.9)) /
            (1 + uStarArrayCount)

  override fun getRecipeMap(): RecipeMap<*>? {
    return when (mRecipeMode.toInt()) {
      0 -> NovaRecipeMaps.thermonuclearControlRecipes
      1 -> NovaRecipeMaps.transcendentReactorRecipes
      2 -> RecipeMaps.fusionRecipes
      else -> RecipeMaps.nanoForgeRecipes
    }
  }

  override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>?> {
    return listOf<RecipeMap<*>?>(
        NovaRecipeMaps.thermonuclearControlRecipes,
        NovaRecipeMaps.transcendentReactorRecipes,
        RecipeMaps.fusionRecipes)
  }

  override fun getRecipeCatalystPriority(): Int = -20

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
          StatCollector.translateToLocal("append.UltimateHeater.mRecipeMode." + this.mRecipeMode))
    }
  }
  // endregion

  // region Structure
  override fun isFlipChangeAllowed(): Boolean = false

  override fun isRotationChangeAllowed(): Boolean = false

  override fun checkMachine(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aStack: ItemStack?
  ): Boolean {
    removeMaintenance()
    this.uSpacetimeCompressionCount = 0
    this.uTimeAccelerationField = -1
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

  override fun genStructureDefinition(): IStructureDefinition<NovaMTEAtomMacro> =
      StructureDefinition.builder<NovaMTEAtomMacro>()
          .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
          .addElement('A', StructureUtility.ofBlock(GregTechAPI.sBlockCasings1, 12))
          .addElement('B', StructureUtility.ofBlock(GregTechAPI.sBlockCasings1, 15))
          .addElement(
              'C',
              StructureUtility.ofChain(
                  StructureUtility.onElementPass(
                      Consumer { t: NovaMTEAtomMacro -> t.uTimeAccelerationField = -1 },
                      StructureUtility.ofBlock(GregTechAPI.sBlockCasings1, 14)),
                  StructureUtility.ofBlocksTiered(
                      ITierConverter { block: Block?, meta: Int ->
                        if (block === TTCasingsContainer.TimeAccelerationFieldGenerator) meta
                        else null
                      },
                      ImmutableList.of<ApPair<Block, Int>>(
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 0),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 1),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 2),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 3),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 4),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 5),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 6),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 7),
                          ApPair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)),
                      -1,
                      { t: NovaMTEAtomMacro, meta: Int -> t.uTimeAccelerationField = meta },
                      { it.uTimeAccelerationField })))
          .addElement(
              'D',
              StructureUtility.withChannel<NovaMTEAtomMacro>(
                  "coil",
                  ofCoil(
                      BiConsumer { obj: NovaMTEAtomMacro, aCoilLevel: HeatingCoilLevel ->
                        obj.setCoilLevel(aCoilLevel)
                      }) { it.getCoilLevel() }))
          .addElement(
              'E',
              StructureUtility.ofChain(
                  StructureUtility.ofBlock(GregTechAPI.sBlockCasings1, 14),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 1 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 0)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 2 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 1)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 4 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 8 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 3)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 16 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 4)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 32 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 5)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 64 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 6)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 128 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 7)),
                  StructureUtility.onElementPass(
                      { it.uSpacetimeCompressionCount += 256 },
                      StructureUtility.ofBlock(
                          TTCasingsContainer.SpacetimeCompressionFieldGenerators, 8))))
          .addElement('F', StructureUtility.ofBlock(GregTechAPI.sBlockCasings1, 14))
          .addElement(
              'G',
              HatchElementBuilder.builder<NovaMTEAtomMacro>()
                  .atLeast(InputBus, InputHatch)
                  .adder(NovaMTEAtomMacro::addToMachineList)
                  .dot(1)
                  .casingIndex((GregTechAPI.sBlockCasings1 as BlockCasings1).getTextureIndex(12))
                  .buildAndChain(GregTechAPI.sBlockCasings1, 12))
          .addElement(
              'H',
              HatchElementBuilder.builder<NovaMTEAtomMacro>()
                  .atLeast(OutputBus, OutputHatch)
                  .adder(NovaMTEAtomMacro::addToMachineList)
                  .dot(2)
                  .casingIndex((GregTechAPI.sBlockCasings1 as BlockCasings1).getTextureIndex(12))
                  .buildAndChain(GregTechAPI.sBlockCasings1, 12))
          .addElement(
              'I',
              HatchElementBuilder.builder<NovaMTEAtomMacro>()
                  .atLeast(Energy.or(ExoticEnergy))
                  .adder(NovaMTEAtomMacro::addToMachineList)
                  .dot(3)
                  .casingIndex((GregTechAPI.sBlockCasings1 as BlockCasings1).getTextureIndex(12))
                  .buildAndChain(GregTechAPI.sBlockCasings1, 12))
          .build()

  // spotless: off
  @Suppress("SpellCheckingInspection")
  private val structureShape =
      arrayOf(
          arrayOf(
              "               ",
              "      GGG      ",
              "    AA E AA    ",
              "   I   E   I   ",
              "  A    E    A  ",
              "  A    E    A  ",
              " G     E     G ",
              " GEEEEE~EEEEEG ",
              " G     E     G ",
              "  A    E    A  ",
              "  A    E    A  ",
              "   I   E   I   ",
              "    AA E AA    ",
              "      GGG      ",
              "               "),
          arrayOf(
              "      HHH      ",
              "    AABBBAA    ",
              "   ABBADABBA   ",
              "  ABAA D AABA  ",
              " ABA   D   ABA ",
              " ABA  DDD  ABA ",
              "HBA  D D D  ABH",
              "HBDDDDDCDDDDDBH",
              "HBA  D D D  ABH",
              " ABA  DDD  ABA ",
              " ABA   D   ABA ",
              "  ABAA D AABA  ",
              "   ABBADABBA   ",
              "    AABBBAA    ",
              "      HHH      "),
          arrayOf(
              "               ",
              "      GGG      ",
              "    AA E AA    ",
              "   I   E   I   ",
              "  A    E    A  ",
              "  A    E    A  ",
              " G     E     G ",
              " AEEEEEFEEEEEA ",
              " G     E     G ",
              "  A    E    A  ",
              "  A    E    A  ",
              "   I   E   I   ",
              "    AA E AA    ",
              "      GGG      ",
              "               "))
  // spotless: on

  private val textureIndex = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 12)

  override val sControllerBlock: kotlin.Pair<Block, Int>
    get() = null!! // Special case

  override fun getTexture(
      baseMetaTileEntity: IGregTechTileEntity,
      sideDirection: ForgeDirection,
      facingDirection: ForgeDirection,
      colorIndex: Int,
      active: Boolean,
      redstoneLevel: Boolean
  ): Array<ITexture> {
    return if (sideDirection != ForgeDirection.UP)
        arrayOf(Textures.BlockIcons.getCasingTextureForId(textureIndex))
    else if (active)
        arrayOf(
            Textures.BlockIcons.getCasingTextureForId(textureIndex),
            TextureFactory.builder().addIcon(OVERLAY_FUSION1).extFacing().build(),
            TextureFactory.builder().addIcon(OVERLAY_FUSION1_GLOW).extFacing().glow().build())
    else
        arrayOf(
            Textures.BlockIcons.getCasingTextureForId(textureIndex),
            TextureFactory.builder().addIcon(OVERLAY_SCREEN).extFacing().build(),
            TextureFactory.builder().addIcon(OVERLAY_SCREEN).extFacing().glow().build())
  }
  // endregion

  // region Info
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("热核控制场 | 超维度反应器 | 聚变反应堆")
          .addInfo("粒子宏的控制器")
          .addInfo("用纯粹的能量扭曲物质的存在.")
          .addInfo("每个时空压缩场提供2^(等级-1)的额外并行.")
          .addInfo("并提供(0.05*等级)%的功耗减免, 最高90%.")
          .addInfo("安装时间加速场后, 每级减少10%配方耗时(叠乘).")
          .addInfo("线圈每提高1级, 同样减少10%配方耗时(叠乘).")
          .addInfo("时间缩减的极限是原配方时间的10%.")
          .addInfo("线圈等级在海珀珍及以上时，解锁无损超频.")
          .addInfo("可以在控制器中放入${BOLD}星阵${GRAY}来倍增总并行，同时不增加耗能.")
          .addInfo(NovaValues.CommonStrings.ChangeModeByScrewdriver)
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.StructureTooComplex)
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(15, 3, 15, false)
          .addInputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addEnergyHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NovaValues.CommonStrings.NovaNuclear)

  override fun getInfoDataExtra(): Array<String> =
      arrayOf(
          "${AQUA}时空压缩: ${GOLD}${GTUtility.formatNumbers(uSpacetimeCompressionCount.toLong())}",
          "${AQUA}时间加速: ${GOLD}${GTUtility.formatNumbers(uTimeAccelerationField.toLong())}",
          "${AQUA}星阵数量: ${GOLD}${GTUtility.formatNumbers(uStarArrayCount.toLong())}")
  override fun saveNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      it.setInteger("mRecipeMode", mRecipeMode.toInt())
      it.setInteger("uSpacetimeCompressionField", uSpacetimeCompressionCount)
      it.setInteger("uTimeAccelerationField", uTimeAccelerationField)
      it.setInteger("uStarArrayCount", uStarArrayCount)
    }
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      mRecipeMode = it.getInteger("mRecipeMode").toByte()
      uSpacetimeCompressionCount = it.getInteger("uSpacetimeCompressionField")
      uTimeAccelerationField = it.getInteger("uTimeAccelerationField")
      uStarArrayCount = it.getInteger("uStarArrayCount")
    }
    super.loadNBTData(aNBT)
  }
  // endregion

  // region Selector
  fun setCoilLevel(aCoilLevel: HeatingCoilLevel) {
    this.mCoilLevel = aCoilLevel
  }

  fun getCoilLevel(): HeatingCoilLevel {
    return this.mCoilLevel ?: HeatingCoilLevel.None
  }
  // endregion
}
