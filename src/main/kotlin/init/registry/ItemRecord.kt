package rhynia.nyx.init.registry

import net.minecraft.item.Item
import rhynia.nyx.common.item.AbstractMetaItem
import rhynia.nyx.common.item.NyxDebugItem
import rhynia.nyx.common.item.NyxItemUltimate

object ItemRecord {
    val ItemUltimate: Item = NyxItemUltimate()
    val DebugItem = NyxDebugItem()
    val MetaItem01 = object : AbstractMetaItem("MetaItem01") {}
    val MetaItem02 = object : AbstractMetaItem("MetaItem02") {}
}
