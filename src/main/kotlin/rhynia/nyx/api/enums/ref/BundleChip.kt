package rhynia.nyx.api.enums.ref

import goodgenerator.main.GoodGenerator
import gregtech.api.enums.Materials
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.api.interfaces.RefHelper
import rhynia.nyx.api.util.StackUtil.copyAmountUnsafe
import rhynia.nyx.common.container.NyxItemList

@Suppress("unused")
enum class BundleChip : RefHelper {
  ULV,
  LV,
  MV,
  HV,
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
  MAX;

  companion object {
    const val CIRCUIT_WRAP_NAME = "circuitWrap"
  }

  override fun getItemStack(amount: Int): ItemStack =
      if (amount > 64)
          GTModHandler.getModItem(
                  GoodGenerator.MOD_ID,
                  CIRCUIT_WRAP_NAME,
                  1,
                  this.ordinal,
                  NyxItemList.TestItem01.get(1))
              .copyAmountUnsafe(amount)
      else
          GTModHandler.getModItem(
              GoodGenerator.MOD_ID,
              CIRCUIT_WRAP_NAME,
              1,
              this.ordinal,
              NyxItemList.TestItem01.get(1))

  override fun getFluidStack(amount: Int): FluidStack = Materials.Water.getFluid(1)
}
