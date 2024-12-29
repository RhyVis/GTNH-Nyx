package vis.rhynia.nova.common.block.base

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EnumCreatureType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import vis.rhynia.nova.client.NovaTab
import vis.rhynia.nova.client.block.BlockDataClient.ICON_BLOCK_MAP_01

class BlockBase01 : Block {
  constructor(material: Material) : super(material)

  constructor() : this(Material.iron) {
    setCreativeTab(NovaTab.TabBlock01)
  }

  constructor(rawName: String, localizedName: String) : this() {
    this.rawName = rawName
  }

  private var rawName: String = ""

  override fun getUnlocalizedName(): String = "nova.$rawName"

  @SideOnly(Side.CLIENT)
  override fun getIcon(side: Int, meta: Int): IIcon =
      if (ICON_BLOCK_MAP_01.contains(meta)) ICON_BLOCK_MAP_01[meta]!! else ICON_BLOCK_MAP_01[0]!!

  @SideOnly(Side.CLIENT)
  override fun registerBlockIcons(reg: IIconRegister) {
    blockIcon = reg.registerIcon("nova:MetaBlocks/0")
    BlockBaseItem01.MetaSet.forEach {
      ICON_BLOCK_MAP_01[it] = reg.registerIcon("nova:MetaBlocks/$it")
    }
  }

  @SideOnly(Side.CLIENT)
  override fun getSubBlocks(itemIn: Item?, tab: CreativeTabs?, list: List<ItemStack>) {
    BlockBaseItem01.MetaSet.forEach { list.plus(ItemStack(itemIn, 1, it)) }
  }

  override fun damageDropped(meta: Int): Int = meta

  override fun canBeReplacedByLeaves(world: IBlockAccess?, x: Int, y: Int, z: Int): Boolean = false

  override fun canEntityDestroy(
      world: IBlockAccess?,
      x: Int,
      y: Int,
      z: Int,
      entity: Entity?
  ): Boolean = false

  override fun canCreatureSpawn(
      type: EnumCreatureType?,
      world: IBlockAccess?,
      x: Int,
      y: Int,
      z: Int
  ): Boolean = false
}
