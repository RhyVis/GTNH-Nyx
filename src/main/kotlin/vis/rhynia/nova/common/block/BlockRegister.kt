package vis.rhynia.nova.common.block

import cpw.mods.fml.common.registry.GameRegistry
import vis.rhynia.nova.common.block.base.BlockBaseItem01
import vis.rhynia.nova.common.loader.container.NovaItemList

object BlockRegister {
  private fun registerBlocks() {
    GameRegistry.registerBlock(
        BlockRecord.MetaBlock01,
        BlockBaseItem01::class.java,
        BlockRecord.MetaBlock01.unlocalizedName)
  }

  // spotless:off
  private fun registerBlockContainers() {
    NovaItemList.TestMetaBlock01
      .set(BlockBaseItem01.initMetaBlock("测试方块", 0, arrayOf("我相信它没什么用")))
  }
  // spotless:on

  fun register() {
    registerBlocks()
    registerBlockContainers()
  }
}
