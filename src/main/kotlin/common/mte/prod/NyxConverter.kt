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
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumChatFormatting
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.api.item.MetaItemToken
import rhynia.nyx.api.item.asToken
import rhynia.nyx.api.util.getItemOrNull
import rhynia.nyx.api.util.localize
import rhynia.nyx.api.util.objLongMapOf
import rhynia.nyx.api.util.setItem
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

        if (isRecipeLockingEnabled) {
            if (pControllerToken.isEmpty() || pOrePrefix == null) {
                return pStop(true)
            }
            val currentToken = controllerStack?.asToken() ?: return pStop(true)
            if (currentToken != pControllerToken) {
                return pStop(true)
            }
        } else {
            pControllerToken = controllerStack?.asToken() ?: return pStop(true)
            if (pControllerToken.isEmpty()) return pStop(true)
            pOrePrefix = MaterialMapper.lookupOrePrefix(pControllerToken) ?: return pStop(true)
        }

        val targetPrefix = pOrePrefix!!
        val targetMaterialAmount = targetPrefix.mMaterialAmount.takeIf { it > 0 } ?: return pStop(true)

        val inputItem = storedInputs.also { it.remove(controllerStack) }
        if (inputItem.isEmpty()) return pStop()

        data class InputData(
            val stack: ItemStack,
            val material: MaterialMapper.MaterialData,
            val prefix: OrePrefixes,
            val materialAmount: Long,
        )

        // map input items to InputData to avoid multiple lookups
        val inputDataList =
            inputItem.mapNotNull {
                val (material, prefix) = MaterialMapper.lookup(it) ?: return@mapNotNull null
                if (!material.hasOrePrefix(targetPrefix)) return@mapNotNull null
                val sourceMaterialAmount = prefix.mMaterialAmount.takeIf { i -> i > 0 } ?: return@mapNotNull null
                InputData(it, material, prefix, sourceMaterialAmount)
            }

        if (inputDataList.isEmpty()) return pStop()

        // merge input items by material
        val materialAmountMap = objLongMapOf<MaterialMapper.MaterialData>()
        inputDataList.forEach {
            val totalMaterialAmount = it.materialAmount * it.stack.stackSize
            materialAmountMap[it.material] = materialAmountMap.getLong(it.material) + totalMaterialAmount
        }

        // calculate output counts
        val outputData = objLongMapOf<MaterialMapper.MaterialData>()
        for ((material, totalAmount) in materialAmountMap) {
            val outputCount = totalAmount / targetMaterialAmount
            if (outputCount > 0) {
                outputData[material] = outputCount
            }
        }

        if (outputData.isEmpty()) return pStop()

        // only consume the needed amount
        for (inputData in inputDataList) {
            val outputCount = outputData.getLong(inputData.material).takeIf { it > 0 } ?: continue

            // calculate how much to consume from this stack
            val neededAmount = outputCount * targetMaterialAmount

            // consume ratio capped to 1.0 to avoid over-consumption due to rounding
            val consumeRatio =
                minOf(
                    1.0,
                    neededAmount.toDouble() / (materialAmountMap.getLong(inputData.material).takeIf { it > 0 } ?: 1),
                )
            val consumeCount = (inputData.stack.stackSize * consumeRatio).toInt()

            inputData.stack.stackSize = maxOf(0, inputData.stack.stackSize - consumeCount)
        }

        // output generation
        mOutputItems =
            outputData
                .flatMap { (material, count) ->
                    val stacks = mutableListOf<ItemStack>()
                    var remaining = count

                    while (remaining > 0) {
                        val currentAmount = minOf(remaining, Int.MAX_VALUE.toLong()).toInt()
                        material.getByOrePrefixUnsafe(targetPrefix, currentAmount)?.let {
                            stacks.add(it)
                        }
                        remaining -= currentAmount
                    }

                    stacks
                }.toTypedArray()

        return pStart(outputData.size * 20)
    }

    private fun pStop(clearOrePrefix: Boolean = false): CheckRecipeResult {
        if (clearOrePrefix) pOrePrefix = null
        mEfficiency = 0
        mEfficiencyIncrease = 0
        mMaxProgresstime = 0
        mProgresstime = 0

        return CheckRecipeResultRegistry.NO_RECIPE
    }

    private fun pStart(processTime: Int): CheckRecipeResult {
        mEfficiency = 10000
        mEfficiencyIncrease = 10000
        mMaxProgresstime = processTime

        return CheckRecipeResultRegistry.SUCCESSFUL
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

    override fun supportsInputSeparation(): Boolean = false

    override fun supportsBatchMode(): Boolean = false

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
            .toolTipFinisher(CommonString.NyxGigaFac)

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
        if (aNBT == null) return

        pControllerToken = aNBT.getItemOrNull("pControllerToken")?.asToken() ?: MetaItemToken.EMPTY
        pOrePrefix =
            aNBT.getString("pOrePrefix").let {
                if (it.isEmpty()) null else OrePrefixes.valueOf(it)
            }
    }

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
        if (aNBT == null) return

        aNBT.setItem("pControllerToken", pControllerToken.createStack())
        aNBT.setString("pOrePrefix", pOrePrefix?.name ?: "")
    }
}
