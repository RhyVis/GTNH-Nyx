package rhynia.nyx.common.tile.multi.process

import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain
import com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass
import com.gtnewhorizon.structurelib.structure.StructureUtility.transpose
import com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel
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
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.process.OverclockType
import rhynia.nyx.api.recipe.NyxRecipeMaps
import rhynia.nyx.api.util.MathUtil
import rhynia.nyx.common.tile.base.NyxMTEBase
import tectech.thing.CustomItemList
import tectech.thing.casing.TTCasingsContainer.SpacetimeCompressionFieldGenerators
import tectech.thing.casing.TTCasingsContainer.TimeAccelerationFieldGenerator
import java.util.function.BiConsumer
import kotlin.math.min
import kotlin.math.pow
import org.apache.commons.lang3.tuple.Pair as ApPair

class NyxMTEAtomMacro : NyxMTEBase<NyxMTEAtomMacro> {
    constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
    constructor(aName: String) : super(aName)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity = NyxMTEAtomMacro(this.mName)

    companion object {
        private const val H_OFFSET = 7
        private const val V_OFFSET = 0
        private const val D_OFFSET = 7
    }

    // region Processing Logic
    private var mCoilLevel: HeatingCoilLevel? = null

    private var uSpacetimeCompressionCount = 0
    private var uTimeAccelerationField = 0
    private var uStarArrayCount = 0

