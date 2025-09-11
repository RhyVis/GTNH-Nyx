package rhynia.nyx.common.mte.prod

import com.gtnewhorizons.modularui.api.math.Alignment
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn
import com.gtnewhorizons.modularui.common.widget.SlotWidget
import com.gtnewhorizons.modularui.common.widget.TextWidget
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.OrePrefixes
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.recipe.check.CheckRecipeResultRegistry
import gregtech.api.util.MultiblockTooltipBuilder
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.api.item.MetaItemToken
import rhynia.nyx.api.item.asToken
import rhynia.nyx.api.util.localize
import rhynia.nyx.common.mte.base.NyxMTECubeBase
import rhynia.nyx.init.MaterialMapper

class NyxConverter : NyxMTECubeBase<NyxConverter> {
    constructor(
        aId: Int,
        aName: String,
    ) : super(aId, aName)

    constructor(aName: String) : super(aName)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity = NyxConverter(mName)

    private var pControllerToken: MetaItemToken = MetaItemToken.EMPTY
    private var pOrePrefix: OrePrefixes? = null

    override fun checkProcessing(): CheckRecipeResult {
        val controllerStack = controllerSlot
        pControllerToken = controllerStack?.asToken() ?: return cleanup(true)
        if (pControllerToken.isEmpty()) return cleanup(true)
        val (_, prefix) = MaterialMapper[pControllerToken] ?: return cleanup(true)

        pOrePrefix = prefix

        val inputItem = storedInputs.also { it.remove(controllerStack) }
        if (inputItem.isEmpty()) return cleanup()

        val out = mutableListOf<ItemStack>()
        for (itemStack in inputItem) {
            val (material, _) = MaterialMapper[itemStack] ?: continue
            if (!material.hasOrePrefix(prefix)) continue
            val outStack = material.getByOrePrefix(prefix, itemStack.stackSize) ?: continue
            out.add(outStack)
            itemStack.stackSize = 0
        }
        if (out.isEmpty()) return cleanup()

        mOutputItems = out.toTypedArray()

        mEfficiency = 10000
        mEfficiencyIncrease = 10000
        mMaxProgresstime = 80

        return CheckRecipeResultRegistry.SUCCESSFUL
    }

    private fun cleanup(clearOrePrefix: Boolean = false): CheckRecipeResult {
        if (clearOrePrefix) pOrePrefix = null
        mEfficiency = 0
        mEfficiencyIncrease = 0
        mMaxProgresstime = 0
        return CheckRecipeResultRegistry.NO_RECIPE
    }

    override fun drawTexts(
        screenElements: DynamicPositionedColumn,
        inventorySlot: SlotWidget,
    ) {
        super.drawTexts(screenElements, inventorySlot)
        screenElements.apply {
            widget(
                TextWidget
                    .dynamicString {
                        "${localize(locPrefixed("gui.t.0"))}: ${EnumChatFormatting.AQUA}${
                            pOrePrefix?.mRegularLocalName ?: "EMPTY"
                        }"
                    }.setSynced(true)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled { baseMTE.isActive },
            )
        }
    }

    override val sCasingBlock: Pair<Block, Int>
        get() = GregTechAPI.sBlockCasings2 to 0

    override val sCasingHatch: Array<IHatchElement<in NyxConverter>>
        get() = arrayOf(OutputBus, OutputHatch)

    override val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
        get() = OVERLAY_DTPF_OFF to OVERLAY_DTPF_OFF

    override val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
        get() = OVERLAY_DTPF_ON to OVERLAY_DTPF_ON

    override fun createTooltip(): MultiblockTooltipBuilder? =
        MultiblockTooltipBuilder()
            .addMachineTypeLocalized()
            .addInfoListLocalized(2)
            .addChangeModeByScrewdriver()
            .beginStructureCube()
            .addInputBus()
            .addOutputBus()
            .toolTipFinisher(CommonString.NyxMagical)
}
