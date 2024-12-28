package vis.rhynia.nova.api.interfaces

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack

interface RefHelper {
  fun getItemStack(amount: Int): ItemStack
  fun getFluidStack(amount: Int): FluidStack
}
