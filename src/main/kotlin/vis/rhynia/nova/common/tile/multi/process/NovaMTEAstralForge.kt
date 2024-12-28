package vis.rhynia.nova.common.tile.multi.process

import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility
import com.gtnewhorizon.structurelib.structure.StructureUtility.isAir
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain
import com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass
import fox.spiteful.avaritia.blocks.LudicrousBlocks
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.Textures
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.recipe.RecipeMap
import gregtech.api.render.TextureFactory
import gregtech.api.util.GTStructureUtility.ofFrame
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import gregtech.api.util.MultiblockTooltipBuilder
import kotlin.math.min
import kotlin.math.pow
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.common.util.ForgeDirection
import tectech.thing.casing.TTCasingsContainer
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.common.tile.base.NovaMTEBase

class NovaMTEAstralForge : NovaMTEBase<NovaMTEAstralForge> {
  constructor(aId: Int, aName: String, aNameRegional: String) : super(aId, aName, aNameRegional)

  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity): IMetaTileEntity =
      NovaMTEAstralForge(mName)

  companion object {
    private const val H_OFFSET = 2
    private const val V_OFFSET = 1
    private const val D_OFFSET = 0
  }

  private var uStableField = 0

  override val rPerfectOverclock: Boolean
    get() = uStableField >= 7

  override val rDurationModifier: Double
    get() =
        min(1.0, (1.0 - 0.02 * (uStableField + 1)).pow(GTUtility.getTier(maxInputVoltage).toInt()))

  override fun getRecipeMap(): RecipeMap<*> = NovaRecipeMaps.astralForgeRecipes

  override fun checkMachine(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aStack: ItemStack?
  ): Boolean {
    removeMaintenance()
    uStableField = -1
    return checkPiece(STRUCTURE_PIECE_MAIN, H_OFFSET, V_OFFSET, D_OFFSET)
  }

  override fun construct(stackSize: ItemStack?, hintsOnly: Boolean) {
    buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, H_OFFSET, V_OFFSET, D_OFFSET)
  }

  override fun survivalConstruct(
      stackSize: ItemStack,
      elementBudget: Int,
      env: ISurvivalBuildEnvironment
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

  private var structureDefinition: IStructureDefinition<NovaMTEAstralForge>? = null

  // spotless:off
  @Suppress("SpellCheckingInspection")
  private val structureShape = arrayOf(
    arrayOf("  E  ", "  G  ", "EGFGE", "  G  ", "  E  "),
    arrayOf(" D~D ", "D   D", "B C B", "D   D", " DBD "),
    arrayOf(" BBB ", "BAAAB", "BAAAB", "BAAAB", " BBB ")
  )
  // spotless:on

  private fun generateStructureDefinition(): IStructureDefinition<NovaMTEAstralForge> =
      StructureDefinition.builder<NovaMTEAstralForge>()
          .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
          .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 9))
          .addElement(
              'B',
              HatchElementBuilder.builder<NovaMTEAstralForge>()
                  .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                  .adder(NovaMTEAstralForge::addMachineListCompatible)
                  .dot(1)
                  .casingIndex(183)
                  .buildAndChain(GregTechAPI.sBlockCasings8, 7))
          .addElement(
              'C',
              if (Mods.Avaritia.isModLoaded) ofBlock(LudicrousBlocks.resource_block, 1)
              else ofFrame(Materials.Infinity))
          .addElement('D', ofFrame(Materials.Neutronium))
          .addElement(
              'E',
              HatchElementBuilder.builder<NovaMTEAstralForge>()
                  .atLeast(Energy.or(ExoticEnergy))
                  .adder(NovaMTEAstralForge::addMachineListCompatible)
                  .dot(2)
                  .casingIndex(183)
                  .buildAndChain(GregTechAPI.sBlockCasings8, 7))
          .addElement(
              'F',
              ofChain(
                  onElementPass({ t -> t.uStableField = -1 }, isAir()),
                  ofBlocksTiered(
                      { block, meta ->
                        if (block == TTCasingsContainer.StabilisationFieldGenerators) meta else null
                      },
                      listOf(
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 0),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 1),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 2),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 3),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 4),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 5),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 6),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 7),
                          org.apache.commons.lang3.tuple.Pair.of(
                              TTCasingsContainer.StabilisationFieldGenerators, 8)),
                      -1,
                      { t, meta ->
                        if (meta != null) {
                          t.uStableField = meta
                        }
                      },
                      { t -> t.uStableField })))
          .addElement('G', ofFrame(Materials.Infinity))
          .build()

  override fun getStructureDefinition(): IStructureDefinition<NovaMTEAstralForge> {
    if (structureDefinition == null) {
      structureDefinition = generateStructureDefinition()
    }
    return structureDefinition!!
  }

  override fun getTexture(
      baseMetaTileEntity: IGregTechTileEntity?,
      side: ForgeDirection?,
      facing: ForgeDirection?,
      colorIndex: Int,
      active: Boolean,
      redstoneLevel: Boolean
  ): Array<out ITexture?>? {
    if (side != facing)
        return arrayOf(
            Textures.BlockIcons.getCasingTextureForId(
                GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 7)))
    if (active)
        return arrayOf(
            Textures.BlockIcons.getCasingTextureForId(
                GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 7)),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build())
    return arrayOf(
        Textures.BlockIcons.getCasingTextureForId(
            GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 7)),
        TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
            .extFacing()
            .glow()
            .build())
  }

  override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("星光聚能器")
          .addInfo("星辉锻造台的控制器")
          .addInfo("${EnumChatFormatting.RED}不要试图去理解原理.")
          .addInfo("使用星光将平凡转化为奇迹.")
          .addInfo("没有在指定位置安装稳定器时，最大并行为64.")
          .addInfo("安装稳定器后, 最大并行=64*2^等级.")
          .addInfo("在太初及以上等级时, 启用无损超频.")
          .addInfo("且电压每提高1级, 额外降低(等级*2)%配方耗时(叠乘).")
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.StructureTooComplex)
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(5, 3, 5, false)
          .addInputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addEnergyHatch(NovaValues.CommonStrings.BluePrintInfo, 2)
          .toolTipFinisher(NovaValues.CommonStrings.NovaMagical)

  override fun getInfoData(): Array<String> {
    val original = super.getInfoData()
    val originalSize = original.size
    return arrayOfNulls<String>(originalSize + 1).run {
      System.arraycopy(original, 0, this, 0, originalSize)
      this[originalSize] =
          "${EnumChatFormatting.AQUA}Stable Field: ${EnumChatFormatting.GOLD}${uStableField}"
      this.filterNotNull().toTypedArray()
    }
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
    aNBT?.setInteger("uStableField", uStableField)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
    uStableField = aNBT?.getInteger("uStableField") ?: -1
  }
}
