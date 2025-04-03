package rhynia.nyx.api.enums.ref

import goodgenerator.main.GoodGenerator
import gregtech.api.enums.GTValues
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods.BartWorks
import gregtech.api.enums.Mods.GTPlusPlus
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTOreDictUnificator
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import org.jetbrains.annotations.Range
import rhynia.nyx.ModLogger
import rhynia.nyx.api.enums.RecipeValues
import rhynia.nyx.api.enums.RecipeValues.INGOT
import rhynia.nyx.common.NyxItemList
import tectech.thing.CustomItemList

/** Enum class for tiered components and materials. */
@Suppress("unused", "SpellCheckingInspection")
enum class Tier(
    private val material: Materials,
    val recipeVol: Long,
) {
    ULV(Materials.ULV, RecipeValues.RECIPE_ULV),
    LV(Materials.LV, RecipeValues.RECIPE_LV),
    MV(Materials.MV, RecipeValues.RECIPE_MV),
    HV(Materials.HV, RecipeValues.RECIPE_HV),
    EV(Materials.EV, RecipeValues.RECIPE_EV),
    IV(Materials.IV, RecipeValues.RECIPE_IV),
    LuV(Materials.LuV, RecipeValues.RECIPE_LuV),
    ZPM(Materials.ZPM, RecipeValues.RECIPE_ZPM),
    UV(Materials.UV, RecipeValues.RECIPE_UV),
    UHV(Materials.UHV, RecipeValues.RECIPE_UHV),
    UEV(Materials.UEV, RecipeValues.RECIPE_UEV),
    UIV(Materials.UIV, RecipeValues.RECIPE_UIV),
    UMV(Materials.UMV, RecipeValues.RECIPE_UMV),
    UXV(Materials.UXV, RecipeValues.RECIPE_UXV),
    MAX(Materials.MAX, RecipeValues.RECIPE_MAX),
    ;

    /** Enum class for tiered components. */
    enum class Component(
        private val enumNamePrefix: String,
    ) {
        ElectricMotor("Electric_Motor"),
        ElectricPiston("Electric_Piston"),
        ElectricPump("Electric_Pump"),
        RobotArm("Robot_Arm"),
        ConveyorModule("Conveyor_Module"),
        Emitter("Emitter"),
        Sensor("Sensor"),
        FieldGenerator("Field_Generator"),
        ;

        fun ofTier(tier: Tier): IItemContainer = ItemList.valueOf("${this.enumNamePrefix}_$tier")
    }

    enum class Hatch {
        Dynamo,
        Energy,
        Energy4A,
        Energy16A,
        Energy64A,
        LaserEnergy,
        LaserDynamo,
        WirelessDynamo,
        WirelessEnergy,
        WirelessEnergy4A,
        WirelessEnergy16A,
        WirelessEnergy64A,
        WirelessLaser,
    }

    val voltage: Long
        get() = GTValues.V[ordinal]

    val voltageRecipe: Long
        get() = GTValues.VP[ordinal]

    val superConductor: SuperConductorPart
        get() =
            when (this) {
                ULV,
                LV,
                -> {
                    ModLogger.error(
                        "$this tier is too low for standard Super Conductor material! Using MV instead.",
                    )
                    SuperConductorPart.MV
                }

                UXV,
                MAX,
                -> {
                    ModLogger.error(
                        "$this tier is too high for standard Super Conductor material! Using UMV instead.",
                    )
                    SuperConductorPart.UMV
                }

                else -> SuperConductorPart.entries[this.ordinal - 2]
            }

    val solderMaterial: SolderMaterial
        get() =
            when (this) {
                ULV,
                LV,
                MV,
                HV,
                EV,
                -> SolderMaterial.T1

                UEV,
                UIV,
                UMV,
                UXV,
                MAX,
                -> SolderMaterial.T3

                else -> SolderMaterial.T2
            }

    private val fallbackStack: ItemStack
        get() = NyxItemList.TestItem01.get(1)

    private fun fail(info: String): ItemStack {
        ModLogger.error("Attempting to get $info, but it doesn't exist!")
        return fallbackStack
    }

    fun getSolder(amount: Int): FluidStack = solderMaterial.getFluidStack(amount)

    fun getIngotSolder(amount: Int): FluidStack = solderMaterial.getFluidStack(amount * INGOT)

    fun getCircuit(amount: Int): ItemStack = GTOreDictUnificator.get(OrePrefixes.circuit, material, amount.toLong())

    fun getCircuitWrap(amount: Int): ItemStack = BundleChip.entries[ordinal].getItemStack(amount)

    fun getComponent(
        component: Component,
        amount: Int,
    ): ItemStack =
        if (this == ULV) {
            fail("ULV component of ${component.name}")
        } else {
            component.ofTier(this).get(amount.toLong(), fallbackStack)
        }

    fun getCoil(amount: Int): ItemStack =
        when (this) {
            UEV,
            UIV,
            UMV,
            UXV,
            MAX,
            -> fail("$this coil")

            else -> ItemList.valueOf("${this}_Coil").get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getComponentAssemblyCasing(amount: Int): ItemStack =
        if (this == ULV) {
            fail("ULV component assembly casing")
        } else {
            GTModHandler.getModItem(
                GoodGenerator.MOD_ID,
                "componentAssemblylineCasing",
                amount.toLong(),
                this.ordinal - 1,
            )
        }

    fun getGlass(amount: Int): ItemStack =
        when (this) {
            ULV,
            LV,
            MV,
            HV,
            -> GTModHandler.getModItem(BartWorks.ID, "BW_GlasBlocks", amount.toLong(), 0)

            UMV,
            UXV,
            MAX,
            -> GTModHandler.getModItem(BartWorks.ID, "BW_GlasBlocks2", amount.toLong())

            else ->
                GTModHandler.getModItem(
                    BartWorks.ID,
                    "BW_GlasBlocks",
                    amount.toLong(),
                    this.ordinal - 3,
                )
        }

    fun getBufferCore(amount: Int): ItemStack =
        when (this) {
            UHV,
            UEV,
            UIV,
            UMV,
            UXV,
            MAX,
            -> GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", amount.toLong())

            else ->
                GTModHandler.getModItem(
                    GTPlusPlus.ID,
                    "item.itemBufferCore${this.ordinal + 1}",
                    amount.toLong(),
                )
        }

    fun getHatch(
        hatch: Hatch,
        amount: Int,
    ) = when (hatch) {
        Hatch.Dynamo -> getDynamoHatch(amount)
        Hatch.Energy -> getEnergyHatch(amount)
        Hatch.Energy4A -> getEnergyHatch4A(amount)
        Hatch.Energy16A -> getEnergyHatch16A(amount)
        Hatch.Energy64A -> getEnergyHatch64A(amount)
        Hatch.LaserEnergy -> getLaserTarget(1, amount)
        Hatch.LaserDynamo -> getLaserTarget(1, amount)
        Hatch.WirelessDynamo -> getDynamoWireless(amount)
        Hatch.WirelessEnergy -> getEnergyWireless(amount)
        Hatch.WirelessEnergy4A -> getEnergyWireless4A(amount)
        Hatch.WirelessEnergy16A -> getEnergyWireless16A(amount)
        Hatch.WirelessEnergy64A -> getEnergyWireless64A(amount)
        Hatch.WirelessLaser -> getLaserWireless(1, amount)
    }

    fun getDynamoHatch(amount: Int): ItemStack =
        when (this) {
            MAX -> fail("MAX dynamo hatch")
            else ->
                ItemList
                    .valueOf("Hatch_Dynamo_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getEnergyHatch(amount: Int): ItemStack =
        when (this) {
            MAX -> fail("MAX energy hatch")
            else ->
                ItemList
                    .valueOf("Hatch_Energy_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getEnergyHatch4A(amount: Int): ItemStack =
        when (this) {
            EV,
            IV,
            LuV,
            ZPM,
            UV,
            UHV,
            UEV,
            UIV,
            UMV,
            UXV,
            ->
                CustomItemList
                    .valueOf("eM_energyMulti4_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))

            else -> fail("$this 4A energy hatch")
        }

    fun getEnergyHatch16A(amount: Int): ItemStack =
        when (this) {
            EV,
            IV,
            LuV,
            ZPM,
            UV,
            UHV,
            UEV,
            UIV,
            UMV,
            UXV,
            ->
                CustomItemList
                    .valueOf("eM_energyMulti16_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))

            else -> fail("$this 16A energy hatch")
        }

    fun getEnergyHatch64A(amount: Int): ItemStack =
        when (this) {
            EV,
            IV,
            LuV,
            ZPM,
            UV,
            UHV,
            UEV,
            UIV,
            UMV,
            UXV,
            ->
                CustomItemList
                    .valueOf("eM_energyMulti64_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))

            else -> fail("$this 64A energy hatch")
        }

    fun getDynamoWireless(amount: Int): ItemStack =
        ItemList
            .valueOf("Wireless_Dynamo_Energy_$this")
            .get(amount.toLong(), NyxItemList.TestItem01.get(1))

    fun getEnergyWireless(amount: Int): ItemStack =
        ItemList
            .valueOf("Wireless_Hatch_Energy_$this")
            .get(amount.toLong(), NyxItemList.TestItem01.get(1))

    fun getEnergyWireless4A(amount: Int): ItemStack =
        when (this) {
            ULV,
            LV,
            MV,
            HV,
            -> fail("$this 4A energy wireless")

            else ->
                CustomItemList
                    .valueOf("eM_energyWirelessMulti4_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getEnergyWireless16A(amount: Int): ItemStack =
        when (this) {
            ULV,
            LV,
            MV,
            HV,
            -> fail("$this 16A energy wireless")

            else ->
                CustomItemList
                    .valueOf("eM_energyWirelessMulti16_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getEnergyWireless64A(amount: Int): ItemStack =
        when (this) {
            ULV,
            LV,
            MV,
            HV,
            -> fail("$this 64A energy wireless")

            else ->
                CustomItemList
                    .valueOf("eM_energyWirelessMulti64_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getLaserTarget(
        tier:
            @Range(from = 1, to = 7)
            Int,
        amount: Int,
    ): ItemStack =
        when (this) {
            ULV,
            LV,
            MV,
            HV,
            EV,
            MAX,
            -> fail("$this level $tier laser target")

            else ->
                CustomItemList
                    .valueOf("eM_energyTunnel${tier}_$this")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))
        }

    fun getLaserWireless(
        tier:
            @Range(from = 1, to = 7)
            Int,
        amount: Int,
    ): ItemStack =
        when (this) {
            ULV,
            LV,
            MV,
            HV,
            EV,
            MAX,
            -> fail("$this level $tier laser wireless")

            UXV ->
                CustomItemList
                    .valueOf("eM_energyWirelessTunnel${tier}_UXV")
                    .get(amount.toLong(), NyxItemList.TestItem01.get(1))

            else -> NyxItemList.Dummy
        }
}
