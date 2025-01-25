package vis.rhynia.nova.common.block

import cpw.mods.fml.common.registry.GameRegistry
import vis.rhynia.nova.common.block.base.BlockBaseItem01
import vis.rhynia.nova.common.block.casing.EyeOfHarmonyCore
import vis.rhynia.nova.common.block.casing.EyeOfHarmonyCoreItem
import vis.rhynia.nova.common.loader.container.NovaItemList

object BlockRegister {
  private fun registerBlocks() {
    GameRegistry.registerBlock(
        BlockRecord.MetaBlock01,
        BlockBaseItem01::class.java,
        BlockRecord.MetaBlock01.unlocalizedName)
    GameRegistry.registerBlock(
        BlockRecord.EyeOfHarmonyCoreCasing,
        EyeOfHarmonyCoreItem::class.java,
        BlockRecord.EyeOfHarmonyCoreCasing.unlocalizedName)
  }

  // spotless:off
  private fun registerBlockContainers() {
    NovaItemList.TestMetaBlock01
      .set(BlockBaseItem01.initMetaBlock("测试方块", 0, arrayOf("我相信它没什么用")))

    NovaItemList.EOHCoreT1
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 0))
    NovaItemList.EOHCoreT2
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 1))
    NovaItemList.EOHCoreT3
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 2))
    NovaItemList.EOHCoreT4
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 3))
    NovaItemList.EOHCoreT5
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 4))
    NovaItemList.EOHCoreT6
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 5))
    NovaItemList.EOHCoreT7
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 6))
    NovaItemList.EOHCoreT8
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 7))
    NovaItemList.EOHCoreT9
      .set(EyeOfHarmonyCore.eyeOfHarmonyCoreCasingMeta( 8))
  }
  // spotless:on

  fun register() {
    registerBlocks()
    registerBlockContainers()
  }
}
