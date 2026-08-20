package rhynia.nyx.common.mte.prod

import com.gtnewhorizons.modularui.api.widget.Widget
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
import gregtech.common.blocks.ItemMachines
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.DARK_RED
import net.minecraft.util.EnumChatFormatting.WHITE
import net.minecraftforge.common.util.ForgeDirection
import rhynia.nyx.ModLogger
import rhynia.nyx.api.enums.CheckRecipeResultRef
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.api.item.MetaItemToken
import rhynia.nyx.api.item.asToken
import rhynia.nyx.api.item.matches
import rhynia.nyx.api.process.NyxProcessingLogic
import rhynia.nyx.api.util.RefContainer
import rhynia.nyx.api.util.intObjMapOf
import rhynia.nyx.api.util.localize
import rhynia.nyx.api.util.localized
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
    private var pLastControllerItem: MetaItemToken? = null
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
        aTool: ItemStack?,
    ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool)
        pMode?.let { mode ->
            mode.next()
            ModLogger.debug("Recipe map: ${mode.currentName}")
        }
    }

    override fun createProcessingLogic(): ProcessingLogic =
        object : NyxProcessingLogic() {
            override fun process(): CheckRecipeResult {
                if (updateRecipeContainer()) {
                    setEuModifier(rEuModifier)
                    setSpeedBonus(rTimeModifier)
                    setOverclock(rOverclockType)
                    return super.process()
                } else {
                    return CheckRecipeResultRef.NO_RECIPE_MAP_SET
                }
            }

            init {
                setMaxParallelSupplier(::rMaxParallel)
            }
        }

    private fun updateRecipeContainer(): Boolean {
        val controllerItem = controllerSlot ?: return false

        // process() calls this on every recipe check, so skip the lookup while the
        // controller item itself is unchanged.
        val lastToken = pLastControllerItem
        if (pMode != null && lastToken != null && controllerItem matches lastToken) {
            pControllerStackSize = controllerItem.stackSize
            return true
        }

        val token = controllerItem.asToken()
        val modeContainer = RecipeMapper.getRecipeMap(token)

        if (modeContainer != null) {
            pMode = modeContainer
            pLastControllerItem = token
            pControllerStackSize = controllerItem.stackSize
            if (ModLogger.isDebugEnabled) ModLogger.debug("Update recipe map: ${modeContainer.currentName}")
            return true
        } else {
            pMode = null
            pLastControllerItem = null
            pControllerStackSize = 0
            if (ModLogger.isDebugEnabled) ModLogger.debug("Update recipe map: null")
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
                    "${WHITE}${"nyx.common.current"
                        .localized()}: ${pMode?.let { AQUA.toString() + it.currentName } ?: "${DARK_RED}?"}"
                },
        )
        super.drawTexts(screenElements, inventorySlot)
    }

    override fun addRowUIWidgets(): List<Widget> =
        listOf(
            ButtonWidget()
                .setOnClick { _, _ -> updateRecipeContainer() }
                .setPlayClickSound(true)
                .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_UP)
                .setSize(16, 16)
                .addTooltip(localize("nyx.machine.proxy.gui.t.1"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY),
            ButtonWidget()
                .setOnClick { _, _ -> pMode?.next() }
                .setPlayClickSound(true)
                .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                .setSize(16, 16)
                .addTooltip(localize("nyx.machine.proxy.gui.t.0"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY),
        )

    override fun loadNBTData(aNBT: NBTTagCompound) {
        super.loadNBTData(aNBT)
        if (pMode == null) updateRecipeContainer()
        pMode?.loadNBTData(aNBT, "pMode")
    }

    override fun saveNBTData(aNBT: NBTTagCompound) {
        super.saveNBTData(aNBT)
        pMode?.saveNBTData(aNBT, "pMode")
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

    object RecipeMapper {
        private val gtMteCache = intObjMapOf<RefContainer<RecipeMap<*>>?>()

        fun getRecipeMap(token: MetaItemToken): RefContainer<RecipeMap<*>>? {
            when (token.item) {
                is ItemMachines -> {
                    val id = token.meta.takeIf { it > 0 } ?: return null
                    if (gtMteCache.containsKey(id)) return gtMteCache[id]

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
                                gtMteCache[id] = it
                            }
                        } ?: null.also { gtMteCache[id] = null }
                }
                else -> {
                    if (ModLogger.isDebugEnabled) ModLogger.debug("Unsupported token item: ${token.item.javaClass.name}")
                    return null
                }
            }
        }
    }

    companion object {
        private val RefContainer<RecipeMap<*>>.currentName: String
            get() = localize(current.unlocalizedName)
    }
}
