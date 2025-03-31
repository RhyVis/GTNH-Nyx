package rhynia.nyx.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack

/**
 * Set the stack size of an [ItemStack] but not copy it.
 */
infix fun ItemStack.size(amount: Int): ItemStack = this.apply { stackSize = amount }

/**
 * Copy the [ItemStack] with a new stack size using [copyAmount]
 */
fun ItemStack.copyAmount(amount: Int): ItemStack = GTUtility.copyAmount(amount, this)

/**
 * Copy the [ItemStack] with a new stack size using [copyAmountUnsafe]
 */
fun ItemStack.copyAmountUnsafe(amount: Int): ItemStack = GTUtility.copyAmountUnsafe(amount, this)

/**
 * Copy the [ItemStack] with a new stack size using [copyAmount] or [copyAmountUnsafe] depending on the amount.
 *
 * If the amount is less than or equal to 64, use [copyAmount], otherwise use [copyAmountUnsafe].
 */
fun ItemStack.copyAmountAnyway(amount: Int): ItemStack =
    if (amount <= 64) {
        GTUtility.copyAmount(amount, this)
    } else {
        GTUtility.copyAmountUnsafe(amount, this)
    }
