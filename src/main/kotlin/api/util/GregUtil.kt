package rhynia.nyx.api.util

import gregtech.api.enums.Mods
import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import rhynia.nyx.api.enums.NyxMods
import rhynia.nyx.common.NyxItemList

fun Mods.getItem(
    name: String,
    count: Int = 1,
    meta: Int = 0,
    replacement: (() -> ItemStack)? = null,
): ItemStack =
    if (count <= 64) {
        GTModHandler.getModItem(this.ID, name, count.toLong(), meta) ?: replacement?.invoke() ?: NyxItemList.Dummy
    } else {
        GTModHandler.getModItem(this.ID, name, 1, meta)?.copyAmountUnsafe(count) ?: replacement?.invoke() ?: NyxItemList.Dummy
    }

fun NyxMods.getItem(
    name: String,
    count: Int = 1,
    meta: Int = 0,
    replacement: (() -> ItemStack)? = null,
): ItemStack =
    if (count <= 64) {
        GTModHandler.getModItem(this.ID, name, count.toLong(), meta) ?: replacement?.invoke() ?: NyxItemList.Dummy
    } else {
        GTModHandler.getModItem(this.ID, name, 1, meta)?.copyAmountUnsafe(count) ?: replacement?.invoke() ?: NyxItemList.Dummy
    }

fun IItemContainer.getAmountUnsafe(amount: Int): ItemStack = this.get(1).copyAmountUnsafe(amount)
