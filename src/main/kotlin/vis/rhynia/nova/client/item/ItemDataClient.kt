package vis.rhynia.nova.client.item

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.IIcon

@SideOnly(Side.CLIENT)
object ItemDataClient {
  @SideOnly(Side.CLIENT) val ICON_MAP_01 = mutableMapOf<Int, IIcon>()
  @SideOnly(Side.CLIENT) val ICON_MAP_02 = mutableMapOf<Int, IIcon>()
}
