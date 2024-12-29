package vis.rhynia.nova.client.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.IIcon

@SideOnly(Side.CLIENT)
object BlockDataClient {
  @SideOnly(Side.CLIENT) val ICON_BLOCK_MAP_01 = mutableMapOf<Int, IIcon>()
  @SideOnly(Side.CLIENT) val ICON_EOH_CORE_MAP = mutableMapOf<Int, IIcon>()
}
