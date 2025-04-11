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
import gregtech.api.interfaces.tileentity.RecipeMapWorkable
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.util.MultiblockTooltipBuilder
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.DARK_RED
import net.minecraft.util.EnumChatFormatting.WHITE
import net.minecraftforge.common.util.ForgeDirection
import rhynia.nyx.ModLogger
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.api.util.RefContainer
import rhynia.nyx.api.util.localize
import rhynia.nyx.common.mte.base.NyxMTECubeBase
import kotlin.math.log10
import kotlin.math.pow

class NyxProxy : NyxMTECubeBase<NyxProxy> {
    constructor(
        aId: Int,
        aName: String,
    ) : super(aId, aName)

    constructor(aName: String) : super(aName)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity = NyxProxy(mName)

    private var pMode: RefContainer<RecipeMap<*>>? = null
    private var pLastControllerID: Int = -1
    private var pControllerStackSize: Int = 0

    override val rMaxParallel: Int
        get() = LogarithmicMapper[pControllerStackSize]

    override fun getRecipeMap(): RecipeMap<*>? = pMode?.current

    override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>?> = emptyList()

    override fun onScrewdriverRightClick(
        side: ForgeDirection?,
        aPlayer: EntityPlayer?,
        aX: Float,
        aY: Float,
        aZ: Float,
    ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ)
        pMode?.let { mode ->
            mode.next()
            ModLogger.debug("Recipe map: ${mode.currentName}")
        }
    }

    override fun createProcessingLogic(): ProcessingLogic? =
        object : ProcessingLogic() {
            override fun process(): CheckRecipeResult {
                if (updateRecipeContainer()) {
                    setEuModifier(rEuModifier)
                    setSpeedBonus(rTimeModifier)
                    setOverclock(rOverclockType.timeDec, rOverclockType.powerInc)
                }
                return super.process()
            }
        }.setMaxParallelSupplier(::rMaxParallel)

    private fun updateRecipeContainer(): Boolean {
        val id = controllerSlot?.itemDamage ?: -1
        if (id == pLastControllerID) return true

        id.takeIf { it > 0 }?.let { id ->
            pMode = getRecipeMap(id)
            pLastControllerID = id
            pControllerStackSize = controllerSlot!!.stackSize
            ModLogger.debug("Update recipe map: ${pMode?.currentName}")
            return true
        } ?: run {
            pMode = null
            pLastControllerID = -1
            pControllerStackSize = 0
            ModLogger.info("Update recipe map: null")
            return false
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
        screenElements.widget(
            TextWidget
                .dynamicString {
                    "${WHITE}${localize(
                        "nyx.common.current",
                    )}: ${pMode?.let { AQUA.toString() + it.currentName } ?: "${DARK_RED}?"}"
                },
        )
        super.drawTexts(screenElements, inventorySlot)
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
                    .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                    .setPos(80, 91)
                    .setSize(16, 16)
                    .addTooltip(localize("nyx.machine.proxy.gui.t.0"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY),
            ).widget(
                ButtonWidget()
                    .setOnClick { _, _ -> updateRecipeContainer() }
                    .setPlayClickSound(true)
                    .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_UP)
                    .setPos(174, 112)
                    .setSize(16, 16)
                    .addTooltip(localize("nyx.machine.proxy.gui.t.1"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY),
            )
    }

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
        if (aNBT == null) return

        if (pMode == null) updateRecipeContainer()
        pMode?.loadNBTData(aNBT, "pMode")
    }

    object LogarithmicMapper {
        private val mappingCache: IntArray by lazy { initMapping() }

        private fun initMapping(): IntArray {
            val cache = IntArray(65)

            val factor = log10(Int.MAX_VALUE.toDouble()) / log10(64.0)

            for (i in 0..64) {
                if (i <= 1) {
                    cache[i] = 1
                } else if (i == 64) {
                    cache[i] = Int.MAX_VALUE
                } else {
                    val value = i.toDouble().pow(factor).toLong()
                    cache[i] = value.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
                }
            }

            return cache
        }

        operator fun get(i: Int) = mappingCache[i.coerceIn(0, 64)]
    }

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
        if (aNBT == null) return

        pMode?.saveNBTData(aNBT, "pMode")
    }

    companion object {
        private val cache = mutableMapOf<Int, RefContainer<RecipeMap<*>>>()

        private val RefContainer<RecipeMap<*>>.currentName: String
            get() = localize(current.unlocalizedName)

        fun getRecipeMap(id: Int): RefContainer<RecipeMap<*>>? {
            if (cache.containsKey(id)) return cache[id]

            val mte = GregTechAPI.METATILEENTITIES[id] ?: return null
            val recipeMaps =
                when (mte) {
                    is RecipeMapWorkable ->
                        mte.availableRecipeMaps.filter {
                            it != RecipeMaps.assemblylineVisualRecipes
                        }
                    else -> return null
                }

            return recipeMaps.size
                .takeIf { it > 0 }
                ?.let {
                    RefContainer(recipeMaps).also {
                        cache[id] = it
                    }
                }
        }
    }
}
