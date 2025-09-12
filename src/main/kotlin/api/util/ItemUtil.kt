@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.util

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import rhynia.nyx.common.item.NyxDebugItem

/**
 * Set the stack size of an [ItemStack] but not copy it.
 */
inline infix fun ItemStack.size(amount: Int): ItemStack = apply { stackSize = amount }

/**
 * Set the stack size of an [ItemStack] but not copy it.
 */
inline infix fun ItemStack.size(amount: Long): ItemStack = apply { stackSize = amount.toInt() }

/**
 * Copy the [ItemStack] with a new stack size using [copyAmount]
 */
inline fun ItemStack.copyAmount(amount: Int): ItemStack = GTUtility.copyAmount(amount, this)

/**
 * Copy the [ItemStack] with a new stack size using [copyAmount]
 */
inline fun ItemStack.copyAmount(amount: Long): ItemStack = GTUtility.copyAmount(amount.toInt(), this)

/**
 * Copy the [ItemStack] with a new stack size using [copyAmountUnsafe]
 */
inline fun ItemStack.copyAmountUnsafe(amount: Int): ItemStack = GTUtility.copyAmountUnsafe(amount, this)

/**
 * Copy the [ItemStack] with a new stack size using [copyAmountUnsafe]
 */
inline fun ItemStack.copyAmountUnsafe(amount: Long): ItemStack = GTUtility.copyAmountUnsafe(amount.toInt(), this)

/**
 * Copy the [ItemStack] with a new stack size using [copyAmount] or [copyAmountUnsafe] depending on the amount.
 *
 * If the amount is less than or equal to 64, use [copyAmount], otherwise use [copyAmountUnsafe].
 */
inline fun ItemStack.copyAmountAnyway(amount: Int): ItemStack =
    if (amount <= 64) {
        GTUtility.copyAmount(amount, this)
    } else {
        GTUtility.copyAmountUnsafe(amount, this)
    }

/**
 * Create a new [NyxDebugItem] stack with the info given.
 */
inline fun debugItem(vararg info: String) = NyxDebugItem.reportInfo(*info)
