package rhynia.nyx.common.mte.prod

import com.gtnewhorizons.modularui.api.math.Alignment
import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.screen.UIBuildContext
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn
import com.gtnewhorizons.modularui.common.widget.SlotWidget
import com.gtnewhorizons.modularui.common.widget.TextWidget
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.BaseTileEntity
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.recipe.check.CheckRecipeResultRegistry
import gregtech.api.util.GTUtility
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.mte.base.NyxMTECubeBase
import java.util.UUID

class NyxCopier : NyxMTECubeBase<NyxCopier> {
    constructor(
        aId: Int,
        aName: String,
    ) : super(aId, aName)

    constructor(aName: String) : super(aName)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity = NyxCopier(mName)

    private var pItemMode = true

    private var pDisplayName: String = "×"
    private val pCopyItemBase: ItemStack by lazy { NyxItemList.TestItem01.get(1) }
    private var pCopyItem: ItemStack = pCopyItemBase.copy()
        set(value) {
            pDisplayName = value.displayName
            field = value
        }
    private val pCopyFluidBase: FluidStack by lazy { NyxMaterials.Null.getFluid() }
    private var pCopyFluid: FluidStack = pCopyFluidBase
        set(value) {
            pDisplayName = value.localizedName
            field = value
        }

    private lateinit var pUUID: UUID

    override fun onScrewdriverRightClick(
        side: ForgeDirection?,
        aPlayer: EntityPlayer?,
        aX: Float,
        aY: Float,
        aZ: Float,
    ) {
        if (!baseMetaTileEntity.isServerSide) return
        pItemMode = !pItemMode
    }

    override fun onPreTick(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aTick: Long,
    ) {
        super.onPreTick(aBaseMetaTileEntity, aTick)
        if (aTick == 1L) {
            pUUID = baseMetaTileEntity.ownerUuid
            strongCheckOrAddUser(pUUID)
            checkNotNull(pUUID) { "UUID NULL!" }
        }
    }

    override fun createProcessingLogic(): ProcessingLogic? = null

    override fun checkProcessing(): CheckRecipeResult {
        resetStatus()
        return checkRun(controllerSlot)
    }

    private fun resetStatus() {
        mMaxProgresstime = 100
        mEfficiency = 10000
        mEfficiencyIncrease = 10000
        pCopyItem = pCopyItemBase
        pCopyFluid = pCopyFluidBase
        pDisplayName = "×"
    }

    private fun checkRun(stackToCopy: ItemStack?): CheckRecipeResult {
        if (stackToCopy == null) return CheckRecipeResultRegistry.NO_RECIPE
        if (pItemMode) {
            pCopyItem = stackToCopy
            if (stackToCopy.isItemEqual(pCopyItemBase)) return CheckRecipeResultRegistry.NO_RECIPE
            outputItemToAENetwork(pCopyItem, 1024)
        } else {
            pCopyFluid = GTUtility.convertCellToFluid(stackToCopy) ?: return CheckRecipeResultRegistry.NO_RECIPE
            if (pCopyFluid.isFluidEqual(pCopyFluidBase)) return CheckRecipeResultRegistry.NO_RECIPE
            outputFluidToAENetwork(pCopyFluid, 128_000)
        }
        return CheckRecipeResultRegistry.SUCCESSFUL
    }

    override fun supportsVoidProtection(): Boolean = false

    override fun supportsInputSeparation(): Boolean = false

    override fun supportsBatchMode(): Boolean = false

    override fun supportsSingleRecipeLocking(): Boolean = false

    override val sCasingBlock: Pair<Block, Int>
        get() = GregTechAPI.sBlockCasings8 to 7

    override val sCasingHatch: Array<IHatchElement<in NyxCopier>>
        get() = arrayOf(OutputBus, OutputHatch)

    override val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
        get() = OVERLAY_DTPF_OFF to OVERLAY_DTPF_OFF

    override val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
        get() = OVERLAY_DTPF_ON to OVERLAY_DTPF_ON

    override fun createTooltip(): MultiblockTooltipBuilder =
        MultiblockTooltipBuilder()
            .addMachineTypeLocalized()
            .addInfoListLocalized(2)
            .addChangeModeByScrewdriver()
            .beginStructureCube()
            .addInputBus(1)
            .addOutputBus(1)
            .toolTipFinisher(CommonString.NyxMagical)

    override fun addUIWidgets(
        builder: ModularWindow.Builder,
        buildContext: UIBuildContext?,
    ) {
        super.addUIWidgets(builder, buildContext)
        builder.widget(
            CycleButtonWidget()
                .setToggle({ pItemMode }, { pItemMode = it })
                .setTextureGetter {
                    if (it == 1) {
                        GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM
                    } else {
                        GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID
                    }
                }.setPlayClickSound(true)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(80, 91)
                .setSize(16, 16)
                .setUpdateTooltipEveryTick(true)
                .setTooltipShowUpDelay(BaseTileEntity.TOOLTIP_DELAY),
        )
    }

    override fun drawTexts(
        screenElements: DynamicPositionedColumn,
        inventorySlot: SlotWidget,
    ) {
        super.drawTexts(screenElements, inventorySlot)
        screenElements.widget(
            TextWidget
                .dynamicString { StatCollector.translateToLocalFormatted("nyx.machine.copier.waila.0", pDisplayName) }
                .setSynced(true)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled { baseMetaTileEntity.isActive },
        )
        screenElements.widget(
            TextWidget
                .dynamicString {
                    if (pItemMode) {
                        StatCollector.translateToLocal("nyx.machine.copier.waila.1.item")
                    } else {
                        StatCollector.translateToLocal("nyx.machine.copier.waila.1.fluid")
                    }
                }.setSynced(true)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled { baseMetaTileEntity.isActive },
        )
    }

    override fun getWailaBody(
        itemStack: ItemStack?,
        currentTip: MutableList<String>,
        accessor: IWailaDataAccessor,
        config: IWailaConfigHandler?,
    ) {
        super.getWailaBody(itemStack, currentTip, accessor, config)
        val displayName = accessor.nbtData.getString("pDisplayName")
        if (displayName.isEmpty()) return
        currentTip.add(StatCollector.translateToLocalFormatted("nyx.machine.copier.waila.0", displayName))
        val mode = accessor.nbtData.getInteger("pItemMode").takeIf { it != 0 } ?: return
        if (mode == 1) {
            currentTip.add(StatCollector.translateToLocal("nyx.machine.copier.waila.1.item"))
        } else {
            currentTip.add(StatCollector.translateToLocal("nyx.machine.copier.waila.1.fluid"))
        }
    }

    override fun getWailaNBTData(
        player: EntityPlayerMP?,
        tile: TileEntity?,
        tag: NBTTagCompound,
        world: World?,
        x: Int,
        y: Int,
        z: Int,
    ) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z)
        if (baseMetaTileEntity?.isActive == true) {
            tag.setString("pDisplayName", pDisplayName)
            tag.setInteger("pItemMode", if (pItemMode) 1 else -1)
        }
    }

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
        if (aNBT == null) return

        aNBT.setBoolean("pItemMode", pItemMode)
    }

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
        if (aNBT == null) return

        pItemMode = aNBT.getBoolean("pItemMode")
    }
}
