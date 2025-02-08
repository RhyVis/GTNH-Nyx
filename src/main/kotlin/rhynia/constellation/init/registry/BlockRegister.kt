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

    CelItemList.EOHCoreT1
      .register(EyeOfHarmonyCoreCasing, 0, "${BOLD}${AQUA}允许执行鸿蒙之眼T1配方")
    CelItemList.EOHCoreT2
      .register(EyeOfHarmonyCoreCasing, 1, "${BOLD}${AQUA}允许执行鸿蒙之眼T2配方")
    CelItemList.EOHCoreT3
      .register(EyeOfHarmonyCoreCasing, 2, "${BOLD}${AQUA}允许执行鸿蒙之眼T3配方")
    CelItemList.EOHCoreT4
      .register(EyeOfHarmonyCoreCasing, 3, "${BOLD}${AQUA}允许执行鸿蒙之眼T4配方")
    CelItemList.EOHCoreT5
      .register(EyeOfHarmonyCoreCasing, 4, "${BOLD}${AQUA}允许执行鸿蒙之眼T5配方")
    CelItemList.EOHCoreT6
      .register(EyeOfHarmonyCoreCasing, 5, "${BOLD}${AQUA}允许执行鸿蒙之眼T6配方")
    CelItemList.EOHCoreT7
      .register(EyeOfHarmonyCoreCasing, 6, "${BOLD}${AQUA}允许执行鸿蒙之眼T7配方")
    CelItemList.EOHCoreT8
      .register(EyeOfHarmonyCoreCasing, 7, "${BOLD}${AQUA}允许执行鸿蒙之眼T8配方")
    CelItemList.EOHCoreT9
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
