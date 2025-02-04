package vis.rhynia.nova.common.item.container

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.gui.GuiScreen.isShiftKeyDown
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.BLUE
import net.minecraft.util.EnumChatFormatting.RED
import vis.rhynia.nova.client.NovaTab.TabItem

class NovaItemUltimate() : Item() {
  init {
    setCreativeTab(TabItem)
    setUnlocalizedName("ultimate")
    setTextureName("nova:ultimate")
  }

  @SideOnly(Side.CLIENT)
  override fun getSubItems(item: Item, tab: CreativeTabs, list: MutableList<ItemStack>) {
    list.add(ItemStack(item, 1, 0))
  }

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      itemStack: ItemStack,
      player: EntityPlayer,
      toolTip: MutableList<String>,
      advancedToolTips: Boolean
  ) {
    if (isShiftKeyDown()) {
      toolTip.add("${BLUE}你${AQUA}会${RED}更进一步${BLUE}吗?")
    } else {
      toolTip.add("你可能已经触及这个游戏的最终阶段了.")
    }
  }
}
