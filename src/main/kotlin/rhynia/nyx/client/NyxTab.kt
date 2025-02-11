package rhynia.nyx.client

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import rhynia.nyx.init.registry.ItemRecord

object NyxTab {
  val TabItem: CreativeTabs =
      object : CreativeTabs("Nyx: Item") {
        @SideOnly(Side.CLIENT) override fun getTabIconItem(): Item = ItemRecord.MetaItem01
      }

  val TabBlock: CreativeTabs =
      object : CreativeTabs("Nyx: Block") {
        @SideOnly(Side.CLIENT) override fun getTabIconItem(): Item = ItemRecord.MetaItem01
      }
}
