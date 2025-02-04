package vis.rhynia.nova.common.block.base

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vis.rhynia.nova.Constant
import vis.rhynia.nova.api.interfaces.item.MetaTooltip
import vis.rhynia.nova.api.interfaces.item.MetaVariant
import vis.rhynia.nova.client.NovaTab

abstract class AbstractMetaBlock : Block, MetaVariant, MetaTooltip {
  constructor(material: Material, rawName: String) : super(material) {
    setBlockName(rawName)
    setCreativeTab(NovaTab.TabBlock01)
  }

  constructor(rawName: String) : this(Material.iron, rawName)

  protected var iconMap: Map<Int, IIcon> = mutableMapOf()
  protected val tooltipMap: MutableMap<Int, Array<String>?> = mutableMapOf()
  protected val metaSet: MutableSet<Int> = mutableSetOf(16)

  companion object {
    fun getAllVariants(item: Block, metaSet: Set<Int>): Array<ItemStack> =
        metaSet.map { ItemStack(item, 1, it) }.toTypedArray()
  }

  override fun getIcon(side: Int, meta: Int): IIcon? = iconMap[meta]

  override fun damageDropped(meta: Int): Int = meta

  override fun getDamageValue(worldIn: World?, x: Int, y: Int, z: Int): Int =
      worldIn?.getBlockMetadata(x, y, z) ?: 0

  // region MetaVariant Implementation

  override fun getVariant(meta: Int): ItemStack =
      if (metaSet.contains(meta)) ItemStack(this, 1, meta)
      else throw IllegalArgumentException("Invalid meta value: $meta")

  override fun getVariants(): Array<ItemStack> = getAllVariants(this, metaSet)

  override fun getVariantIds(): Set<Int> = metaSet.toSet()

  override fun registerVariant(meta: Int): ItemStack {
    if (metaSet.contains(meta))
        throw IllegalArgumentException(
            "Meta $meta already taken by $unlocalizedName in ${javaClass.simpleName}")
    else
        return meta.let {
          metaSet.add(it)
          ItemStack(this, 1, it)
        }
  }

  // endregion

  // region MetaTooltip Implementation

  override fun getTooltips(meta: Int): Array<String>? = tooltipMap[meta]

  override fun setTooltips(meta: Int, tooltips: Array<String>?) {
    if (tooltips == null) tooltipMap.remove(meta) else tooltipMap[meta] = tooltips
  }

  // endregion

  @SideOnly(Side.CLIENT)
  override fun registerBlockIcons(register: IIconRegister) {
    iconMap = this.registerVariantIcon(register) { "${Constant.MOD_ID}:$unlocalizedName/$it" }
    blockIcon = iconMap[0]
  }

  @SideOnly(Side.CLIENT)
  override fun getSubBlocks(
      aItem: Item?,
      aCreativeTabs: CreativeTabs?,
      list: MutableList<ItemStack?>
  ) {
    list.addAll(getVariants())
  }
}
