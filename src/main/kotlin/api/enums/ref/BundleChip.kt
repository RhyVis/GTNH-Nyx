package rhynia.nyx.api.enums.ref

import gregtech.api.enums.Mods.GoodGenerator
import net.minecraft.item.ItemStack
import rhynia.nyx.api.interfaces.RefHelper
import rhynia.nyx.api.util.copyAmountAnyway
import rhynia.nyx.api.util.getItem

@Suppress("UNUSED")
enum class BundleChip : RefHelper {
    ULV,
    LV,
    MV,
    HV,
    EV,
    IV,
    LuV,
    ZPM,
    UV,
    UHV,
    UEV,
    UIV,
    UMV,
    UXV,
    MAX,
    ;

    companion object {
        private const val CIRCUIT_WRAP_NAME = "circuitWrap"
    }

    override fun getItemStack(amount: Int): ItemStack =
        GoodGenerator
            .getItem(CIRCUIT_WRAP_NAME, 1, ordinal)
            .copyAmountAnyway(amount)
}
