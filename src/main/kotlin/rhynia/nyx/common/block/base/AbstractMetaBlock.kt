package rhynia.nyx.common.block.base

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
import rhynia.nyx.NYX_MOD_ID
import rhynia.nyx.api.interfaces.item.MetaTooltip
import rhynia.nyx.api.interfaces.item.MetaVariant
import rhynia.nyx.client.NyxTab.TabBlock

/**
 * Abstract class for blocks using meta as variant.
 *
 * @param rawName the raw name of the block
 */
abstract class AbstractMetaBlock(
    protected val rawName: String,
    material: Material = Material.iron
) : Block(material), MetaVariant, MetaTooltip {

  init {
    setBlockName(rawName)
    setCreativeTab(TabBlock)
  }

  protected var iconMap: Map<Int, IIcon> = mutableMapOf()
  protected val tooltipMap: MutableMap<Int, Array<String>?> = mutableMapOf()
  protected val metaSet: MutableSet<Int> = mutableSetOf()

  override fun getUnlocalizedName(): String = super.getUnlocalizedName()

  override fun damageDropped(meta: Int): Int = meta

  override fun getDamageValue(worldIn: World?, x: Int, y: Int, z: Int): Int =
      worldIn?.getBlockMetadata(x, y, z) ?: 0

  // region MetaVariant Implementation

  override fun getVariant(meta: Int): ItemStack =
      if (metaSet.contains(meta)) ItemStack(this, 1, meta)
      else throw IllegalArgumentException("Invalid meta value: $meta")

  override fun getVariants(): Array<ItemStack> =
      metaSet.map { ItemStack(this, 1, it) }.toTypedArray()

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

  @SideOnly(Side.CLIENT) override fun getIcon(side: Int, meta: Int): IIcon? = iconMap[meta]

  @SideOnly(Side.CLIENT)
  override fun registerBlockIcons(register: IIconRegister) {
    iconMap = this.registerVariantIcon(register) { "${NYX_MOD_ID}:$rawName/$it" }
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
