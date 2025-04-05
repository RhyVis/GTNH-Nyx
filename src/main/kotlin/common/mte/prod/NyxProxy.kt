package rhynia.nyx.common.mte.prod

import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.screen.UIBuildContext
import com.gtnewhorizons.modularui.common.widget.ButtonWidget
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn
import com.gtnewhorizons.modularui.common.widget.SlotWidget
import com.gtnewhorizons.modularui.common.widget.TextWidget
import gregtech.api.GregTechAPI
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.util.MultiblockTooltipBuilder
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.WHITE
import rhynia.nyx.ModLogger
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.api.util.localize
import rhynia.nyx.common.mte.base.NyxMTECubeBase

class NyxProxy : NyxMTECubeBase<NyxProxy> {
    constructor(
        aId: Int,
        aName: String,
    ) : super(aId, aName)

    constructor(aName: String) : super(aName)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity = NyxProxy(mName)

    private var pMode: ModeContainer<RecipeMap<*>>? = null
    private var pLastControllerID: Int = -1

    @Suppress("SpellCheckingInspection")
    override fun isCorrectMachinePart(aStack: ItemStack?): Boolean = aStack?.unlocalizedName?.startsWith("gt.blockmachine") == true

    override fun getRecipeMap(): RecipeMap<*>? = RecipeMaps.multiblockChemicalReactorRecipes // pMode?.current

    override fun createProcessingLogic(): ProcessingLogic? =
        object : ProcessingLogic() {
            override fun process(): CheckRecipeResult {
                updateRecipeContainer()
                return super.process()
            }
        }

    // override fun checkProcessing(): CheckRecipeResult = super.checkProcessing()

    private fun updateRecipeContainer() {
        controllerSlot?.let { controller ->
            pMode = getRecipeMap(controller)
            pLastControllerID = controller.itemDamage
            ModLogger.info("Update recipe map: ${pMode?.let { localize(it.current.unlocalizedName) }}")
        } ?: {
            pMode = null
            pLastControllerID = -1
            ModLogger.info("Update recipe map: null")
        }
    }

    override val sCasingBlock: Pair<Block, Int>
        get() = GregTechAPI.sBlockCasings2 to 0

    override fun createTooltip(): MultiblockTooltipBuilder =
        MultiblockTooltipBuilder()
            .addMachineTypeLocalized()
            .beginStructureCube()
            .toolTipFinisher(CommonString.NyxGigaFac)

    override fun drawTexts(
        screenElements: DynamicPositionedColumn,
        inventorySlot: SlotWidget?,
    ) {
        super.drawTexts(screenElements, inventorySlot)
        screenElements.widget(
            TextWidget
                .dynamicString {
                    "${WHITE}${localize("nyx.common.current")}: ${AQUA}${pMode?.let {
                        localize(it.current.unlocalizedName)
                    } ?: "?"}"
                },
        )
    }

    override fun addUIWidgets(
        builder: ModularWindow.Builder,
        buildContext: UIBuildContext?,
    ) {
        super.addUIWidgets(builder, buildContext)
        builder
            .widget(
                ButtonWidget()
                    .setOnClick { _, _ -> pMode?.next() }
                    .setPlayClickSound(true)
                    .setUpdateTooltipEveryTick(true)
                    .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                    .setPos(80, 91)
                    .setSize(16, 16)
                    .dynamicTooltip {
                        listOf(pMode?.let { localize(it.current.unlocalizedName) } ?: "?")
                    }.setTooltipShowUpDelay(TOOLTIP_DELAY),
            ).widget(
                ButtonWidget()
                    .setOnClick { _, _ -> updateRecipeContainer() }
                    .setPlayClickSound(true)
                    .setUpdateTooltipEveryTick(true)
                    .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_UP)
                    .setPos(174, 112)
                    .setSize(16, 16)
                    .addTooltip(localize("nyx.machine.proxy.gui.t.0"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY),
            )
    }

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
        if (aNBT == null) return

        if (pMode == null) updateRecipeContainer()
        pMode?.loadNBTData(aNBT, "pMode")
    }

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
        if (aNBT == null) return

        pMode?.saveNBTData(aNBT, "pMode")
    }

    companion object {
        private val cache = mutableMapOf<Int, ModeContainer<RecipeMap<*>>>()

        fun getRecipeMap(controller: ItemStack): ModeContainer<RecipeMap<*>>? {
            if (cache.containsKey(controller.itemDamage)) return cache[controller.itemDamage]

            val mte = GregTechAPI.METATILEENTITIES[controller.itemDamage] as? MTEMultiBlockBase ?: return null
            val recipeMaps =
                mte.availableRecipeMaps.filter {
                    it != RecipeMaps.assemblylineVisualRecipes
                }

            return recipeMaps.size
                .takeIf { it > 0 }
                ?.let {
                    ModeContainer(recipeMaps.toTypedArray()).also {
                        cache[controller.itemDamage] = it
                    }
                }
        }
    }
}
