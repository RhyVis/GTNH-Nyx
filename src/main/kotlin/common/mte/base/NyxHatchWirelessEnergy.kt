package rhynia.nyx.common.mte.base

import com.google.common.math.LongMath
import gregtech.api.enums.GTValues.V
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.util.GTUtility
import gregtech.common.misc.WirelessNetworkManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.GRAY
import net.minecraft.util.EnumChatFormatting.YELLOW
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.ForgeDirection
import tectech.thing.metaTileEntity.Textures
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti
import java.math.BigInteger
import java.util.UUID
import kotlin.math.min

class NyxHatchWirelessEnergy :
    MTEHatchEnergyMulti,
    IWirelessEnergyHatchInformation {
    private val helper = TransferHelper(Amperes, mTier)
    private var ownerUUID: UUID? = null

    @Suppress("SpellCheckingInspection")
    constructor(
        aID: Int,
        aName: String,
        aNameRegional: String,
        aTier: Int,
        aAmp: Int,
    ) : super(
        aID,
        aName,
        aNameRegional,
        aTier,
        0,
        arrayOf(
            StatCollector.translateToLocal("nyx.wirelessExt.tooltip.0"),
            StatCollector.translateToLocal("nyx.wirelessExt.tooltip.1"),
            StatCollector.translateToLocal("nyx.wirelessExt.tooltip.2"),
            "${StatCollector.translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1")}: ${YELLOW}${
                GTUtility.formatNumbers(aAmp * V[aTier])
            }${GRAY}EU/t",
        ),
        aAmp,
    )

    constructor(
        aName: String?,
        aTier: Int,
        aAmp: Int,
        aDescription: Array<String?>?,
        aTextures: Array<Array<Array<ITexture?>?>?>?,
    ) : super(aName, aTier, aAmp, aDescription, aTextures)

    private val textureOverlay: Array<ITexture> by lazy { mapTextureSet(Amperes) }

    private val vol: Long get() = V[mTier.toInt()]

    override fun getTexturesActive(aBaseTexture: ITexture?): Array<ITexture?> = arrayOf(aBaseTexture, textureOverlay[mTier.toInt()])

    override fun getTexturesInactive(aBaseTexture: ITexture?): Array<ITexture?> = arrayOf(aBaseTexture, textureOverlay[mTier.toInt()])

    override fun isSimpleMachine(): Boolean = true

    override fun isFacingValid(facing: ForgeDirection?): Boolean = true

    override fun isAccessAllowed(aPlayer: EntityPlayer?): Boolean = true

    override fun isEnetInput(): Boolean = false

    override fun isInputFacing(side: ForgeDirection?): Boolean = side == baseMetaTileEntity.frontFacing

    override fun isValidSlot(aIndex: Int): Boolean = false

    override fun getMinimumStoredEU(): Long = Amperes * vol

    override fun maxEUInput(): Long = vol

    override fun maxEUStore(): Long = (totalStorage(vol) / (2 * helper.overflowDivisor) * Amperes).toLong()

    override fun maxAmperesIn(): Long = Amperes.toLong()

    override fun maxWorkingAmperesIn(): Long = Amperes.toLong()

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity? =
        NyxHatchWirelessEnergy(mName, mTier.toInt(), Amperes, mDescriptionArray, mTextures)

    override fun allowPullStack(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aIndex: Int,
        side: ForgeDirection?,
        aStack: ItemStack?,
    ): Boolean = false

    override fun allowPutStack(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aIndex: Int,
        side: ForgeDirection?,
        aStack: ItemStack?,
    ): Boolean = false

    override fun getConnectionType(): ConnectionType = ConnectionType.WIRELESS

    override fun onFirstTick(aBaseMetaTileEntity: IGregTechTileEntity) {
        if (aBaseMetaTileEntity.isServerSide) {
            ownerUUID = aBaseMetaTileEntity.ownerUuid
            WirelessNetworkManager.strongCheckOrAddUser(ownerUUID)
            tryFetchingEnergy()
        }
    }

    override fun onPreTick(
        aBaseMetaTileEntity: IGregTechTileEntity,
        aTick: Long,
    ) {
        super.onPreTick(aBaseMetaTileEntity, aTick)
        if (aBaseMetaTileEntity.isServerSide && aTick % helper.actualTicksBetweenEnergyAddition == 0L) {
            tryFetchingEnergy()
        }
    }

    private fun tryFetchingEnergy() {
        val currentEU = baseMetaTileEntity.storedEU
        val maxEU = maxEUStore()
        val euToTransfer = min(maxEU - currentEU, helper.transferPerOptLong)
        if (euToTransfer <= 0) return // nothing to transfer

        if (!WirelessNetworkManager.addEUToGlobalEnergyMap(ownerUUID, -euToTransfer)) return
        euVar = currentEU + euToTransfer
    }
}

