package rhynia.nyx.init.registry

import net.minecraft.item.Item
import rhynia.nyx.common.item.base.AbstractMetaItem
import rhynia.nyx.common.item.container.NyxItemUltimate

object ItemRecord {
  val ItemUltimate: Item = NyxItemUltimate()
  val MetaItem01: Item = object : AbstractMetaItem("MetaItem01") {}
  val MetaItem02: Item = object : AbstractMetaItem("MetaItem02") {}
}
