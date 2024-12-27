package vis.rhynia.nova.common.item

import cpw.mods.fml.common.registry.GameRegistry
import vis.rhynia.nova.common.NovaItemList
import vis.rhynia.nova.common.item.container.NovaMetaItem01

object ItemRegister {
  private fun registerItems() {
    arrayOf(ItemRecord.MetaItem01).forEach { GameRegistry.registerItem(it, it.unlocalizedName) }
  }

  private fun registerItemContainers() {
    NovaItemList.TestItem01.set(NovaMetaItem01.initItem("调试占位", 0, arrayOf("我相信它没什么用")))
  }

  fun register() {
    registerItems()
    registerItemContainers()
  }
}
