package rhynia.constellation.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.CelMaterials

object NBTUtil {
  fun NBTTagCompound.getItem(key: String): ItemStack =
      GTUtility.loadItem(this, key) ?: CelItemList.TestItem01.get(1)

  fun NBTTagCompound.getFluid(key: String): FluidStack =
      GTUtility.loadFluid(this, key) ?: CelMaterials.Null.getFluid(1)

  fun NBTTagCompound.setFluid(key: String, fluidStack: FluidStack) {
    NBTTagCompound().let {
      fluidStack.writeToNBT(it)
      this.setTag(key, it)
    }
  }
}
