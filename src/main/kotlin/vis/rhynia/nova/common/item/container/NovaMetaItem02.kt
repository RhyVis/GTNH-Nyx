package vis.rhynia.nova.common.item.container

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import vis.rhynia.nova.api.util.MetaItemStack
import vis.rhynia.nova.client.item.ItemDataClient
import vis.rhynia.nova.common.item.ItemRecord
import vis.rhynia.nova.common.item.registry.BaseMetaItem

class NovaMetaItem02(name: String, metaName: String, creativeTabs: CreativeTabs) :
    BaseMetaItem(name, metaName, creativeTabs) {
  companion object {
    val MetaSet = mutableSetOf<Int>()
    val MetaTooltipMap = mutableMapOf<Int, Array<String>>()

    fun initItem(name: String, meta: Int): ItemStack =
        MetaItemStack.initMetaItemStack(meta, ItemRecord.MetaItem02, MetaSet)

    fun initItem(name: String, meta: Int, tooltip: Array<String>?): ItemStack {
      tooltip
          .takeIf { !it.isNullOrEmpty() }
          ?.let { MetaItemStack.metaItemStackTooltipsAdd(MetaTooltipMap, meta, tooltip!!) }
      return initItem(name, meta)
    }
  }

  private val rawName: String = metaName

  override fun getUnlocalizedName(stack: ItemStack): String = "nova.${rawName}.${stack.itemDamage}"

  override fun getUnlocalizedName(): String = rawName

  @SideOnly(Side.CLIENT)
  override fun registerIcons(register: IIconRegister) {
    super.registerIcons(register)
    itemIcon = register.registerIcon("nova:MetaItem02/0")
    MetaSet.forEach {
      ItemDataClient.ICON_MAP_02[it] = register.registerIcon("nova:MetaItem02/$it")
    }
  }

  @SideOnly(Side.CLIENT)
  override fun getIconFromDamage(meta: Int): IIcon {
    return ItemDataClient.let {
      if (it.ICON_MAP_02.contains(meta)) {
        it.ICON_MAP_02[meta]!!
      } else {
        it.ICON_MAP_02[0]!!
      }
    }
  }

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      stack: ItemStack,
      player: EntityPlayer,
      list: MutableList<String>,
      b: Boolean
  ) {
    stack.itemDamage.let { if (MetaTooltipMap.contains(it)) list.addAll(MetaTooltipMap[it]!!) }
  }

  @SideOnly(Side.CLIENT)
  override fun getSubItems(item: Item, creativeTabs: CreativeTabs, list: MutableList<ItemStack>) {
    MetaSet.forEach { list.add(ItemStack(ItemRecord.MetaItem02, 1, it)) }
  }
}
