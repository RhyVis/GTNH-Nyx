package rhynia.nyx.api.util

import gregtech.api.enums.Mods
import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import rhynia.nyx.api.enums.NyxMods

fun Mods.getItem(
    name: String,
    count: Int = 1,
    meta: Int = 0,
    replacement: (() -> ItemStack)? = null,
): ItemStack =
    getModItem(
        this.ID,
        name,
        count,
        meta,
        replacement,
    )

fun NyxMods.getItem(
    name: String,
    count: Int = 1,
    meta: Int = 0,
    replacement: (() -> ItemStack)? = null,
): ItemStack =
    getModItem(
        this.ID,
        name,
        count,
        meta,
        replacement,
    )

private fun getModItem(
    id: String,
    name: String,
    count: Int = 1,
    meta: Int = 0,
    replacement: (() -> ItemStack)? = null,
): ItemStack =
    if (count <= 64) {
        GTModHandler.getModItem(id, name, count.toLong(), meta) ?: replacement?.invoke() ?: debugItem(
            "Mod item $name:$meta not found in $id",
        )
    } else {
        GTModHandler.getModItem(id, name, 1, meta)?.copyAmountUnsafe(count) ?: replacement?.invoke() ?: debugItem(
            "Mod item $name:$meta not found in $id",
        )
    }

fun IItemContainer.getAmountUnsafe(amount: Int): ItemStack = this.get(1).copyAmountUnsafe(amount)