    private var pRecipeMode =
        ModeContainer.of(
            NyxRecipeMaps.thermonuclearControlRecipes,
            NyxRecipeMaps.transcendentReactorRecipes,
            RecipeMaps.fusionRecipes,
        )

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
                setOverclock(rOverclockType.timeDec, rOverclockType.powerInc)
                return super.process()
            }
        }
    }

    override val rOverclockType: OverclockType
        get() = if ((mCoilLevel?.tier ?: -1) > 11) OverclockType.Perfect else OverclockType.Normal

    override val rMaxParallel: Int
        get() = (1 + uStarArrayCount) * (1 + uSpacetimeCompressionCount)

    override val rDurationModifier: Double
        get() =
            min(
                0.1,
                (
                    0.97.pow(mCoilLevel?.tier?.toDouble() ?: 0.0) *
                        0.93.pow((uTimeAccelerationField + 1).toDouble())
                ),
            )

    public override val rEuModifier: Double
        get() =
            (1.0 - MathUtil.clampVal((0.0005 * uSpacetimeCompressionCount), 0.0, 0.9)) /
                (1 + uStarArrayCount)

    override fun getRecipeMap(): RecipeMap<*>? = pRecipeMode.current

    override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>?> = pRecipeMode.all

    override fun getRecipeCatalystPriority(): Int = -20

    override fun onScrewdriverRightClick(
        side: ForgeDirection?,
        aPlayer: EntityPlayer?,
        aX: Float,
        aY: Float,
        aZ: Float,
    ) {
        if (baseMetaTileEntity.isServerSide) {
            pRecipeMode.next()
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("nyx.AtomMacro.pRecipeMode.${pRecipeMode.index}"),
            )
        }
    }
    // endregion

    // region Structure
    override fun isFlipChangeAllowed(): Boolean = false

    override fun isRotationChangeAllowed(): Boolean = false

    override fun checkMachine(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aStack: ItemStack?,
    ): Boolean {
        removeMaintenance()
        this.uSpacetimeCompressionCount = 0
        this.uTimeAccelerationField = -1
        return checkPiece(STRUCTURE_PIECE_MAIN, H_OFFSET, V_OFFSET, D_OFFSET)
    }

    override fun construct(
        stackSize: ItemStack?,
        hintsOnly: Boolean,
    ) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, H_OFFSET, V_OFFSET, D_OFFSET)
    }

    override fun survivalConstruct(
        stackSize: ItemStack?,
        elementBudget: Int,
        env: ISurvivalBuildEnvironment?,
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
            true,
        )
    }

    override fun genStructureDefinition(): IStructureDefinition<NyxMTEAtomMacro> =
        StructureDefinition
            .builder<NyxMTEAtomMacro>()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(structureShape))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 12))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 15))
            .addElement(
                'C',
                ofChain(
                    onElementPass(
                        { t -> t.uTimeAccelerationField = -1 },
                        ofBlock(GregTechAPI.sBlockCasings1, 14),
                    ),
                    ofBlocksTiered(
                        { block, meta ->
                            if (block === TimeAccelerationFieldGenerator) meta else null
                        },
                        listOf(
                            ApPair.of(TimeAccelerationFieldGenerator, 0),
                            ApPair.of(TimeAccelerationFieldGenerator, 1),
                            ApPair.of(TimeAccelerationFieldGenerator, 2),
                            ApPair.of(TimeAccelerationFieldGenerator, 3),
                            ApPair.of(TimeAccelerationFieldGenerator, 4),
                            ApPair.of(TimeAccelerationFieldGenerator, 5),
                            ApPair.of(TimeAccelerationFieldGenerator, 6),
                            ApPair.of(TimeAccelerationFieldGenerator, 7),
                            ApPair.of(TimeAccelerationFieldGenerator, 8),
                        ),
                        -1,
                        { t, tier -> t.uTimeAccelerationField = tier!! },
                        { it.uTimeAccelerationField },
                    ),
                ),
            ).addElement(
                'D',
                withChannel<NyxMTEAtomMacro>(
                    "coil",
                    ofCoil(BiConsumer { t, aCoilLevel -> t.setCoilLevel(aCoilLevel) }) {
                        it.getCoilLevel()
                    },
                ),
            ).addElement(
                'E',
                ofChain(
                    ofBlock(GregTechAPI.sBlockCasings1, 14),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 1 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 0),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 2 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 1),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 4 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 2),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 8 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 3),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 16 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 4),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 32 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 5),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 64 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 6),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 128 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 7),
                    ),
                    onElementPass(
                        { it.uSpacetimeCompressionCount += 256 },
                        ofBlock(SpacetimeCompressionFieldGenerators, 8),
                    ),
                ),
            ).addElement('F', ofBlock(GregTechAPI.sBlockCasings1, 14))
            .addElement(
                'G',
                HatchElementBuilder
                    .builder<NyxMTEAtomMacro>()
                    .atLeast(InputBus, InputHatch)
                    .adder { c, t, i -> c.addInputToMachineList(t, i.toInt()) }
                    .dot(1)
                    .casingIndex((GregTechAPI.sBlockCasings1 as BlockCasings1).getTextureIndex(12))
                    .buildAndChain(GregTechAPI.sBlockCasings1, 12),
            ).addElement(
                'H',
                HatchElementBuilder
                    .builder<NyxMTEAtomMacro>()
                    .atLeast(OutputBus, OutputHatch)
                    .adder { c, t, i -> c.addOutputToMachineList(t, i.toInt()) }
                    .dot(2)
                    .casingIndex((GregTechAPI.sBlockCasings1 as BlockCasings1).getTextureIndex(12))
                    .buildAndChain(GregTechAPI.sBlockCasings1, 12),
            ).addElement(
                'I',
                HatchElementBuilder
                    .builder<NyxMTEAtomMacro>()
                    .atLeast(Energy, ExoticEnergy)
                    .adder { c, t, i -> c.addEnergyInputToMachineList(t, i.toInt()) }
                    .dot(3)
                    .casingIndex((GregTechAPI.sBlockCasings1 as BlockCasings1).getTextureIndex(12))
                    .buildAndChain(GregTechAPI.sBlockCasings1, 12),
            ).build()

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
                "               ",
            ),
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
                "      HHH      ",
            ),
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
                "               ",
            ),
        )
    // spotless: on

    private val textureIndex = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 12)

    override val sControllerBlock: Pair<Block, Int>
        get() = null!! // Special case

    override fun getTexture(
        baseMetaTileEntity: IGregTechTileEntity,
        sideDirection: ForgeDirection,
        facingDirection: ForgeDirection,
        colorIndex: Int,
        active: Boolean,
        redstoneLevel: Boolean,
    ): Array<ITexture> =
        if (sideDirection != ForgeDirection.UP) {
            arrayOf(Textures.BlockIcons.getCasingTextureForId(textureIndex))
        } else if (active) {
            arrayOf(
                Textures.BlockIcons.getCasingTextureForId(textureIndex),
                TextureFactory
                    .builder()
                    .addIcon(OVERLAY_FUSION1)
                    .extFacing()
                    .build(),
                TextureFactory
                    .builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build(),
            )
        } else {
            arrayOf(
                Textures.BlockIcons.getCasingTextureForId(textureIndex),
                TextureFactory
                    .builder()
                    .addIcon(OVERLAY_SCREEN)
                    .extFacing()
                    .build(),
                TextureFactory
                    .builder()
                    .addIcon(OVERLAY_SCREEN)
                    .extFacing()
                    .glow()
                    .build(),
            )
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
            .addInfo(NyxValues.CommonStrings.ChangeModeByScrewdriver)
            .beginStructureBlock(15, 3, 15, false)
            .addInputHatch(NyxValues.CommonStrings.BluePrintInfo, 1)
            .addOutputHatch(NyxValues.CommonStrings.BluePrintInfo, 1)
            .addInputBus(NyxValues.CommonStrings.BluePrintInfo, 1)
            .addOutputBus(NyxValues.CommonStrings.BluePrintInfo, 1)
            .addEnergyHatch(NyxValues.CommonStrings.BluePrintInfo, 1)
            .toolTipFinisher(NyxValues.CommonStrings.NyxNuclear)

    override fun getInfoDataExtra(): Array<String> =
        arrayOf(
            "${AQUA}时空压缩: ${GOLD}${GTUtility.formatNumbers(uSpacetimeCompressionCount.toLong())}",
            "${AQUA}时间加速: ${GOLD}${GTUtility.formatNumbers(uTimeAccelerationField.toLong())}",
            "${AQUA}星阵数量: ${GOLD}${GTUtility.formatNumbers(uStarArrayCount.toLong())}",
        )

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
        if (aNBT == null) return
        aNBT.setInteger("uSpacetimeCompressionField", uSpacetimeCompressionCount)
        aNBT.setInteger("uTimeAccelerationField", uTimeAccelerationField)
        aNBT.setInteger("uStarArrayCount", uStarArrayCount)
        pRecipeMode.saveNBTData(aNBT, "pRecipeMode")
    }

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
        if (aNBT == null) return
        uSpacetimeCompressionCount = aNBT.getInteger("uSpacetimeCompressionField")
        uTimeAccelerationField = aNBT.getInteger("uTimeAccelerationField")
        uStarArrayCount = aNBT.getInteger("uStarArrayCount")
        pRecipeMode.loadNBTData(aNBT, "pRecipeMode")
    }

    // endregion

    // region Selector

    fun setCoilLevel(aCoilLevel: HeatingCoilLevel) {
        this.mCoilLevel = aCoilLevel
    }

    fun getCoilLevel(): HeatingCoilLevel = this.mCoilLevel ?: HeatingCoilLevel.None

    // endregion
}
