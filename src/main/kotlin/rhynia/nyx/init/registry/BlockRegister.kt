package rhynia.nyx.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.util.EnumChatFormatting.*
import rhynia.nyx.api.util.RegistryUtil
import rhynia.nyx.common.block.AbstractMetaBlock
import rhynia.nyx.common.block.NyxMetaBlockItem
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.init.registry.BlockRecord.EyeOfHarmonyCoreCasing
import rhynia.nyx.init.registry.BlockRecord.MetaBlock01

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
            GameRegistry.registerBlock(it, NyxMetaBlockItem::class.java, it.unlocalizedName)
          }

  // spotless:off
  private fun registerBlockContainers() {
    NyxItemList.TestMetaBlock01
      .register(MetaBlock01, 0, arrayOf("我相信它没什么用", "仅作测试之用"))

    arrayOf(
      NyxItemList.EOHCoreT1,
      NyxItemList.EOHCoreT2,
      NyxItemList.EOHCoreT3,
      NyxItemList.EOHCoreT4,
      NyxItemList.EOHCoreT5,
      NyxItemList.EOHCoreT6,
      NyxItemList.EOHCoreT7,
      NyxItemList.EOHCoreT8,
      NyxItemList.EOHCoreT9
    ).forEachIndexed { index, item ->
      item.register(EyeOfHarmonyCoreCasing, index, "${BOLD}${AQUA}允许执行鸿蒙之眼T${index + 1}配方")
    }
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
