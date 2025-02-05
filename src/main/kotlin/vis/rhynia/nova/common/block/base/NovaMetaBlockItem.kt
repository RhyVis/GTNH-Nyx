package vis.rhynia.nova.common.block.base

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.util.GTLanguageManager
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import vis.rhynia.nova.api.interfaces.block.GregTechTileInfo
import vis.rhynia.nova.client.NovaTab

class NovaMetaBlockItem(block: Block) : ItemBlock(block) {
  init {
    hasSubtypes = true
    maxDamage = 0
    creativeTab = NovaTab.TabBlock
  }

  @Suppress("SpellCheckingInspection")
  private val mNoMobsToolTip: String? =
      GTLanguageManager.addStringLocalization(
          "gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block")

  @Suppress("SpellCheckingInspection")
  private val mNoTileEntityToolTip: String? =
      GTLanguageManager.addStringLocalization(
          "gt.notileentityinthisblock", "This is NOT a TileEntity!")

  private val metaBlock: AbstractMetaBlock
    get() = field_150939_a as AbstractMetaBlock

  override fun getUnlocalizedName(stack: ItemStack?): String =
      "${metaBlock.unlocalizedName}.${stack?.itemDamage ?: 0}"

  override fun getMetadata(meta: Int): Int =
      metaBlock.isValidVariant(meta).let { if (it) meta else 0 }

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      stack: ItemStack,
      player: EntityPlayer?,
      list: MutableList<String?>,
      isAdvancedMode: Boolean
  ) {
    metaBlock.getTooltips(stack.itemDamage)?.let { list.addAll(it) }
    if (metaBlock is GregTechTileInfo) {
      if ((metaBlock as GregTechTileInfo).infoNoMobSpawn) list.add(mNoMobsToolTip)
      if ((metaBlock as GregTechTileInfo).infoNotTileEntity) list.add(mNoTileEntityToolTip)
    }
  }
}
