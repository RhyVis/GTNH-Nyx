package vis.rhynia.nova.common.item

import cpw.mods.fml.common.registry.GameRegistry
import vis.rhynia.nova.common.NovaItemList
import vis.rhynia.nova.common.item.container.NovaMetaItem01

object ItemRegister {
  private fun registerItems() {
    arrayOf(ItemRecord.MetaItem01).forEach { GameRegistry.registerItem(it, it.unlocalizedName) }
  }

  // spotless:off
  private fun registerItemContainers() {
    NovaItemList.TestItem01
      .set(NovaMetaItem01.initItem("调试占位", 0, arrayOf("我相信它没什么用")))
    NovaItemList.LapotronMatrix
      .set(NovaMetaItem01.initItem("兰波顿矩阵", 1, arrayOf("兰波顿密度达到了极致")))
    NovaItemList.CrystalMatrix
      .set(NovaMetaItem01.initItem("晶体矩阵", 2, arrayOf("用于批量生产各类晶体芯片")))
    NovaItemList.DenseMicaInsulatorFoil
      .set(NovaMetaItem01.initItem("致密绝缘云母箔", 3, arrayOf("16倍的绝缘性能!")))
    NovaItemList.PreTesseract
      .set(NovaMetaItem01.initItem("准超立方体", 4, arrayOf("高维工程学的试验产品")))
  }
  // spotless:on

  fun register() {
    registerItems()
    registerItemContainers()
  }
}
