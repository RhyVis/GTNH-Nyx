package vis.rhynia.nova.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterials

object NBTUtil {
  fun NBTTagCompound.getItem(key: String): ItemStack =
      GTUtility.loadItem(this, key) ?: NovaItemList.TestItem01.get(1)

  fun NBTTagCompound.getFluid(key: String): FluidStack =
      GTUtility.loadFluid(this, key) ?: NovaMaterials.Null.getFluid(1)

  fun NBTTagCompound.setFluid(key: String, fluidStack: FluidStack) {
    NBTTagCompound().let {
      fluidStack.writeToNBT(it)
      this.setTag(key, it)
    }
  }
}
