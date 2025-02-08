package rhynia.constellation.common.item.base

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import org.jetbrains.annotations.ApiStatus.OverrideOnly
import rhynia.constellation.CEL_MOD_ID
import rhynia.constellation.api.interfaces.item.MetaTooltip
import rhynia.constellation.api.interfaces.item.MetaVariant
import rhynia.constellation.client.CelTab

/** Abstract class for items using meta as variant. */
abstract class AbstractMetaItem(protected val rawName: String) : Item(), MetaVariant, MetaTooltip {
  protected var iconMap: Map<Int, IIcon> = mutableMapOf()
  protected val tooltipMap: MutableMap<Int, Array<String>?> = mutableMapOf()
  protected val metaSet: MutableSet<Int> = mutableSetOf()

  init {
    hasSubtypes = true
    maxDamage = 0
    creativeTab = CelTab.TabItem
    unlocalizedName = rawName
    iconString = "${CEL_MOD_ID}:${iconName ?: "$rawName/0"}"
  }

  /**
   * Override this to set the alternate texture name, if null defaults to
   *
   * `nova:'rawName'/0`.
   */
  open val iconName: String?
    @OverrideOnly get() = null

  override fun getMetadata(meta: Int): Int = meta

  override fun getUnlocalizedName(): String = super.getUnlocalizedName()

  override fun getUnlocalizedName(stack: ItemStack?): String =
      "${super.getUnlocalizedName()}.${stack?.itemDamage ?: 0}"
  override fun getIconFromDamage(meta: Int): IIcon = iconMap[meta] ?: itemIcon

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

  @SideOnly(Side.CLIENT)
  override fun registerIcons(register: IIconRegister) {
    iconMap = this.registerVariantIcon(register) { "${CEL_MOD_ID}:$rawName/$it" }
    itemIcon = iconMap[0]
  }

  @SideOnly(Side.CLIENT)
  override fun getSubItems(
      aItem: Item?,
      aCreativeTabs: CreativeTabs?,
      aList: MutableList<ItemStack?>
  ) {
    aList.addAll(getVariants())
  }

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      stack: ItemStack,
      player: EntityPlayer?,
      list: MutableList<String?>,
      isAdvancedMode: Boolean
  ) {
    getTooltips(stack.getItemDamage())?.let { list.addAll(it) }
  }
}
