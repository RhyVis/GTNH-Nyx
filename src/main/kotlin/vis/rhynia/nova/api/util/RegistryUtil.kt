package vis.rhynia.nova.api.util

import net.minecraft.item.ItemStack
import vis.rhynia.nova.common.item.base.AbstractMetaItem

object RegistryUtil {
  fun registerMetaItem(item: AbstractMetaItem, meta: Int, tooltip: Array<String>?): ItemStack {
    tooltip?.let { item.setTooltips(meta, it) }
    return item.registerVariant(meta)
  }

  fun registerMetaItem(item: AbstractMetaItem, meta: Int, tooltip: String): ItemStack =
      registerMetaItem(item, meta, arrayOf(tooltip))
}
