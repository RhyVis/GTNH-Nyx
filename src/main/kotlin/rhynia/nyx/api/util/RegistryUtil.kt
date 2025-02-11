package rhynia.nyx.api.util

import net.minecraft.item.ItemStack
import rhynia.nyx.common.block.AbstractMetaBlock
import rhynia.nyx.common.item.AbstractMetaItem

@Suppress("unused")
object RegistryUtil {
  fun registerMetaItem(item: AbstractMetaItem, meta: Int): ItemStack {
    return item.registerVariant(meta)
  }

  fun registerMetaItem(item: AbstractMetaItem, meta: Int, tooltip: Array<String>?): ItemStack {
    tooltip?.let { item.setTooltips(meta, it) }
    return item.registerVariant(meta)
  }

  fun registerMetaItem(item: AbstractMetaItem, meta: Int, tooltip: String): ItemStack =
      registerMetaItem(item, meta, arrayOf(tooltip))

  fun registerMetaBlock(block: AbstractMetaBlock, meta: Int, tooltip: Array<String>?): ItemStack {
    tooltip?.let { block.setTooltips(meta, it) }
    return block.registerVariant(meta)
  }

  fun registerMetaBlock(block: AbstractMetaBlock, meta: Int, tooltip: String): ItemStack =
      registerMetaBlock(block, meta, arrayOf(tooltip))
}
