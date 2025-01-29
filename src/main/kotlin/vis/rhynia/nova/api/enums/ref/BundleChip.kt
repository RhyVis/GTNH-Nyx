package vis.rhynia.nova.api.enums.ref

import goodgenerator.main.GoodGenerator
import gregtech.api.enums.Materials
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.api.interfaces.RefHelper
import vis.rhynia.nova.api.util.StackUtil.copyAmountUnsafe
import vis.rhynia.nova.common.loader.container.NovaItemList

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
                  NovaItemList.TestItem01.get(1))
              .copyAmountUnsafe(amount)
      else
          GTModHandler.getModItem(
              GoodGenerator.MOD_ID,
              CIRCUIT_WRAP_NAME,
              1,
              this.ordinal,
              NovaItemList.TestItem01.get(1))

  override fun getFluidStack(amount: Int): FluidStack = Materials.Water.getFluid(1)
}
