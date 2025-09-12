@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.common.material.NyxMaterials

inline fun NBTTagCompound.getItem(key: String): ItemStack =
    GTUtility.loadItem(this, key) ?: debugItem(
        "ItemStack not found in NBT: $key",
    )

inline fun NBTTagCompound.getItemOrNull(key: String): ItemStack? = GTUtility.loadItem(this, key)

inline fun NBTTagCompound.setItem(
    key: String,
    itemStack: ItemStack,
) {
    GTUtility.saveItem(this, key, itemStack)
}

inline fun NBTTagCompound.getFluid(key: String): FluidStack = GTUtility.loadFluid(this, key) ?: NyxMaterials.Null.getFluid(1)

inline fun NBTTagCompound.setFluid(
    key: String,
    fluidStack: FluidStack,
) {
    NBTTagCompound().let {
        fluidStack.writeToNBT(it)
        this.setTag(key, it)
    }
}
