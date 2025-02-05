package vis.rhynia.nova.common.block

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import kotlin.jvm.java
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.BOLD
import vis.rhynia.nova.api.util.RegistryUtil
import vis.rhynia.nova.common.block.BlockRecord.EyeOfHarmonyCoreCasing
import vis.rhynia.nova.common.block.BlockRecord.MetaBlock01
import vis.rhynia.nova.common.block.base.AbstractMetaBlock
import vis.rhynia.nova.common.block.base.NovaMetaBlockItem
import vis.rhynia.nova.common.loader.container.NovaItemList

@Suppress("unused")
object BlockRegister {
  fun register() {
    registerBlocks()
    registerBlockContainers()
  }

  private fun registerBlocks() =
      arrayOf(
              MetaBlock01,
              EyeOfHarmonyCoreCasing,
          )
          .forEach {
            GameRegistry.registerBlock(it, NovaMetaBlockItem::class.java, it.unlocalizedName)
          }

  // spotless:off
  private fun registerBlockContainers() {
    NovaItemList.TestMetaBlock01
      .register(MetaBlock01, 0, arrayOf("我相信它没什么用", "仅作测试之用"))

    NovaItemList.EOHCoreT1
      .register(EyeOfHarmonyCoreCasing, 0, "${BOLD}${AQUA}允许执行鸿蒙之眼T1配方")
    NovaItemList.EOHCoreT2
      .register(EyeOfHarmonyCoreCasing, 1, "${BOLD}${AQUA}允许执行鸿蒙之眼T2配方")
    NovaItemList.EOHCoreT3
      .register(EyeOfHarmonyCoreCasing, 2, "${BOLD}${AQUA}允许执行鸿蒙之眼T3配方")
    NovaItemList.EOHCoreT4
      .register(EyeOfHarmonyCoreCasing, 3, "${BOLD}${AQUA}允许执行鸿蒙之眼T4配方")
    NovaItemList.EOHCoreT5
      .register(EyeOfHarmonyCoreCasing, 4, "${BOLD}${AQUA}允许执行鸿蒙之眼T5配方")
    NovaItemList.EOHCoreT6
      .register(EyeOfHarmonyCoreCasing, 5, "${BOLD}${AQUA}允许执行鸿蒙之眼T6配方")
    NovaItemList.EOHCoreT7
      .register(EyeOfHarmonyCoreCasing, 6, "${BOLD}${AQUA}允许执行鸿蒙之眼T7配方")
    NovaItemList.EOHCoreT8
      .register(EyeOfHarmonyCoreCasing, 7, "${BOLD}${AQUA}允许执行鸿蒙之眼T8配方")
    NovaItemList.EOHCoreT9
      .register(EyeOfHarmonyCoreCasing, 8, "${BOLD}${AQUA}允许执行鸿蒙之眼T9配方")
  }
  // spotless:on

  private fun IItemContainer.register(block: AbstractMetaBlock, meta: Int) {
    this.set(RegistryUtil.registerMetaBlock(block, meta, null))
  }

  private fun IItemContainer.register(block: AbstractMetaBlock, meta: Int, tooltip: Array<String>) {
    this.set(RegistryUtil.registerMetaBlock(block, meta, tooltip))
  }

  private fun IItemContainer.register(block: AbstractMetaBlock, meta: Int, tooltip: String) {
    this.set(RegistryUtil.registerMetaBlock(block, meta, tooltip))
  }
}
