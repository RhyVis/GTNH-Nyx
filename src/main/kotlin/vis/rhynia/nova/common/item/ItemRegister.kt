package vis.rhynia.nova.common.item

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.DARK_PURPLE
import net.minecraft.util.EnumChatFormatting.DARK_RED
import vis.rhynia.nova.common.item.container.NovaMetaItem01
import vis.rhynia.nova.common.item.container.NovaMetaItem02
import vis.rhynia.nova.common.loader.container.NovaItemList

object ItemRegister {
  private fun registerItems() {
    arrayOf(ItemRecord.MetaItem01, ItemRecord.MetaItem02, ItemRecord.ItemUltimate).forEach {
      GameRegistry.registerItem(it, it.unlocalizedName)
    }
  }

  // spotless:off
  private fun registerItemContainers() {
    // Production
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

    // Resource
    NovaItemList.AstriumInfinityGem.set(
      NovaMetaItem01.initItem("星极", 1001, arrayOf("Aμⁿ", "能量络合物")))
    NovaItemList.AstriumInfinityComplex.set(
      NovaMetaItem01.initItem("星矩", 1002, arrayOf(
        "-[Aμⁿ-Aμⁿ]-",
        "${DARK_RED}高维能量络合物"
      )))
    NovaItemList.AstriumInfinityGauge.set(
      NovaMetaItem01.initItem("星规", 1003, arrayOf(
        "┌Aμⁿ-Aμⁿ┐",
        "└Aμⁿ-Aμⁿ┘",
        "${DARK_PURPLE}时空之空洞"
      )))

    // Lens
    NovaItemList.LensAstriumInfinity
      .set(NovaMetaItem01.initItem("星极天体透镜", 3001, arrayOf("能量聚集于此")))
    NovaItemList.LensAstriumMagic
      .set(NovaMetaItem01.initItem("星芒魔力透镜", 3002, arrayOf("我想它能自行产生魔力")))
    NovaItemList.LensPrimoium
      .set(NovaMetaItem01.initItem("原石透镜", 3003, arrayOf("异世界的能量?")))
    NovaItemList.LensOriginium
      .set(NovaMetaItem01.initItem("源石透镜", 3004, arrayOf("操作时请注意源石尘")))

    // region Meta Item 02

    NovaItemList.Calibration
      .set(NovaMetaItem02.initItem("标定指示", 0, arrayOf("记录着GTNH世界运行的原理")))
    NovaItemList.AssemblyDTPF
      .set(NovaMetaItem02.initItem("套件-DTPF", 1, arrayOf("内置永恒锻炉")))

    // endregion

    // Special Item
    NovaItemList.ItemUltimate.set(ItemStack(ItemRecord.ItemUltimate, 1))
  }
  // spotless:on

  fun register() {
    registerItems()
    registerItemContainers()
  }
}
