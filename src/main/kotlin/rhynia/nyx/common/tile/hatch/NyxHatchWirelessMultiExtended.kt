package rhynia.nyx.common.tile.hatch

import com.google.common.math.LongMath
import gregtech.api.enums.GTValues
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.util.GTUtility
import gregtech.common.misc.WirelessNetworkManager
import java.math.BigInteger
import java.util.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.GRAY
import net.minecraft.util.EnumChatFormatting.YELLOW
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import tectech.thing.metaTileEntity.Textures
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessMulti

@Suppress("unused")
class NyxHatchWirelessMultiExtended : MTEHatchEnergyMulti, IWirelessEnergyHatchInformation {
  private val precisionMultiplier = LongMath.pow(10, 15)
  private val euTransferredPerOperation: BigInteger =
      BigInteger.valueOf(Amperes * GTValues.V[mTier.toInt()])
          .multiply(
              BigInteger.valueOf(IWirelessEnergyHatchInformation.ticks_between_energy_addition))

  private val overflowDivisor: Double = getOverflowDivisor(euTransferredPerOperation)

  private val actualTicksBetweenEnergyAddition =
      if (overflowDivisor > 1)
          (IWirelessEnergyHatchInformation.ticks_between_energy_addition / (overflowDivisor * 2))
              .toLong()
      else IWirelessEnergyHatchInformation.ticks_between_energy_addition

  private val euTransferredPerOperationLong =
      if (overflowDivisor > 1)
          euTransferredPerOperation
              .divide(BigInteger.valueOf((overflowDivisor * precisionMultiplier * 2).toLong()))
              .multiply(BigInteger.valueOf(precisionMultiplier))
              .toLong()
      else euTransferredPerOperation.toLong()

  private var ownerUUID: UUID? = null

  @Suppress("SpellCheckingInspection")
  constructor(
      aID: Int,
      aName: String,
      aNameRegional: String,
      aTier: Int,
      aAmp: Int
  ) : super(
      aID,
      aName,
      aNameRegional,
      aTier,
      0,
      arrayOf(
          "${GRAY}将能量存储于全局网络中，上限为2^(2^31)EU.",
          "${GRAY}不连接导线，此方块可以从网络中抽取EU.",
          "${AQUA}额外能源仓系列",
          "${StatCollector.translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1")}: ${YELLOW}${
        GTUtility.formatNumbers(
          aAmp * GTValues.V[aTier]
        )}${GRAY}EU/t",
      ),
      aAmp)

  constructor(
      aName: String,
      aTier: Int,
      aAmp: Int,
      aDescription: Array<out String>,
      aTextures: Array<out Array<out Array<out ITexture>>>
  ) : super(aName, aTier, aAmp, aDescription, aTextures)

  private fun getOverflowDivisor(euTransferredPerOperation: BigInteger): Double {
    if (euTransferredPerOperation > BigInteger.valueOf(Long.Companion.MAX_VALUE)) {
      return euTransferredPerOperation.toDouble() / Long.Companion.MAX_VALUE
    }
    return 1.0
  }

  private var textureOverlay: Array<ITexture>? = null

  override fun getTexturesActive(aBaseTexture: ITexture?): Array<ITexture?> {
    textureOverlay =
        when (Amperes) {
          4 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A
          16 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A
          64 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A
          else -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_LASER
        }
    return arrayOf<ITexture?>(aBaseTexture, textureOverlay!![mTier.toInt()])
  }

  override fun getTexturesInactive(aBaseTexture: ITexture?): Array<ITexture?> {
    textureOverlay =
        when (Amperes) {
          4 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A
          16 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A
          64 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A
          else -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_LASER
        }
    return arrayOf<ITexture?>(aBaseTexture, textureOverlay!![mTier.toInt()])
  }

  override fun isSimpleMachine(): Boolean {
    return true
  }

  override fun isFacingValid(facing: ForgeDirection?): Boolean {
    return true
  }

  override fun isAccessAllowed(aPlayer: EntityPlayer?): Boolean {
    return true
  }

  override fun isEnetInput(): Boolean {
    return false
  }

  override fun isInputFacing(side: ForgeDirection?): Boolean {
    return side == baseMetaTileEntity.frontFacing
  }

  override fun isValidSlot(aIndex: Int): Boolean {
    return false
  }

  override fun getMinimumStoredEU(): Long {
    return Amperes * GTValues.V[mTier.toInt()]
  }

  override fun maxEUInput(): Long {
    return GTValues.V[mTier.toInt()]
  }

  override fun maxEUStore(): Long {
    return (totalStorage(GTValues.V[mTier.toInt()]) / (2 * overflowDivisor) * Amperes).toLong()
  }

  override fun maxAmperesIn(): Long {
    return Amperes.toLong()
  }

  override fun maxWorkingAmperesIn(): Long {
    return Amperes.toLong()
  }

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity? {
    return MTEHatchWirelessMulti(mName, mTier.toInt(), Amperes, mDescriptionArray, mTextures)
  }

  override fun allowPullStack(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aIndex: Int,
      side: ForgeDirection?,
      aStack: ItemStack?
  ): Boolean {
    return false
  }

  override fun allowPutStack(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aIndex: Int,
      side: ForgeDirection?,
      aStack: ItemStack?
  ): Boolean {
    return false
  }

  override fun getConnectionType(): ConnectionType {
    return ConnectionType.WIRELESS
  }

  override fun onFirstTick(aBaseMetaTileEntity: IGregTechTileEntity) {
    if (aBaseMetaTileEntity.isServerSide) {
      // On first tick find the player name and attempt to add them to the map.

      // UUID and username of the owner.

      ownerUUID = aBaseMetaTileEntity.ownerUuid

      WirelessNetworkManager.strongCheckOrAddUser(ownerUUID)

      tryFetchingEnergy()
    }
  }

  override fun onPreTick(aBaseMetaTileEntity: IGregTechTileEntity, aTick: Long) {
    super.onPreTick(aBaseMetaTileEntity, aTick)

    if (aBaseMetaTileEntity.isServerSide) {
      // This is set up in a way to be as optimised as possible. If a user has a relatively
      // plentiful energy
      // network
      // it should make no difference to them. Minimising the number of operations on BigInteger is
      // essential.

      // Every actualTicksBetweenEnergyAddition add eu_transferred_per_operation to internal EU
      // storage from
      // network.

      if (aTick % actualTicksBetweenEnergyAddition == 0L) {
        tryFetchingEnergy()
      }
    }
  }

  private fun tryFetchingEnergy() {
    val currentEU = baseMetaTileEntity.storedEU
    val maxEU = maxEUStore()
    val euToTransfer = java.lang.Long.min(maxEU - currentEU, euTransferredPerOperationLong)
    if (euToTransfer <= 0) return // nothing to transfer

    if (!WirelessNetworkManager.addEUToGlobalEnergyMap(ownerUUID, -euToTransfer)) return
    euVar = currentEU + euToTransfer
  }
}
