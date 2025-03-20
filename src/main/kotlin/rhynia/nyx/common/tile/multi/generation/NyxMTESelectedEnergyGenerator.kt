package rhynia.nyx.common.tile.multi.generation

import gregtech.api.GregTechAPI
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.recipe.check.SimpleCheckRecipeResult
import gregtech.api.util.GTUtility
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap
import gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser
import java.math.BigInteger
import java.util.*
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.UNDERLINE
import net.minecraft.util.EnumChatFormatting.WHITE
import net.minecraft.world.World
import rhynia.nyx.api.enums.CheckRecipeResultRef
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.util.ItemUtil
import rhynia.nyx.api.util.MathUtil
import rhynia.nyx.common.tile.base.NyxMTECubeBase

class NyxMTESelectedEnergyGenerator : NyxMTECubeBase<NyxMTESelectedEnergyGenerator> {
  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)

  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NyxMTESelectedEnergyGenerator(this.mName)
  }

  // region Process
  private var pCoreValue = 0
  private var pTweakValue = 0
  private var pBaseValue = 0L
  private var pConstruct: BigInteger = BigInteger.ZERO
  private var pEnergy: BigInteger = BigInteger.ZERO
  private var pUUID: UUID? = null

  override fun createProcessingLogic(): ProcessingLogic? = null

  override fun checkProcessing(): CheckRecipeResult {
    resetState()

    controllerSlot?.let {
      pCoreValue =
          if (ItemUtil.isAstralInfinityComplex(it)) {
            it.stackSize
          } else if (ItemUtil.isAstralInfinityGauge(it)) {
            it.stackSize * 4
          } else {
            return CheckRecipeResultRef.NO_SELECTED_ENERGY_CORE
          }
    }
        ?: return CheckRecipeResultRef.NO_SELECTED_ENERGY_CORE

    mInputBusses.forEach { bus ->
      bus.realInventory.forEach { itemStack ->
        if (ItemUtil.isCalibration(itemStack)) pBaseValue += itemStack.stackSize.toLong()
        if (pTweakValue <= 0 && GTUtility.isAnyIntegratedCircuit(itemStack))
            pTweakValue = MathUtil.clampVal(itemStack.getItemDamage(), 2, 24)
      }
    }

    if (pBaseValue <= 0) pBaseValue = 1L
    if (pTweakValue <= 0) pTweakValue = 2

    pConstruct =
        BigInteger.valueOf(pBaseValue)
            .multiply(BigInteger.valueOf(pTweakValue.toLong()).pow(pCoreValue))
    pEnergy =
        BigInteger.valueOf(Int.Companion.MAX_VALUE.toLong())
            .multiply(pConstruct)
            .multiply(BigInteger.valueOf(128))

    addEUToGlobalEnergyMap(pUUID, pEnergy)

    return SimpleCheckRecipeResult.ofSuccess("importing_energy")
  }

  private fun resetState() {
    pCoreValue = 0
    pTweakValue = 0
    pBaseValue = 0L
    pConstruct = BigInteger.ZERO
    pEnergy = BigInteger.ZERO
    mMaxProgresstime = 128
    mEfficiencyIncrease = 10000
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
    get() = GregTechAPI.sBlockCasings1 to 12

  override val sCasingHatch: Array<IHatchElement<in NyxMTESelectedEnergyGenerator>>
    get() = arrayOf(InputBus)

  override val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_DTPF_OFF to OVERLAY_DTPF_OFF

  override val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
    get() = OVERLAY_DTPF_ON to OVERLAY_DTPF_ON
  // endregion

  // region Info
  protected override fun createTooltip(): MultiblockTooltipBuilder =
      MultiblockTooltipBuilder()
          .addMachineType("虚空发电机")
          .addInfo("虚空发电机的控制器")
          .addInfo("使用星矩/星规和标定指示来产生能量.")
          .addInfo("发电 = 电路板编号^(星矩/星规) * 标定指示 * MAX A/t.")
          .addInfo("产出的能量将直接输出至无线网络.")
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus()
          .toolTipFinisher(NyxValues.CommonStrings.NyxMagical)

  override fun getInfoDataExtra(): Array<String> =
      arrayOf("${AQUA}等效能量: ${GTUtility.formatNumbers(pConstruct)}MAX EU/t")

  override fun getWailaBody(
      itemStack: ItemStack?,
      currentTip: List<String?>?,
      accessor: IWailaDataAccessor?,
      config: IWailaConfigHandler?
  ) {
    super.getWailaBody(itemStack, currentTip, accessor, config)
    currentTip!!.plus(
        "${WHITE}等效能量: ${AQUA}${
                GTUtility.formatNumbers(pConstruct)
            } ${WHITE}${UNDERLINE}MAX${WHITE} EU/t")
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
    if (baseMetaTileEntity?.isActive == true) {
      tag?.setByteArray("pConstructW", pConstruct.toByteArray())
    }
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    super.saveNBTData(aNBT)
    if (aNBT == null) return

    aNBT.setInteger("pCoreValue", pCoreValue)
    aNBT.setInteger("pTweakValue", pTweakValue)
    aNBT.setLong("pBaseValue", pBaseValue)
    aNBT.setByteArray("pConstruct", pConstruct.toByteArray())
    aNBT.setByteArray("pEnergy", pEnergy.toByteArray())
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    super.loadNBTData(aNBT)
    if (aNBT == null) return

    pCoreValue = aNBT.getInteger("pCoreValue")
    pTweakValue = aNBT.getInteger("pTweakValue")
    pBaseValue = aNBT.getLong("pBaseValue")
    pConstruct = BigInteger(aNBT.getByteArray("pConstruct"))
    pEnergy = BigInteger(aNBT.getByteArray("pEnergy"))
  }
  // endregion
}
