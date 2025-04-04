package rhynia.nyx.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.common.material.NyxMaterials

fun NBTTagCompound.getItem(key: String): ItemStack =
    GTUtility.loadItem(this, key) ?: debugItem(
        "ItemStack not found in NBT: $key",
    )

fun NBTTagCompound.getFluid(key: String): FluidStack = GTUtility.loadFluid(this, key) ?: NyxMaterials.Null.getFluid(1)

fun NBTTagCompound.setFluid(
    key: String,
    fluidStack: FluidStack,
) {
    NBTTagCompound().let {
        fluidStack.writeToNBT(it)
        this.setTag(key, it)
    }
}
