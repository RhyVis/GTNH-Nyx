package rhynia.nyx.common.item

import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import rhynia.nyx.ModLogger
import rhynia.nyx.api.util.RegistryUtil
import rhynia.nyx.init.registry.ItemRecord

class NyxDebugItem : AbstractMetaItem("DebugItem") {
    override fun getItemStackDisplayName(stack: ItemStack?): String? =
        stack?.let { "Debug Item - ${it.itemDamage}" } ?: "Debug Item - Unknown"

    override val iconName: String get() = "ultimate"

    override fun getMetadata(meta: Int): Int = 0

    override fun getUnlocalizedName(stack: ItemStack?): String = super.getUnlocalizedName()

    override fun getIconFromDamage(meta: Int): IIcon? = itemIcon

    override fun registerIcons(register: IIconRegister) {
        itemIcon = register.registerIcon(iconString)
    }

    companion object {
        private var cursor = 0
        private val nextCursor get() = cursor++

        private val noInfo = arrayOf("No info provided.")

        fun reportInfo(vararg info: String): ItemStack {
            val cur = nextCursor
            val info = info.takeIf { it.isNotEmpty() } ?: noInfo
            ModLogger.warn("Debug item $cur info: ${info.joinToString(", ")}")
            return RegistryUtil.registerMetaItem(ItemRecord.DebugItem, cur, info.takeIf { it.isNotEmpty() } ?: noInfo)
        }
    }
}
