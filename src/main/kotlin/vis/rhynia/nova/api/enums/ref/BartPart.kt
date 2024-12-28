package vis.rhynia.nova.api.enums.ref

import gregtech.api.enums.Materials
import gregtech.api.enums.Mods.BartWorks
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.api.interfaces.RefHelper
import vis.rhynia.nova.common.NovaItemList

@Suppress("unused")
enum class BartPart : RefHelper {
  LivingBioChip,
  LivingCrystalChip,
  Opt_Ram,
  Opt_CPUCasing,
  Opt_CPU,
  Opt_Board,
  Opt_Card,
  Opt_Inductor,
  Adv_Inductor,
  Opt_Capacitor,
  Opt_Transistor,
  Opt_Diode,
  Opt_Resistor,
  Bio_Cell,
  Stem_Cell,
  Bio_Processor,
  Stem_Processor,
  AdvCrystal_Raw,
  AdvCrystal_SOC,
  Crystal_CPU,
  Part_QBit,
  Part_NanoCPU,
  Part_IC_Q,
  Part_IC_P,
  Part_IC_N,
  Part_IC_L,
  Part_IC_UL,
  Part_IC_UH,
  Part_IC_H,
  Part_SSOC,
  Part_IC,
  Part_ASOC,
  Part_SOC,
  Part_CPU,
  Part_NOR,
  Part_NAND,
  Part_RAM,
  Part_ILC,
  Adv_Capacitor,
  Adv_Transistor,
  Adv_Diode,
  Adv_Resistor,
  Basic_Capacitor,
  Basic_Transistor,
  Basic_Diode,
  Basic_Inductor,
  Basic_Resistor,
  Bio_Board,
  Bio_Board_Raw,
  Plastic_Board,
  Plastic_Board_Raw,
  Wetware_Board,
  Wetware_Board_Raw,
  Elite_Board_Raw_Useless,
  Elite_Board,
  Delicate_Board,
  Delicate_Board_Raw,
  Adv_Board,
  Adv_Board_Raw_Useless,
  Good_Board,
  Good_Board_Raw,
  Basic_Board,
  Basic_Board_Raw_Useless,
  Lapotron,
  Crystal_Raw,
  Elite_Board_Raw,
  Adv_Board_Raw,
  Basic_Board_Raw,
  ;

  companion object {
    const val WRAP_CIRCUIT_OFFSET = 32699
    const val BW_GEN_ITEM_NAME = "gt.bwMetaGeneratedItem0"
  }

  override fun getItemStack(amount: Int): ItemStack =
      if (amount > 64) {
        GTUtility.copyAmountUnsafe(
            amount,
            GTModHandler.getModItem(
                BartWorks.ID,
                BW_GEN_ITEM_NAME,
                1,
                this.ordinal + WRAP_CIRCUIT_OFFSET,
                NovaItemList.TestItem01.get(1)))
      } else {
        GTModHandler.getModItem(
            BartWorks.ID,
            BW_GEN_ITEM_NAME,
            amount.toLong(),
            this.ordinal + WRAP_CIRCUIT_OFFSET,
            NovaItemList.TestItem01.get(1))
      }

  override fun getFluidStack(amount: Int): FluidStack = Materials.Water.getFluid(1)
}