class NyxHatchWirelessDynamo :
    MTEHatchDynamoMulti,
    IWirelessEnergyHatchInformation {
    private val helper = TransferHelper(Amperes, mTier)
    private var ownerUUID: UUID? = null

    constructor(
        aID: Int,
        aName: String,
        aNameRegional: String,
        aTier: Int,
        aAmp: Int,
    ) : super(
        aID,
        aName,
        aNameRegional,
        aTier,
        0,
        arrayOf(
            StatCollector.translateToLocal("nyx.wirelessExt.tooltip.0"),
            StatCollector.translateToLocal("nyx.wirelessExt.tooltip.1"),
            StatCollector.translateToLocal("nyx.wirelessExt.tooltip.2"),
        ),
        aAmp,
    )

    constructor(
        aName: String?,
        aTier: Int,
        aAmp: Int,
        aDescription: Array<String?>?,
        aTextures: Array<Array<Array<ITexture?>?>?>?,
    ) : super(aName, aTier, aAmp, aDescription, aTextures)

    private val textureOverlay: Array<ITexture> by lazy { mapTextureSet(Amperes) }

    private val vol: Long get() = V[mTier.toInt()]

    override fun onWireCutterRightClick(
        side: ForgeDirection?,
        wrenchingSide: ForgeDirection?,
        aPlayer: EntityPlayer?,
        aX: Float,
        aY: Float,
        aZ: Float,
        aTool: ItemStack?,
    ): Boolean {
        if (tryStoringEnergy()) {
            GTUtility.sendChatToPlayer(aPlayer, "Instantly transferred energy to the network.")
        }
        return false
    }

    override fun getTexturesActive(aBaseTexture: ITexture?): Array<ITexture?> = arrayOf(aBaseTexture, textureOverlay[mTier.toInt()])

    override fun getTexturesInactive(aBaseTexture: ITexture?): Array<ITexture?> = arrayOf(aBaseTexture, textureOverlay[mTier.toInt()])

    override fun isSimpleMachine(): Boolean = true

    override fun isFacingValid(facing: ForgeDirection?): Boolean = true

    override fun isAccessAllowed(aPlayer: EntityPlayer?): Boolean = true

    override fun isEnetOutput(): Boolean = false

    override fun isInputFacing(side: ForgeDirection?): Boolean = side == baseMetaTileEntity.frontFacing

    override fun isValidSlot(aIndex: Int): Boolean = false

    override fun getMinimumStoredEU(): Long = Amperes * vol

    override fun maxEUOutput(): Long = vol

    override fun maxEUStore(): Long = (totalStorage(vol) / (2 * helper.overflowDivisor) * Amperes).toLong()

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity =
        NyxHatchWirelessDynamo(mName, mTier.toInt(), Amperes, mDescriptionArray, mTextures)

    override fun allowPullStack(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aIndex: Int,
        side: ForgeDirection?,
        aStack: ItemStack?,
    ): Boolean = false

    override fun allowPutStack(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aIndex: Int,
        side: ForgeDirection?,
        aStack: ItemStack?,
    ): Boolean = false

    override fun getConnectionType(): ConnectionType = ConnectionType.WIRELESS

    override fun onFirstTick(aBaseMetaTileEntity: IGregTechTileEntity) {
        if (aBaseMetaTileEntity.isServerSide) {
            ownerUUID = aBaseMetaTileEntity.ownerUuid
            WirelessNetworkManager.strongCheckOrAddUser(ownerUUID)
            tryStoringEnergy()
        }
    }

    override fun onPreTick(
        aBaseMetaTileEntity: IGregTechTileEntity,
        aTick: Long,
    ) {
        super.onPreTick(aBaseMetaTileEntity, aTick)
        if (aBaseMetaTileEntity.isServerSide && aTick % helper.actualTicksBetweenEnergyAddition == 0L) {
            tryStoringEnergy()
        }
    }

    private fun tryStoringEnergy(): Boolean {
        if (euVar < 0) euVar = 0
        if (euVar == 0L || !WirelessNetworkManager.addEUToGlobalEnergyMap(ownerUUID, euVar)) return false
        euVar = 0
        return true
    }
}

private val precisionMultiplier = LongMath.pow(10, 15)

private class TransferHelper(
    amp: Int,
    tier: Byte,
) {
    private val transferPerOpt: BigInteger =
        BigInteger
            .valueOf(amp * V[tier.toInt()])
            .multiply(BigInteger.valueOf(IWirelessEnergyHatchInformation.ticks_between_energy_addition))

    val overflowDivisor: Double = getOverflowDivisor(transferPerOpt)

    val transferPerOptLong: Long =
        if (overflowDivisor > 1) {
            transferPerOpt
                .divide(BigInteger.valueOf((overflowDivisor * precisionMultiplier * 2).toLong()))
                .multiply(BigInteger.valueOf(precisionMultiplier))
                .toLong()
        } else {
            transferPerOpt.toLong()
        }

    val actualTicksBetweenEnergyAddition =
        if (overflowDivisor > 1) {
            (IWirelessEnergyHatchInformation.ticks_between_energy_addition / (overflowDivisor * 2))
                .toLong()
        } else {
            IWirelessEnergyHatchInformation.ticks_between_energy_addition
        }
}

private fun mapTextureSet(amp: Int) =
    when (amp) {
        4 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A
        16 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A
        64 -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A
        else -> Textures.OVERLAYS_ENERGY_IN_WIRELESS_LASER
    }

private fun getOverflowDivisor(transferPerOpt: BigInteger): Double =
    if (transferPerOpt > BigInteger.valueOf(Long.MAX_VALUE)) {
        transferPerOpt.toDouble() / Long.MAX_VALUE
    } else {
        1.0
    }
