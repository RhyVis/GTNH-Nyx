package vis.rhynia.nova.common.item

import net.minecraft.item.Item
import vis.rhynia.nova.client.NovaTab.TabMetaItem01
import vis.rhynia.nova.common.item.container.NovaMetaItem01

object ItemRecord {
  val MetaItem01: Item =
      NovaMetaItem01("MetaItem01Base", "MetaItem01", TabMetaItem01)
          .setTextureName("append:MetaItem01/0")
}
