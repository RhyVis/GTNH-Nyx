package vis.rhynia.nova.common.item

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.DARK_PURPLE
import net.minecraft.util.EnumChatFormatting.DARK_RED
import vis.rhynia.nova.api.util.RegistryUtil.registerMetaItem
import vis.rhynia.nova.common.item.ItemRecord.ItemUltimate
import vis.rhynia.nova.common.item.ItemRecord.MetaItem01
import vis.rhynia.nova.common.item.ItemRecord.MetaItem02
import vis.rhynia.nova.common.item.base.AbstractMetaItem
import vis.rhynia.nova.common.loader.container.NovaItemList

object ItemRegister {
  fun register() {
    registerItems()
    registerItemContainers()
  }

  private fun registerItems() {
    arrayOf(MetaItem01, MetaItem02, ItemUltimate).forEach {
      GameRegistry.registerItem(it, it.unlocalizedName)
    }
  }

  // spotless:off
  private fun registerItemContainers() {
    (MetaItem01 as AbstractMetaItem).let {
      // Production
      NovaItemList.TestItem01
        .register(it, 0, arrayOf(
          "我相信它没什么用",
          "如果在配方中发现了它，代表又一个Null被按下不表"
        ))
      NovaItemList.LapotronMatrix
        .register(it, 1, "兰波顿密度达到了极致")
      NovaItemList.CrystalMatrix
        .register(it, 2, "用于批量生产各类晶体芯片")
      NovaItemList.DenseMicaInsulatorFoil
        .register(it, 3, "16倍的绝缘性能!")
      NovaItemList.PreTesseract
        .register(it, 4, "高维工程学的试验产品")

      // Resource
      NovaItemList.AstriumInfinityGem.register(it, 1001,
        arrayOf(
          "Aμⁿ",
          "能量络合物"
        ))
      NovaItemList.AstriumInfinityComplex.register(it, 1002,
        arrayOf(
          "-[Aμⁿ-Aμⁿ]-",
          "${DARK_RED}高维能量络合物"
        ))
      NovaItemList.AstriumInfinityGauge.register(it, 1003,
        arrayOf(
          "┌Aμⁿ-Aμⁿ┐",
          "└Aμⁿ-Aμⁿ┘",
          "${DARK_PURPLE}时空之空洞"
        ))

      // Lens
      NovaItemList.LensAstriumInfinity
        .register(it, 3001, "能量聚集于此")
      NovaItemList.LensAstriumMagic
        .register(it, 3002, "我想它能自行产生魔力")
      NovaItemList.LensPrimoium
        .register(it, 3003, "异世界的能量?")
      NovaItemList.LensOriginium
        .register(it, 3004, "操作时请注意源石尘")
    }

    (MetaItem02 as AbstractMetaItem).let {
      NovaItemList.Calibration
        .register(it, 0, "记录着GT:NH世界运行的原理")
      NovaItemList.AssemblyDTPF
        .register(it, 1, "内置永恒锻炉")
    }

    // Special Item
    NovaItemList.ItemUltimate.set(ItemStack(ItemUltimate, 1))
  }
  // spotless:on

  private fun IItemContainer.register(item: AbstractMetaItem, meta: Int, tooltip: Array<String>) {
    this.set(registerMetaItem(item, meta, tooltip))
  }

  private fun IItemContainer.register(item: AbstractMetaItem, meta: Int, tooltip: String) {
    this.set(registerMetaItem(item, meta, tooltip))
  }
}
