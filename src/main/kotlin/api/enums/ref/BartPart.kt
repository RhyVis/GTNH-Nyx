package rhynia.nyx.api.enums.ref

import gregtech.api.enums.Materials
import gregtech.api.enums.Mods.BartWorks
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.ModLogger
import rhynia.nyx.api.interfaces.RefHelper
import rhynia.nyx.api.util.debugItem
import rhynia.nyx.api.util.getItem

@Suppress("UNUSED", "SpellCheckingInspection")
enum class BartPart(
    private val deprecated: Boolean = false,
) : RefHelper {
    LivingBioChip,
    LivingCrystalChip,
    OpticalRam,
    OpticalCPUCasing,
    OpticalCPU,
    OpticalBoard,
    OpticalCard,
    OpticalInductor,
    AdvancedInductor,
    OpticalCapacitor,
    OpticalTransistor,
    OpticalDiode,
    OpticalResistor,
    BioCell,
    StemCell,
    BioProcessor,
    StemProcessor,
    AdvancedCrystalRaw,
    AdvancedCrystalSOC,
    CrystalCPU,
    QBit,
    NanoCPU,
    IC_Q,
    IC_P,
    IC_N,
    IC_L,
    IC_UL,
    IC_UH,
    IC_H,
    SSOC,
    IC,
    ASOC,
    SOC,
    CPU,
    NOR,
    NAND,
    RAM,
    ILC,
    AdvancedCapacitor,
    AdvancedTransistor,
    AdvancedDiode,
    AdvancedResistor,
    BasicCapacitor,
    BasicTransistor,
    BasicDiode,
    BasicInductor,
    BasicResistor,
    BioBoard,
    BioBoardRaw,
    PlasticBoard,
    PlasticBoardRaw,
    WetwareBoard,
    WetwareBoardRaw,
    EliteBoardRawUseless(true),
    EliteBoard,
    DelicateBoard,
    DelicateBoardRaw,
    AdvancedBoard,
    AdvancedBoardRawUseless(true),
    GoodBoard,
    GoodBoardRaw,
    BasicBoard,
    BasicBoardRawUseless(true),
    Lapotron,
    CrystalRaw,
    EliteBoardRaw,
    AdvancedBoardRaw,
    BasicBoardRaw,
    ;

    private val meta: Int
        get() = this.ordinal + WRAP_CIRCUIT_OFFSET

    companion object {
        private const val WRAP_CIRCUIT_OFFSET = 32699
        private const val BW_GEN_ITEM_NAME = "gt.bwMetaGeneratedItem0"
    }

    override fun getItemStack(amount: Int): ItemStack =
        if (deprecated) {
            ModLogger.error("Attempting to get deprecated item $this!")
            debugItem("Trying to get deprecated bw item $this")
        } else {
            BartWorks.getItem(BW_GEN_ITEM_NAME, amount, meta) {
                debugItem("Failed to get item $this from BartWorks, with null result")
            }
        }

    override fun getFluidStack(amount: Int): FluidStack = Materials.Water.getFluid(1)
}
