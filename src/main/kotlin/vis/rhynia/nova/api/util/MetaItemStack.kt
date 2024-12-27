package vis.rhynia.nova.api.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object MetaItemStack {
  fun initMetaItemStack(meta: Int, baseItem: Item, containerSet: MutableSet<Int>): ItemStack {
    containerSet.add(meta)
    return ItemStack(baseItem, 1, meta)
  }
  fun initMetaItemStack(meta: Int, baseBlock: Block, containerSet: MutableSet<Int>): ItemStack {
    containerSet.add(meta)
    return ItemStack(baseBlock, 1, meta)
  }

  fun metaItemStackTooltipsAdd(
      tooltipsMap: MutableMap<Int, Array<String>>,
      meta: Int,
      tooltips: Array<String>
  ) {
    tooltipsMap[meta] = tooltips
  }
}
