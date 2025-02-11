package rhynia.nyx.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack

object StackUtil {
  fun ItemStack.copyAmountUnsafe(amount: Int): ItemStack = GTUtility.copyAmountUnsafe(amount, this)
}
