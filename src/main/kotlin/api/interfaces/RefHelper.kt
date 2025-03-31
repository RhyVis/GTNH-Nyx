package rhynia.nyx.api.interfaces

import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.material.NyxMaterials

/** RefHelper interface for getting ItemStack and FluidStack */
interface RefHelper {
    fun getItemStack(amount: Int): ItemStack = NyxItemList.Dummy

    fun getItemStack(amount: Long): ItemStack = getItemStack(amount.toInt())

    fun getFluidStack(amount: Int): FluidStack = NyxMaterials.Null.getFluid(1)

    fun getFluidStack(amount: Long): FluidStack = getFluidStack(amount.toInt())
}
