package vis.rhynia.nova.common.item.registry

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import vis.rhynia.nova.Log

open class BaseMetaItem(name: String, metaName: String, creativeTabs: CreativeTabs) : Item() {
  private val tooltips = mutableListOf<String>()
  private var rawName: String

  init {
    setHasSubtypes(true)
    setMaxDamage(0)
    setCreativeTab(creativeTabs)
    rawName = metaName
    Log.debug("Item: $name, registry meta name: $metaName")
  }

  override fun getMetadata(meta: Int): Int = meta

  override fun setUnlocalizedName(name: String?): Item {
    name?.let { rawName = it }
    return this
  }

  override fun getUnlocalizedName(stack: ItemStack): String = "${rawName}.${stack.itemDamage}"

  override fun getUnlocalizedName(): String = rawName

  @SideOnly(Side.CLIENT)
  override fun getSubItems(item: Item, creativeTabs: CreativeTabs, list: MutableList<ItemStack>) {
    list.add(ItemStack(item, 1, 0))
  }

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      stack: ItemStack,
      player: EntityPlayer,
      list: MutableList<String>,
      b: Boolean
  ) {
    tooltips.takeIf { it.isNotEmpty() }?.let { list.addAll(it) }
  }
}
