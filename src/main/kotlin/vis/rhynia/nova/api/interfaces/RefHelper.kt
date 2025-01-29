package vis.rhynia.nova.api.interfaces

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack

interface RefHelper {
  fun getItemStack(amount: Int): ItemStack
  fun getItemStack(amount: Long): ItemStack = getItemStack(amount.toInt())
  fun getFluidStack(amount: Int): FluidStack
  fun getFluidStack(amount: Long): FluidStack = getFluidStack(amount.toInt())
}
