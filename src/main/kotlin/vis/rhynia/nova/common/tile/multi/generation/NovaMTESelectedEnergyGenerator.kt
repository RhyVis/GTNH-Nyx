package vis.rhynia.nova.common.tile.multi.generation

import gregtech.api.GregTechAPI
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON
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
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.util.ItemUtil
import vis.rhynia.nova.api.util.MathUtil
import vis.rhynia.nova.common.tile.base.NovaMTECubeBase

class NovaMTESelectedEnergyGenerator : NovaMTECubeBase<NovaMTESelectedEnergyGenerator> {
  constructor(aID: Int, aName: String, aNameRegional: String) : super(aID, aName, aNameRegional)

  constructor(aName: String) : super(aName)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity {
    return NovaMTESelectedEnergyGenerator(this.mName)
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
    controllerSlot.let {
      pCoreValue =
          if (ItemUtil.isAstralInfinityComplex(it)) {
            it.stackSize
          } else if (ItemUtil.isAstralInfinityGauge(it)) {
            it.stackSize * 4
          } else {
            return SimpleCheckRecipeResult.ofFailure("no_core_set")
          }
    }

    for (inputBus in mInputBusses) {
      for (itemStack in inputBus.realInventory) {
        if (ItemUtil.isCalibration(itemStack)) {
          pBaseValue += itemStack.stackSize.toLong()
        }
        if (pTweakValue <= 0 && GTUtility.isAnyIntegratedCircuit(itemStack)) {
          pTweakValue = MathUtil.clampVal(itemStack.getItemDamage(), 2, 24)
        }
      }
    }

    if (pBaseValue <= 0) {
      pBaseValue = 1L
    }
    if (pTweakValue <= 0) {
      pTweakValue = 1
    }

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

  override fun supportsVoidProtection(): Boolean {
    return false
  }

  override fun supportsInputSeparation(): Boolean {
    return false
  }

  override fun supportsBatchMode(): Boolean {
    return false
  }

  override fun supportsSingleRecipeLocking(): Boolean {
    return false
  }
  // endregion

  // region Structure
  override val sCasingBlock: Pair<Block, Int>
    get() = GregTechAPI.sBlockCasings1 to 12

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
          .addInfo("发电 = 电路板编号^星矩 * 标定指示 * MAX A/t.")
          .addInfo("产出的能量将直接输出至无线网络.")
          .addSeparator()
          .addInfo(NovaValues.CommonStrings.BluePrintTip)
          .beginStructureBlock(3, 3, 3, false)
          .addInputBus(NovaValues.CommonStrings.BluePrintInfo, 1)
          .toolTipFinisher(NovaValues.CommonStrings.NovaMagical)

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
    val tileEntity = baseMetaTileEntity
    if (tileEntity?.isActive == true) {
      tag?.setByteArray("pConstructW", pConstruct.toByteArray())
    }
  }

  override fun saveNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      it.setInteger("pCoreValue", pCoreValue)
      it.setInteger("pTweakValue", pTweakValue)
      it.setLong("pBaseValue", pBaseValue)
      it.setByteArray("pConstruct", pConstruct.toByteArray())
      it.setByteArray("pEnergy", pEnergy.toByteArray())
    }
    super.saveNBTData(aNBT)
  }

  override fun loadNBTData(aNBT: NBTTagCompound?) {
    aNBT?.let {
      pCoreValue = it.getInteger("pCoreValue")
      pTweakValue = it.getInteger("pTweakValue")
      pBaseValue = it.getLong("pBaseValue")
      pConstruct = BigInteger(it.getByteArray("pConstruct"))
      pEnergy = BigInteger(it.getByteArray("pEnergy"))
    }
    super.loadNBTData(aNBT)
  }
  // endregion
}
