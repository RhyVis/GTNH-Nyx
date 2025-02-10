package rhynia.constellation.init.registry

import net.minecraft.item.Item
import rhynia.constellation.common.item.base.AbstractMetaItem
import rhynia.constellation.common.item.container.CelItemUltimate

object ItemRecord {
  val ItemUltimate: Item = CelItemUltimate()
  val MetaItem01: Item = object : AbstractMetaItem("MetaItem01") {}
  val MetaItem02: Item = object : AbstractMetaItem("MetaItem02") {}
}
