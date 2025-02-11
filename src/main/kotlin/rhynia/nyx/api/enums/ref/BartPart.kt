package rhynia.nyx.api.enums.ref

import gregtech.api.enums.Materials
import gregtech.api.enums.Mods.BartWorks
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.Log
import rhynia.nyx.api.interfaces.RefHelper
import rhynia.nyx.api.util.StackUtil.copyAmountUnsafe
import rhynia.nyx.common.container.NyxItemList

@Suppress("unused", "SpellCheckingInspection")
enum class BartPart(private val deprecated: Boolean = false) : RefHelper {
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

  companion object {
    const val WRAP_CIRCUIT_OFFSET = 32699
    const val BW_GEN_ITEM_NAME = "gt.bwMetaGeneratedItem0"
  }

  override fun getItemStack(amount: Int): ItemStack =
      if (deprecated) {
        Log.error("Attempting to get deprecated item $this!")
        NyxItemList.TestItem01.get(1)
      } else if (amount > 64) {
        GTModHandler.getModItem(
                BartWorks.ID,
                BW_GEN_ITEM_NAME,
                1,
                this.ordinal + WRAP_CIRCUIT_OFFSET,
                NyxItemList.TestItem01.get(1))
            .copyAmountUnsafe(amount)
      } else {
        GTModHandler.getModItem(
            BartWorks.ID,
            BW_GEN_ITEM_NAME,
            amount.toLong(),
            this.ordinal + WRAP_CIRCUIT_OFFSET,
            NyxItemList.TestItem01.get(1))
      }

  override fun getFluidStack(amount: Int): FluidStack = Materials.Water.getFluid(1)
}
