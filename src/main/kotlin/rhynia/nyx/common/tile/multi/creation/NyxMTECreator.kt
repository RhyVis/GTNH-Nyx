package rhynia.nyx.common.tile.multi.creation

import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.screen.UIBuildContext
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget
import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.InputBus
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
import gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap
import gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser
import java.util.*
import kotlin.math.min
import kotlin.math.pow
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.GOLD
import net.minecraft.util.EnumChatFormatting.GREEN
import net.minecraft.util.EnumChatFormatting.WHITE
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.Config
import rhynia.nyx.api.enums.CheckRecipeResultRef
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.util.ItemUtil
import rhynia.nyx.api.util.MathUtil
import rhynia.nyx.api.util.NBTUtil.getFluid
import rhynia.nyx.api.util.NBTUtil.getItem
import rhynia.nyx.api.util.NBTUtil.setFluid
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.tile.base.NyxMTECubeBase

class NyxMTECreator : NyxMTECubeBase<NyxMTECreator> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NyxMTECreator(this.mName)
  }

  private var pItemProcessMode = true
  private val pItemProcessModeInt
    get() = if (pItemProcessMode) 1 else 0
  private var pRecipeTime: Byte = 0

  private var pMultiplier = 0
  private var pBase = 0
  private var pCost = 0L
    get() = if (field < 0) 0 else field

  private var pUUID: UUID? = null

  private var pProcessDisplayName: String = ""

  private val pItemStackZeroPoint: ItemStack by lazy { NyxItemList.TestItem01.get(1) }
  private var pItemStackStore = pItemStackZeroPoint
    set(value) {
      if (value != pItemStackZeroPoint) {
        field = value
        pProcessDisplayName = value.getDisplayName()
      }
    }

  private val pFluidStackZeroPoint: FluidStack by lazy { NyxMaterials.Null.getFluid(1) }
  private var pFluidStackStore = pFluidStackZeroPoint
    set(value) {
      if (value != pFluidStackZeroPoint) {
        field = value
        pProcessDisplayName = value.localizedName
      }
    }

  private val pUseEnergy: Boolean by lazy { Config.MTE_Creator_UseEnergy }

  override fun onScrewdriverRightClick(
      side: ForgeDirection?,
      aPlayer: EntityPlayer?,
      aX: Float,
      aY: Float,
      aZ: Float
  ) {
    if (baseMetaTileEntity.isServerSide) {
      this.pRecipeTime = ((this.pRecipeTime + 1) % 5).toByte()
      GTUtility.sendChatToPlayer(aPlayer, "合成时间: " + (this.pRecipeTime + 1) + "s")
    }
  }

  override fun createProcessingLogic(): ProcessingLogic? = null

  override fun checkProcessing(): CheckRecipeResult {
    mMaxProgresstime = 20 * (pRecipeTime + 1)
    mEfficiencyIncrease = 10000

    pBase = 0
    pMultiplier = 0
    pCost = 0

    val tempStack = controllerSlot ?: return CheckRecipeResultRegistry.NO_RECIPE

    mInputBusses
        .mapNotNull { it.realInventory }
        .filter { it.size > 0 }
        .forEach { stacks ->
          pBase += stacks.filter { ItemUtil.isAstralInfinityComplex(it) }.sumOf { it.stackSize }
          pMultiplier +=
              MathUtil.clampVal(
                  stacks.filter { GTUtility.isAnyIntegratedCircuit(it) }.sumOf { it.stackSize },
                  0,
                  24)
        }

    pCost = (pBase * 2.0.pow(min(48.0, pMultiplier.toDouble())).toLong())

    if (pItemProcessMode) {
      pItemStackStore = tempStack

      if (tempStack.isItemEqual(pItemStackZeroPoint)) return CheckRecipeResultRegistry.NO_RECIPE

      if (!pUseEnergy || addEUToGlobalEnergyMap(pUUID, -pCost)) {
        outputItemToAENetwork(pItemStackStore, pCost)
        return CheckRecipeResultRegistry.SUCCESSFUL
      } else {
        return CheckRecipeResultRef.INSUFFICIENT_POWER_NO_VAL
      }
    } else {
      pFluidStackStore =
          GTUtility.convertCellToFluid(tempStack) ?: return CheckRecipeResultRegistry.NO_RECIPE

      if (pFluidStackStore.isFluidEqual(pFluidStackZeroPoint)) {
        return CheckRecipeResultRegistry.NO_RECIPE
      }

      if (!pUseEnergy || addEUToGlobalEnergyMap(pUUID, -pCost)) {
        outputFluidToAENetwork(pFluidStackStore, pCost)
        return CheckRecipeResultRegistry.SUCCESSFUL
      } else {
        return CheckRecipeResultRef.INSUFFICIENT_POWER_NO_VAL
      }
    }

    return CheckRecipeResultRegistry.NO_RECIPE
  }

  override fun onPreTick(aBaseMetaTileEntity: IGregTechTileEntity?, aTick: Long) {
    super.onPreTick(aBaseMetaTileEntity, aTick)
    if (aTick == 1L) {
      pUUID = baseMetaTileEntity.ownerUuid
      strongCheckOrAddUser(pUUID)
    }
  }

  override fun supportsVoidProtection(): Boolean = false

  override fun supportsInputSeparation(): Boolean = false

  override fun supportsBatchMode(): Boolean = false

  override fun supportsSingleRecipeLocking(): Boolean = false

  // endregion

  // region Structure

  override val sCasingBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings8 to 7

  override val sCasingHatch: Array<IHatchElement<in NyxMTECreator>>
    get() = arrayOf(InputBus, OutputBus, OutputHatch)

  override val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_DTPF_OFF to OVERLAY_DTPF_OFF

  override val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_DTPF_ON to OVERLAY_DTPF_ON

  // endregion

  // region Info
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("造物者")
          .addInfo("逆向奇点的控制器")
          .addInfo("复制指定的物品或流体.")
          .addInfo("直接从无线电网获取所需能量.")
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus(NyxValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NyxValues.CommonStrings.BluePrintInfo, 1)
          .addOutputHatch(NyxValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NyxValues.CommonStrings.NyxMagical)

  override fun addUIWidgets(builder: ModularWindow.Builder, buildContext: UIBuildContext?) {
    super.addUIWidgets(builder, buildContext)
    builder.widget(
        CycleButtonWidget()
            .setToggle({ pItemProcessMode }, { pItemProcessMode = it })
            .setTextureGetter {
              if (it == 1) GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM
              else GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID
            }
            .setPlayClickSound(true)
            .setBackground(GTUITextures.BUTTON_STANDARD)
            .setPos(80, 91)
            .setSize(16, 16)
            .dynamicTooltip {
              mutableListOf(
                  StatCollector.translateToLocal("nyx.Creator.pItemProcess.$pItemProcessModeInt"))
            }
            .setUpdateTooltipEveryTick(true)
            .setTooltipShowUpDelay(BaseTileEntity.TOOLTIP_DELAY))
  }

  override fun getInfoDataExtra(): Array<String> =
      arrayOf("${AQUA}复制目标: ${GOLD}$pProcessDisplayName")

  override fun getWailaBody(
      itemStack: ItemStack?,
      currentTip: List<String?>?,
      accessor: IWailaDataAccessor?,
      config: IWailaConfigHandler?
  ) {
    super.getWailaBody(itemStack, currentTip, accessor, config)
    if (accessor?.nbtData?.getString("pName")?.isNotEmpty() == true) {
      currentTip as MutableList<String?>
      currentTip.add("${WHITE}复制目标: ${AQUA}${accessor.nbtData.getString("pName")}")
      currentTip.add(
          "${WHITE}生产总量: ${GREEN}${GTUtility.formatNumbers(accessor.nbtData.getLong("pProduce"))}")
    }
  }

  override fun getWailaNBTData(
      player: EntityPlayerMP?,
      tile: TileEntity?,
      tag: NBTTagCompound?,
      world: World?,
      x: Int,
      y: Int,
      z: Int
  ) {
    super.getWailaNBTData(player, tile, tag, world, x, y, z)
    if (baseMetaTileEntity?.isActive == true && tag != null) {
      tag.setString("pName", pProcessDisplayName)
      tag.setLong("pProduce", pCost)
    }
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
    if (aNBT == null) return

    aNBT.setByte("pRecipeTime", pRecipeTime)
    aNBT.setInteger("pMultiplier", pMultiplier)
    aNBT.setInteger("pBase", pBase)
    aNBT.setLong("pProduce", pCost)
    aNBT.setString("pName", pProcessDisplayName)
    aNBT.setBoolean("pItemProcessMode", pItemProcessMode)
    aNBT.setTag("pItemStackStore", GTUtility.saveItem(pItemStackStore))
    aNBT.setFluid("pFluidStackStore", pFluidStackStore)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
    if (aNBT == null) return

    pRecipeTime = aNBT.getByte("pRecipeTime")
    pMultiplier = aNBT.getInteger("pMultiplier")
    pBase = aNBT.getInteger("pBase")
    pCost = aNBT.getLong("pProduce")
    pProcessDisplayName = aNBT.getString("pName")
    pItemProcessMode = aNBT.getBoolean("pItemProcessMode")
    pItemStackStore = aNBT.getItem("pItemStackStore")
    pFluidStackStore = aNBT.getFluid("pFluidStackStore")
  }
  // endregion
}
