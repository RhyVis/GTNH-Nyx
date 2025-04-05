package rhynia.nyx.common.mte.base

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable
import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW
import gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW
import gregtech.api.interfaces.IHatchElement
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase
import gregtech.api.metatileentity.implementations.MTEHatch
import gregtech.api.metatileentity.implementations.MTEHatchDynamo
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.render.TextureFactory
import gregtech.api.util.GTUtility
import gregtech.api.util.IGTHatchAdder
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.tileentities.machines.MTEHatchOutputBusME
import gregtech.common.tileentities.machines.MTEHatchOutputME
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.GOLD
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import org.jetbrains.annotations.ApiStatus.OverrideOnly
import rhynia.nyx.api.enums.CommonString
import rhynia.nyx.api.process.OverclockType
import rhynia.nyx.api.util.idEqual
import rhynia.nyx.api.util.size
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti
import kotlin.reflect.KClass

@Suppress("UNUSED")
abstract class NyxMTEBase<T : MTEExtendedPowerMultiBlockBase<T>> :
    MTEExtendedPowerMultiBlockBase<T>,
    ISurvivalConstructable {
    protected constructor(
        aID: Int,
        aName: String,
    ) : super(
        aID,
        aName,
        StatCollector.translateToLocal("$aName.name"),
    )

    protected constructor(aName: String) : super(aName)

    protected companion object {
        const val STRUCTURE_PIECE_MAIN = "Main" // Just a name

        /** Simulated Hatch Element for Exotic Energy Hatch which has multiple allowed ampere. */
        val ExoticDynamo =
            object : IHatchElement<NyxMTEBase<*>> {
                override fun mteClasses(): List<Class<out IMetaTileEntity>> = listOf(MTEHatchDynamoMulti::class.java)

                override fun adder(): IGTHatchAdder<in NyxMTEBase<*>> =
                    object : IGTHatchAdder<NyxMTEBase<*>> {
                        override fun apply(
                            c: NyxMTEBase<*>,
                            t: IGregTechTileEntity?,
                            i: Short?,
                        ): Boolean = c.addDynamoToMachineList(t, i!!.toInt())
                    }

                override fun name(): String = "ExoticDynamo"

                override fun count(t: NyxMTEBase<*>?): Long {
                    return (t ?: return 0).mExoticDynamoHatches.size.toLong()
                }
            }

        /**
         * Structure Definition for the machine, set when first time calling getStructureDefinition().
         */
        val cachedStructureDefs: MutableMap<KClass<out NyxMTEBase<*>>, IStructureDefinition<*>> =
            mutableMapOf()
    }

    /** Remove maintenance requirement. */
    protected fun removeMaintenance() {
        mHardHammer = true
        mSoftHammer = true
        mScrewdriver = true
        mCrowbar = true
        mSolderingTool = true
        mWrench = true
    }

    override fun isCorrectMachinePart(aStack: ItemStack?): Boolean = true

    private val mExoticDynamoHatches: MutableList<MTEHatchDynamoMulti> = mutableListOf()

    /** Universal Hatch Adder */
    override fun addToMachineList(
        aTileEntity: IGregTechTileEntity?,
        aBaseCasingIndex: Int,
    ): Boolean =
        super.addToMachineList(aTileEntity, aBaseCasingIndex) ||
            addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex) ||
            addExoticDynamoToMachineList(aTileEntity, aBaseCasingIndex)

    fun addToMachineListCompatible(
        aTileEntity: IGregTechTileEntity?,
        aBaseCasingIndex: Short,
    ): Boolean = super.addToMachineList(aTileEntity, aBaseCasingIndex.toInt())

    override fun addEnergyInputToMachineList(
        aTileEntity: IGregTechTileEntity?,
        aBaseCasingIndex: Int,
    ): Boolean =
        super.addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex) ||
            addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex)

    override fun addDynamoToMachineList(
        aTileEntity: IGregTechTileEntity?,
        aBaseCasingIndex: Int,
    ): Boolean {
        val mte = aTileEntity?.metaTileEntity ?: return false
        if (mte is MTEHatchDynamo) {
            mte.updateTexture(aBaseCasingIndex)
            mte.updateCraftingIcon(machineCraftingIcon)
            return mDynamoHatches.add(mte)
        } else if (mte is MTEHatchDynamoMulti) {
            mte.updateTexture(aBaseCasingIndex)
            mte.updateCraftingIcon(machineCraftingIcon)
            return mExoticDynamoHatches.add(mte)
        }
        return false
    }

    fun addExoticDynamoToMachineList(
        aTileEntity: IGregTechTileEntity?,
        aBaseCasingIndex: Int,
    ): Boolean {
        val mte = aTileEntity?.metaTileEntity ?: return false
        if (mte is MTEHatchDynamoMulti) {
            mte.updateTexture(aBaseCasingIndex)
            mte.updateCraftingIcon(machineCraftingIcon)
            return mExoticDynamoHatches.add(mte)
        }
        return false
    }

    override fun addEnergyOutputMultipleDynamos(
        aEU: Long,
        aAllowMixedVoltageDynamos: Boolean,
    ): Boolean {
        var tEU = aEU
        if (tEU < 0) tEU = -aEU

        fun processHatches(
            hatches: List<MTEHatch>,
            tEU: Long,
        ): Boolean {
            var remainingEU = tEU
            var freeCap: Long = 0

            GTUtility.filterValidMTEs(hatches).forEach {
                freeCap += it.maxEUStore() - it.baseMetaTileEntity.storedEU
                if (freeCap > 0) {
                    if (remainingEU >= freeCap) {
                        it.euVar = it.maxEUStore()
                        remainingEU -= freeCap
                    } else {
                        it.euVar = it.baseMetaTileEntity.storedEU + remainingEU
                        return true
                    }
                }
            }
            return false
        }

        if (processHatches(mDynamoHatches, tEU)) return true
        if (processHatches(mExoticDynamoHatches, tEU)) return true

        return false
    }

    override fun clearHatches() {
        super.clearHatches()
        mExoticDynamoHatches.clear()
    }

    /** Set the structure definition for the machine. */
    protected abstract fun genStructureDefinition(): IStructureDefinition<T>

    @Suppress("UNCHECKED_CAST")
    final override fun getStructureDefinition(): IStructureDefinition<T> =
        cachedStructureDefs.getOrPut(this::class) { genStructureDefinition() }
            as IStructureDefinition<T>

    /** Controller Block and Meta, used for calculating casing texture index. */
    protected abstract val sControllerBlock: Pair<Block, Int>

    protected open val sControllerCasingIndex: Int
        get() = GTUtility.getCasingTextureIndex(sControllerBlock.first, sControllerBlock.second)

    /** Controller Icon for active state, left is normal, right is glow. */
    protected open val sControllerIconActive: Pair<Textures.BlockIcons, Textures.BlockIcons>
        get() = OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE to OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW

    /** Controller Icon for inactive state, left is normal, right is glow. */
    protected open val sControllerIcon: Pair<Textures.BlockIcons, Textures.BlockIcons>
        get() = OVERLAY_FRONT_ASSEMBLY_LINE to OVERLAY_FRONT_ASSEMBLY_LINE_GLOW

    override fun getTexture(
        baseMetaTileEntity: IGregTechTileEntity,
        side: ForgeDirection,
        facing: ForgeDirection,
        colorIndex: Int,
        active: Boolean,
        redstoneLevel: Boolean,
    ): Array<ITexture> =
        if (side != facing) {
            arrayOf(Textures.BlockIcons.getCasingTextureForId(sControllerCasingIndex))
        } else if (active) {
            arrayOf(
                Textures.BlockIcons.getCasingTextureForId(sControllerCasingIndex),
                TextureFactory
                    .builder()
                    .addIcon(sControllerIconActive.first)
                    .extFacing()
                    .build(),
                TextureFactory
                    .builder()
                    .addIcon(sControllerIconActive.second)
                    .extFacing()
                    .glow()
                    .build(),
            )
        } else {
            arrayOf(
                Textures.BlockIcons.getCasingTextureForId(sControllerCasingIndex),
                TextureFactory
                    .builder()
                    .addIcon(sControllerIcon.first)
                    .extFacing()
                    .build(),
                TextureFactory
                    .builder()
                    .addIcon(sControllerIcon.second)
                    .extFacing()
                    .glow()
                    .build(),
            )
        }

    override fun getMaxEfficiency(aStack: ItemStack?): Int = 100_00

    override fun getDamageToComponent(aStack: ItemStack?): Int = 0

    override fun doRandomMaintenanceDamage(): Boolean = false

    override fun explodesOnComponentBreak(aStack: ItemStack?): Boolean = false

    override fun supportsVoidProtection(): Boolean = true

    override fun supportsInputSeparation(): Boolean = true

    override fun supportsBatchMode(): Boolean = true

    override fun supportsSingleRecipeLocking(): Boolean = true

    override fun createProcessingLogic(): ProcessingLogic? =
        object : ProcessingLogic() {
            override fun process(): CheckRecipeResult {
                setEuModifier(rEuModifier)
                setSpeedBonus(rDurationModifier)
                setOverclock(rOverclockType.timeDec, rOverclockType.powerInc)
                return super.process()
            }
        }.setMaxParallelSupplier(::rMaxParallel)

    protected open val rOverclockType: OverclockType
        @OverrideOnly get() = OverclockType.Normal

    protected open val rEuModifier
        @OverrideOnly get() = 1.0

    protected open val rDurationModifier
        @OverrideOnly get() = 1.0

    protected open val rMaxParallel
        @OverrideOnly get() = 1

    protected fun consumeFluid(
        fluid: Fluid,
        amount: Int,
    ): Boolean {
        if (storedFluids.isNullOrEmpty()) return false
        var amount = amount

        // Check if there is enough fluid stored.
        storedFluids
            .filter { it idEqual fluid }
            .sumOf { it.amount }
            .let { if (it < amount) return false }

        storedFluids.forEach {
            if (it.getFluid() idEqual fluid) {
                if (it.amount >= amount) {
                    it.amount -= amount
                    return true
                } else {
                    amount -= it.amount
                    it.amount = 0
                }
            }
        }

        return amount <= 0
    }

    protected fun outputItem(
        item: ItemStack?,
        amount: Long,
    ): Boolean {
        if (item == null || amount <= 0) return false
        var amount = amount

        if (amount <= Int.MAX_VALUE) {
            addOutput(item.copy() size amount.toInt())
        } else {
            while (amount > Int.MAX_VALUE) {
                addOutput(item.copy() size Int.MAX_VALUE)
                amount -= Int.MAX_VALUE
            }
        }

        updateSlots()

        return true
    }

    protected fun outputFluid(
        fluid: FluidStack?,
        amount: Long,
    ): Boolean {
        if (fluid == null || amount <= 0) return false
        var amount = amount
        if (amount <= Int.MAX_VALUE) {
            addOutput(fluid.copy() size amount.toInt())
        } else {
            while (amount > Int.MAX_VALUE) {
                addOutput(fluid.copy() size Int.MAX_VALUE)
                amount -= Int.MAX_VALUE
            }
        }

        updateSlots()

        return true
    }

    protected fun outputItemToAENetwork(
        item: ItemStack?,
        amount: Long,
    ) {
        if ((item == null) || (amount <= 0)) return
        var amount = amount

        mOutputBusses
            .firstOrNull { it is MTEHatchOutputBusME }
            ?.let {
                it as MTEHatchOutputBusME
                if (amount < Int.MAX_VALUE) {
                    it.store(item.copy().apply { stackSize = amount.toInt() })
                } else {
                    // For item stacks > Int max.
                    while (amount >= Int.MAX_VALUE) {
                        it.store(item.copy().apply { stackSize = Int.MAX_VALUE })
                        amount -= Int.MAX_VALUE.toLong()
                    }

                    if (amount > 0) {
                        it.store(item.copy().apply { stackSize = amount.toInt() })
                    }
                }
            }
    }

    protected fun outputFluidToAENetwork(
        fluid: FluidStack?,
        amount: Long,
    ) {
        if ((fluid == null) || (amount <= 0)) return
        var amount = amount

        mOutputHatches
            .firstOrNull { it is MTEHatchOutputME }
            ?.let {
                it as MTEHatchOutputME
                if (amount < Int.MAX_VALUE) {
                    it.tryFillAE(fluid.copy().apply { this.amount = amount.toInt() })
                } else {
                    // For fluidStacks > Int max.
                    while (amount >= Int.MAX_VALUE) {
                        it.tryFillAE(fluid.copy().apply { this.amount = Int.MAX_VALUE })
                        amount -= Int.MAX_VALUE.toLong()
                    }

                    if (amount > 0) {
                        it.tryFillAE(fluid.copy().apply { this.amount = amount.toInt() })
                    }
                }
            }
    }

    /** Format Double number as % */
    protected fun Double.formatPercent() = "%.3f%%".format(this * 100)

    final override fun getInfoData(): Array<String> =
        super.getInfoData() +
            arrayOf(
                "${AQUA}最大并行: ${GOLD}${GTUtility.formatNumbers(rMaxParallel.toLong())}",
                "${AQUA}速度乘数: ${GOLD}${rDurationModifier.formatPercent()}",
                "${AQUA}功率乘数: ${GOLD}${rEuModifier.formatPercent()}",
            ) +
            getInfoDataExtra()

    /** Extra information added after [getInfoData] */
    @OverrideOnly
    protected open fun getInfoDataExtra(): Array<String> = arrayOf()

    protected fun MultiblockTooltipBuilder.addMachineTypeLocalized(): MultiblockTooltipBuilder =
        this.addMachineType(
            if (StatCollector.canTranslate("$mName.type")) {
                StatCollector.translateToLocal("$mName.type")
            } else {
                StatCollector.translateToLocal("$mName.name")
            },
        )

    protected fun MultiblockTooltipBuilder.addInfoLocalized(key: String): MultiblockTooltipBuilder =
        this.addInfo(StatCollector.translateToLocal(key))

    protected fun MultiblockTooltipBuilder.addInfoLocalized(index: Int): MultiblockTooltipBuilder =
        this.addInfo(StatCollector.translateToLocal("$mName.info.$index"))

    protected fun MultiblockTooltipBuilder.addInfoListLocalized(untilIndex: Int): MultiblockTooltipBuilder =
        apply {
            (0..untilIndex)
                .map { StatCollector.translateToLocal("$mName.info.$it") }
                .forEach { this.addInfo(it) }
        }

    protected fun MultiblockTooltipBuilder.addInfoLocalized(
        key: String,
        vararg args: Any,
    ): MultiblockTooltipBuilder = this.addInfo(StatCollector.translateToLocalFormatted(key, *args))

    protected fun MultiblockTooltipBuilder.addInfoLocalized(
        index: Int,
        vararg args: Any,
    ): MultiblockTooltipBuilder = this.addInfo(StatCollector.translateToLocalFormatted("$mName.info.$index", *args))

    protected fun MultiblockTooltipBuilder.addChangeModeByScrewdriver(): MultiblockTooltipBuilder =
        this.addInfo(CommonString.ChangeModeByScrewdriver)

    protected fun MultiblockTooltipBuilder.addInputBus(aDot: Int = 1): MultiblockTooltipBuilder =
        this.addInputBus(CommonString.BluePrintInfo, aDot)

    protected fun MultiblockTooltipBuilder.addInputHatch(aDot: Int = 1): MultiblockTooltipBuilder =
        this.addInputHatch(CommonString.BluePrintInfo, aDot)

    protected fun MultiblockTooltipBuilder.addOutputBus(aDot: Int = 1): MultiblockTooltipBuilder =
        this.addOutputBus(CommonString.BluePrintInfo, aDot)

    protected fun MultiblockTooltipBuilder.addOutputHatch(aDot: Int = 1): MultiblockTooltipBuilder =
        this.addOutputHatch(CommonString.BluePrintInfo, aDot)

    protected fun MultiblockTooltipBuilder.addEnergyHatch(aDot: Int = 1): MultiblockTooltipBuilder =
        this.addEnergyHatch(CommonString.BluePrintInfo, aDot)

    protected fun MultiblockTooltipBuilder.addDynamo(aDot: Int = 1): MultiblockTooltipBuilder =
        this.addDynamoHatch(CommonString.BluePrintInfo, aDot)

    override fun getWailaBody(
        itemStack: ItemStack?,
        currentTip: MutableList<String>,
        accessor: IWailaDataAccessor,
        config: IWailaConfigHandler?,
    ) {
        super.getWailaBody(itemStack, currentTip, accessor, config)
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
    }

    override fun saveNBTData(aNBT: NBTTagCompound?) {
        super.saveNBTData(aNBT)
    }

    override fun loadNBTData(aNBT: NBTTagCompound?) {
        super.loadNBTData(aNBT)
    }

    /** Mode Container for switching between modes. */
    open class ModeContainerPrimitive(
        val size: Int,
    ) {
        /** Current index of the mode. */
        var index = 0
            protected set

        /** Get the next index, return to 0 if reached the end. */
        fun next(): Int {
            index = (index + 1) % size
            return index
        }

        /** Get the previous index, return to the end if reached 0. */
        fun prev(): Int {
            index = (index - 1 + size) % size
            return index
        }

        /** Save the mode index to NBT. */
        fun saveNBTData(
            aNBT: NBTTagCompound,
            key: String,
        ) {
            aNBT.setInteger(key, index)
        }

        /** Load the mode index from NBT. */
        fun loadNBTData(
            aNBT: NBTTagCompound?,
            key: String,
        ) {
            index = aNBT?.getInteger(key) ?: 0
        }
    }

    /**
     * Mode Container for switching between modes, with additional type support.
     *
     * @param T Type of the modes.
     */
    class ModeContainer<T>(
        private val modes: Array<out T>,
    ) : ModeContainerPrimitive(modes.size) {
        companion object {
            /** Create a ModeContainer with vararg modes. */
            inline fun <reified T> of(vararg modes: T): ModeContainer<T> = ModeContainer(modes)
        }

        /** Current mode. */
        val current: T
            get() = modes[index]

        val all: Collection<T>
            get() = modes.toCollection(mutableListOf())

        /** Get the next mode, return to the first mode if reached the end. */
        fun nextMode(): T {
            next()
            return modes[index]
        }

        /** Get the previous mode, return to the last mode if reached the first. */
        fun prevMode(): T {
            prev()
            return modes[index]
        }
    }
}
