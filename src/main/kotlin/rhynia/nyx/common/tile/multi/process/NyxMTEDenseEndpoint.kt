package rhynia.nyx.common.tile.multi.process

import bartworks.API.recipe.BartWorksRecipeMaps
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.interfaces.IHatchElement
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
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.process.OverclockType
import rhynia.nyx.api.recipe.NyxRecipeMaps
import rhynia.nyx.common.tile.base.NyxMTECubeBase

class NyxMTEDenseEndpoint : NyxMTECubeBase<NyxMTEDenseEndpoint> {
    constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
    constructor(aName: String) : super(aName)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity = NyxMTEDenseEndpoint(this.mName)

    // region Process
    private val pRecipeMode =
        ModeContainer.of(
            BartWorksRecipeMaps.electricImplosionCompressorRecipes,
            RecipeMaps.nanoForgeRecipes,
            NyxRecipeMaps.quarkRefactoringRecipes,
        )

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
                StatCollector.translateToLocal("nyx.DenseEndpoint.pRecipeMode.${pRecipeMode.index}"),
            )
        }
    }

    override fun getRecipeMap(): RecipeMap<*>? = pRecipeMode.current

    override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>?> = pRecipeMode.all

    override fun getRecipeCatalystPriority(): Int = -10

    override val rOverclockType: OverclockType = OverclockType.Perfect

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
        if (pRecipeMode.index == 1) {
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

    override val sCasingBlock: Pair<Block, Int>
        get() = GregTechAPI.sBlockCasings8 to 10

    override val sCasingHatch: Array<IHatchElement<in NyxMTEDenseEndpoint>>
        get() = arrayOf(InputBus, InputHatch, OutputBus, OutputHatch, Energy.or(ExoticEnergy))

    // endregion

    // region Info
    override fun createTooltip(): MultiblockTooltipBuilder =
        MultiblockTooltipBuilder()
            .addMachineType("电动聚爆压缩机 | 纳米锻炉 | 夸克重构机")
            .addInfo("致密极点的控制器")
            .addInfo("压缩到奇点, 再释放出来?")
            .addInfo("最大并行为128*最大输入电流.")
            .addInfo("执行无损超频.")
            .beginStructureBlock(3, 3, 3, false)
            .addInputBus()
            .addInputHatch()
            .addOutputBus()
            .addOutputHatch()
            .addEnergyHatch()
            .toolTipFinisher(NyxValues.CommonStrings.NyxNuclear)

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
        pRecipeMode.saveNBTData(aNBT ?: throw NullPointerException("NBT Tag Missing"), "pRecipeMode")
    }

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
        pRecipeMode.loadNBTData(aNBT ?: throw NullPointerException("NBT Tag Missing"), "pRecipeMode")
    }
    // endregion
}
