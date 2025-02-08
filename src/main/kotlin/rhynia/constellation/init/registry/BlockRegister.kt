package rhynia.constellation.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.util.EnumChatFormatting.*
import rhynia.constellation.api.util.RegistryUtil
import rhynia.constellation.common.block.BlockRecord.EyeOfHarmonyCoreCasing
import rhynia.constellation.common.block.BlockRecord.MetaBlock01
import rhynia.constellation.common.block.base.AbstractMetaBlock
import rhynia.constellation.common.block.base.CelMetaBlockItem
import rhynia.constellation.common.container.CelItemList

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
            GameRegistry.registerBlock(it, CelMetaBlockItem::class.java, it.unlocalizedName)
          }

  // spotless:off
  private fun registerBlockContainers() {
    CelItemList.TestMetaBlock01
      .register(MetaBlock01, 0, arrayOf("我相信它没什么用", "仅作测试之用"))

    arrayOf(
      CelItemList.EOHCoreT1,
      CelItemList.EOHCoreT2,
      CelItemList.EOHCoreT3,
      CelItemList.EOHCoreT4,
      CelItemList.EOHCoreT5,
      CelItemList.EOHCoreT6,
      CelItemList.EOHCoreT7,
      CelItemList.EOHCoreT8,
      CelItemList.EOHCoreT9
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
