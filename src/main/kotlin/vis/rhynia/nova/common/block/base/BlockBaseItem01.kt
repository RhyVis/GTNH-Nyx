package vis.rhynia.nova.common.block.base

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.util.GTLanguageManager
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import vis.rhynia.nova.api.util.MetaItemStack
import vis.rhynia.nova.client.NovaTab
import vis.rhynia.nova.common.block.BlockRecord.MetaBlock01

class BlockBaseItem01(block: Block) : ItemBlock(block) {
  init {
    setHasSubtypes(true)
    setMaxDamage(0)
    setCreativeTab(NovaTab.TabBlock01)
  }

  companion object {
    val MetaSet = mutableSetOf<Int>()
    val MetaTooltipMap = mutableMapOf<Int, Array<String>>()

    fun initMetaBlock(name: String, meta: Int): ItemStack =
        MetaItemStack.initMetaItemStack(meta, MetaBlock01, MetaSet)

    fun initMetaBlock(name: String, meta: Int, tooltip: Array<String>?): ItemStack {
      tooltip
          .takeIf { !it.isNullOrEmpty() }
          ?.let { MetaItemStack.metaItemStackTooltipsAdd(MetaTooltipMap, meta, tooltip!!) }
      return initMetaBlock(name, meta)
    }
  }

  @Suppress("SpellCheckingInspection")
  private val mNoMobsToolTip =
      GTLanguageManager.addStringLocalization(
          "gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block")

  @Suppress("SpellCheckingInspection")
  private val mNoTileEntityToolTip =
      GTLanguageManager.addStringLocalization(
          "gt.notileentityinthisblock", "This is NOT a TileEntity!")

  @SideOnly(Side.CLIENT)
  override fun addInformation(
      stack: ItemStack?,
      player: EntityPlayer?,
      list: List<String?>?,
      b: Boolean
  ) {
    val meta = stack?.itemDamage ?: 0
    if (MetaTooltipMap.containsKey(meta)) {
      val tt = MetaTooltipMap[meta]!!
      list?.plus(tt)
    }
    list?.plus(mNoMobsToolTip)
    list?.plus(mNoTileEntityToolTip)
  }

  override fun getUnlocalizedName(stack: ItemStack): String =
      "${field_150939_a.unlocalizedName}.${getDamage(stack)}"

  override fun getMetadata(meta: Int): Int = meta
}
