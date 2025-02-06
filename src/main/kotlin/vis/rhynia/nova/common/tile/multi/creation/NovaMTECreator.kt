package vis.rhynia.nova.common.tile.multi.creation

import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.screen.UIBuildContext
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget
import gregtech.api.GregTechAPI
import gregtech.api.enums.Materials
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.BaseTileEntity
import gregtech.api.metatileentity.implementations.MTEHatchInputBus
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
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.util.ItemUtil
import vis.rhynia.nova.api.util.MathUtil
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.tile.base.NovaMTECubeBase

class NovaMTECreator : NovaMTECubeBase<NovaMTECreator> {

  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)
  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NovaMTECreator(this.mName)
  }

  private var pItemProcess = true
  private var pRecipeTime: Byte = 0
  private var pMultiplier = 0
  private var pBase = 0
  private var pProduce = 0L
  private var pUUID: UUID? = null
  private var pName: String? = ""
  private val pFluidStackZeroPoint: FluidStack = Materials.Water.getFluid(1)
  private var pFluidStackStore = pFluidStackZeroPoint
  private val pItemStackZeroPoint: ItemStack = NovaItemList.TestItem01.get(1)
  private var pItemStackStore = pItemStackZeroPoint

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
    resetState()
    val tempStack = controllerSlot

    mInputBusses
        .stream()
        .map(MTEHatchInputBus::getRealInventory)
        .filter { Objects.nonNull(it) }
        .filter { it.size > 0 }
        .peek { stacks ->
          pBase +=
              stacks
                  .filter { ItemUtil.isAstralInfinityComplex(it) }
                  .map { it.stackSize }
                  .reduce { a, b -> Integer.sum(a, b) }
        }
        .peek { stacks ->
          pMultiplier +=
              MathUtil.clampVal(
                  stacks
                      .filter { GTUtility.isAnyIntegratedCircuit(it) }
                      .map { it.stackSize }
                      .reduce { a: Int, b: Int -> Integer.sum(a, b) },
                  0,
                  24)
        }
        .close()

    pProduce = (pBase * 2.0.pow(min(48.0, pMultiplier.toDouble()).toDouble())).toLong()
    if (pProduce < 0) {
      pProduce = -pProduce
    }

    if (pItemProcess) {
      if (tempStack == null) {
        return CheckRecipeResultRegistry.NO_RECIPE
      }
      setWorkingItem(tempStack)
      if (pItemStackStore.isItemEqual(pItemStackZeroPoint)) {
        return CheckRecipeResultRegistry.NO_RECIPE
      }

      if (addEUToGlobalEnergyMap(pUUID, -pProduce)) {
        outputItemToAENetwork(pItemStackStore, pProduce)
        return CheckRecipeResultRegistry.SUCCESSFUL
      }
    } else {
      val tempFluidStack = GTUtility.convertCellToFluid(tempStack)

      if (tempFluidStack == null) {
        return CheckRecipeResultRegistry.NO_RECIPE
      }
      setWorkingFluid(tempFluidStack)
      if (tempFluidStack.isFluidEqual(pFluidStackZeroPoint)) {
        return CheckRecipeResultRegistry.NO_RECIPE
      }

      if (addEUToGlobalEnergyMap(pUUID, -pProduce)) {
        outputFluidToAENetwork(pFluidStackStore, pProduce)
        return CheckRecipeResultRegistry.SUCCESSFUL
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

  private fun setWorkingItem(itemStack: ItemStack) {
    pItemStackStore = itemStack
    pName = itemStack.getDisplayName()
  }

  private fun setWorkingFluid(fluidStack: FluidStack) {
    pFluidStackStore = fluidStack
    pName = fluidStack.localizedName
  }

  private fun resetState() {
    mMaxProgresstime = 20 * (pRecipeTime + 1)
    mEfficiencyIncrease = 10000
    pBase = 0
    pMultiplier = 0
    pProduce = 0
  }
  // endregion

  // region Structure
  override val sCasingBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings8 to 7

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
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .addOutputHatch(NovaValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NovaValues.CommonStrings.NovaMagical)

  override fun addUIWidgets(builder: ModularWindow.Builder, buildContext: UIBuildContext?) {
    super.addUIWidgets(builder, buildContext)
    builder.widget(
        CycleButtonWidget()
            .setToggle({ pItemProcess }, { pItemProcess = it })
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
                  StatCollector.translateToLocal(
                      "nova.pItemProcess." + (if (pItemProcess) 1 else 0)))
            }
            .setUpdateTooltipEveryTick(true)
            .setTooltipShowUpDelay(BaseTileEntity.TOOLTIP_DELAY))
  }

  override fun getInfoDataExtra(): Array<String> = arrayOf("${AQUA}复制目标: ${GOLD}$pName")

  override fun getWailaBody(
      itemStack: ItemStack?,
      currentTip: List<String?>?,
      accessor: IWailaDataAccessor?,
      config: IWailaConfigHandler?
  ) {
    super.getWailaBody(itemStack, currentTip, accessor, config)
    val tag = accessor?.nbtData
    if (!tag!!.getString("pName").isEmpty()) {
      currentTip as MutableList<String?>
      currentTip.add("${WHITE}复制目标: ${AQUA}${tag.getString("pName")}")
      currentTip.add("${WHITE}生产总量: ${GREEN}${GTUtility.formatNumbers(tag.getLong("pProduce"))}")
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
      tag.setString("pName", pName)
      tag.setLong("pProduce", pProduce)
    }
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
    if (aNBT == null) return

    aNBT.setByte("pRecipeTime", pRecipeTime)
    aNBT.setInteger("pMultiplier", pMultiplier)
    aNBT.setInteger("pBase", pBase)
    aNBT.setLong("pProduce", pProduce)
    aNBT.setString("pName", pName)
    aNBT.setBoolean("pItemProcess", pItemProcess)
    aNBT.setTag("pItemStackStore", GTUtility.saveItem(pItemStackStore))
    NBTTagCompound().let {
      pFluidStackStore.writeToNBT(it)
      aNBT.setTag("pFluidStackStore", it)
    }
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
    if (aNBT == null) return

    pRecipeTime = aNBT.getByte("pRecipeTime")
    pMultiplier = aNBT.getInteger("pMultiplier")
    pBase = aNBT.getInteger("pBase")
    pProduce = aNBT.getLong("pProduce")
    pName = aNBT.getString("pName")
    pItemProcess = aNBT.getBoolean("pItemProcess")
    pItemStackStore = GTUtility.loadItem(aNBT, "pItemStackStore")
    pFluidStackStore = GTUtility.loadFluid(aNBT, "pFluidStackStore")
  }
  // endregion
}
