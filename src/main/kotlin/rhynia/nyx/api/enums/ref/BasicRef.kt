package rhynia.nyx.api.enums.ref

import gregtech.api.enums.Mods.EternalSingularity
import gregtech.api.enums.Mods.GTPlusPlus
import gregtech.api.enums.Mods.GoodGenerator
import gregtech.api.enums.Mods.KekzTech
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import rhynia.nyx.common.container.NyxItemList

@Suppress("unused", "SpellCheckingInspection")
object BasicRef {
  fun getSingularity(amount: Int): ItemStack =
      GTModHandler.getModItem(
          EternalSingularity.ID,
          "eternal_singularity",
          amount.toLong(),
          NyxItemList.TestItem01.get(amount.toLong()))

  fun getSingularityCatalyst(): ItemStack =
      GTModHandler.getModItem(
          EternalSingularity.ID, "eternal_singularity", 0, NyxItemList.TestItem01.get(0))

  fun getFluidCore(tier: Int, amount: Int): ItemStack {
    val tierMeta = MathHelper.clamp_int(tier, 1, 10) - 1
    return GTModHandler.getModItem(GoodGenerator.ID, "fluidCore", amount.toLong(), tierMeta)
  }

  fun getYOTCell(tier: Int, amount: Int): ItemStack {
    val tierMeta = MathHelper.clamp_int(tier, 1, 10) - 1
    return GTModHandler.getModItem(
        GoodGenerator.ID, "yottaFluidTankCells", amount.toLong(), tierMeta)
  }

  fun getTFFTCell(tier: Int, amount: Int): ItemStack {
    val tierMeta = MathHelper.clamp_int(tier, 1, 10)
    return GTModHandler.getModItem(
        KekzTech.ID, "kekztech_tfftstoragefield_block", amount.toLong(), tierMeta)
  }

  fun getQuantumAnomaly(amount: Int): ItemStack {
    return GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", amount.toLong(), 32105)
  }

  fun getFusionMatrixCatalyst(): ItemStack {
    return GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 0, 32100)
  }
}
