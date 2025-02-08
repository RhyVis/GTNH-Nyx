package rhynia.constellation.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import rhynia.constellation.api.util.RegistryUtil
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.item.ItemRecord
import rhynia.constellation.common.item.base.AbstractMetaItem

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
      CelItemList.TestItem01
        .register(it, 0, arrayOf(
          "我相信它没什么用",
          "如果在配方中发现了它，代表又一个Null被按下不表"
        ))
      CelItemList.LapotronMatrix
        .register(it, 1, "兰波顿密度达到了极致")
      CelItemList.CrystalMatrix
        .register(it, 2, "用于批量生产各类晶体芯片")
      CelItemList.DenseMicaInsulatorFoil
        .register(it, 3, "16倍的绝缘性能!")
      CelItemList.PreTesseract
        .register(it, 4, "高维工程学的试验产品")

      // Resource
      CelItemList.AstriumInfinityGem.register(it, 1001,
        arrayOf(
          "Aμⁿ",
          "能量络合物"
        ))
      CelItemList.AstriumInfinityComplex.register(it, 1002,
        arrayOf(
          "-[Aμⁿ-Aμⁿ]-",
          "${EnumChatFormatting.DARK_RED}高维能量络合物"
        ))
      CelItemList.AstriumInfinityGauge.register(it, 1003,
        arrayOf(
          "┌Aμⁿ-Aμⁿ┐",
          "└Aμⁿ-Aμⁿ┘",
          "${EnumChatFormatting.DARK_PURPLE}时空之空洞"
        ))

      // Lens
      CelItemList.LensAstriumInfinity
        .register(it, 3001, "能量聚集于此")
      CelItemList.LensAstriumMagic
        .register(it, 3002, "我想它能自行产生魔力")
      CelItemList.LensPrimoium
        .register(it, 3003, "异世界的能量?")
      CelItemList.LensOriginium
        .register(it, 3004, "操作时请注意源石尘")
    }

    (ItemRecord.MetaItem02 as AbstractMetaItem).let {
      CelItemList.Calibration
        .register(it, 0, "记录着GT:NH世界运行的原理")
      CelItemList.AssemblyDTPF
        .register(it, 1, "内置永恒锻炉")
    }

    // Special Item
    CelItemList.ItemUltimate.set(ItemStack(ItemRecord.ItemUltimate, 1))
  }
  // spotless:on

  private fun IItemContainer.register(item: AbstractMetaItem, meta: Int, tooltip: Array<String>) {
    this.set(RegistryUtil.registerMetaItem(item, meta, tooltip))
  }

  private fun IItemContainer.register(item: AbstractMetaItem, meta: Int, tooltip: String) {
    this.set(RegistryUtil.registerMetaItem(item, meta, tooltip))
  }
}
