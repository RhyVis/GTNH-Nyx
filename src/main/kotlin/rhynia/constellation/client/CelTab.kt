package rhynia.constellation.client

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import rhynia.constellation.init.registry.ItemRecord

object CelTab {
  val TabItem: CreativeTabs =
      object : CreativeTabs("Constellation: Item") {
        @SideOnly(Side.CLIENT) override fun getTabIconItem(): Item = ItemRecord.MetaItem01
      }

  val TabBlock: CreativeTabs =
      object : CreativeTabs("Constellation: Block") {
        @SideOnly(Side.CLIENT) override fun getTabIconItem(): Item = ItemRecord.MetaItem01
      }
}
