package vis.rhynia.nova.client

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import vis.rhynia.nova.common.item.ItemRecord

object NovaTab {
  val TabMetaItem01: CreativeTabs =
      object : CreativeTabs("Nova: Item") {
        @SideOnly(Side.CLIENT)
        override fun getTabIconItem(): Item {
          return ItemRecord.MetaItem01
        }
      }
}
