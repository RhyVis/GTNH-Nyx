package vis.rhynia.nova.common.block.casing

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.BOLD
import vis.rhynia.nova.client.NovaTab
import vis.rhynia.nova.common.block.base.BlockBaseItem01

class EyeOfHarmonyCoreItem(block: Block) : BlockBaseItem01(block) {
  init {
    setCreativeTab(NovaTab.TabBlock01)
  }

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      aItemStack: ItemStack?,
      player: EntityPlayer?,
      list: List<String?>?,
      b: Boolean
  ) {
    if (null == aItemStack) return
    list?.apply {
      plus("${BOLD}${AQUA}允许执行鸿蒙之眼T${aItemStack.getItemDamage() + 1}配方")
      plus(mNoMobsToolTip)
      plus(mNoTileEntityToolTip)
    }
  }
}
