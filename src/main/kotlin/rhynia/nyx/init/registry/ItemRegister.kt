package rhynia.nyx.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import rhynia.nyx.api.util.RegistryUtil
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.item.base.AbstractMetaItem

object ItemRegister {
  fun register() {
    registerItems()
    registerItemContainers()
  }

  private fun registerItems() {
    arrayOf(ItemRecord.MetaItem01, ItemRecord.MetaItem02, ItemRecord.ItemUltimate).forEach {
      GameRegistry.registerItem(it, it.unlocalizedName)
    }
  }

  // spotless:off
  private fun registerItemContainers() {
    (ItemRecord.MetaItem01 as AbstractMetaItem).let {
      // Production
      NyxItemList.TestItem01
        .register(it, 0, arrayOf(
          "我相信它没什么用",
          "如果在配方中发现了它，代表又一个Null被按下不表"
        ))
      NyxItemList.LapotronMatrix
        .register(it, 1, "兰波顿密度达到了极致")
      NyxItemList.CrystalMatrix
        .register(it, 2, "用于批量生产各类晶体芯片")
      NyxItemList.DenseMicaInsulatorFoil
        .register(it, 3, "16倍的绝缘性能!")
      NyxItemList.PreTesseract
        .register(it, 4, "高维工程学的试验产品")

      // Resource
      NyxItemList.AstriumInfinityGem.register(it, 1001,
        arrayOf(
          "Aμⁿ",
          "能量络合物"
        ))
      NyxItemList.AstriumInfinityComplex.register(it, 1002,
        arrayOf(
          "-[Aμⁿ-Aμⁿ]-",
          "${EnumChatFormatting.DARK_RED}高维能量络合物"
        ))
      NyxItemList.AstriumInfinityGauge.register(it, 1003,
        arrayOf(
          "┌Aμⁿ-Aμⁿ┐",
          "└Aμⁿ-Aμⁿ┘",
          "${EnumChatFormatting.DARK_PURPLE}时空之空洞"
        ))

      // Lens
      NyxItemList.LensAstriumInfinity
        .register(it, 3001, "能量聚集于此")
      NyxItemList.LensAstriumMagic
        .register(it, 3002, "我想它能自行产生魔力")
      NyxItemList.LensPrimoium
        .register(it, 3003, "异世界的能量?")
      NyxItemList.LensOriginium
        .register(it, 3004, "操作时请注意源石尘")
    }

    (ItemRecord.MetaItem02 as AbstractMetaItem).let {
      NyxItemList.Calibration
        .register(it, 0, "记录着GT:NH世界运行的原理")
      NyxItemList.AssemblyDTPF
        .register(it, 1, "内置永恒锻炉")
    }

    // Special Item
    NyxItemList.ItemUltimate.set(ItemStack(ItemRecord.ItemUltimate, 1))
  }
  // spotless:on

  private fun IItemContainer.register(item: AbstractMetaItem, meta: Int, tooltip: Array<String>) {
    this.set(RegistryUtil.registerMetaItem(item, meta, tooltip))
  }

  private fun IItemContainer.register(item: AbstractMetaItem, meta: Int, tooltip: String) {
    this.set(RegistryUtil.registerMetaItem(item, meta, tooltip))
  }
}
