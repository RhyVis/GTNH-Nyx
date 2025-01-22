package vis.rhynia.nova.api.process.util

import gregtech.api.util.GTUtility.ItemId
import java.util.Objects
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.oredict.OreDictionary

class NovaItemId : ItemId {
  private var item: Item? = null
  private var meta: Int = 0
  private var nbt: NBTTagCompound? = null

  constructor(item: Item) {
    this.item = item
  }

  constructor(item: Item, meta: Int) {
    this.item = item
    this.meta = meta
  }

  constructor(item: Item, meta: Int, nbt: NBTTagCompound) {
    this.item = item
    this.meta = meta
    this.nbt = nbt
  }

  companion object {
    fun create(item: ItemStack) = NovaItemId(item.item, item.itemDamage, item.tagCompound)

    fun createWithoutNBT(item: ItemStack) = NovaItemId(item.item, item.itemDamage)

    fun createAsWildcard(item: ItemStack) = NovaItemId(item.item, OreDictionary.WILDCARD_VALUE)
  }

  override fun getItemStack() = ItemStack(item, 1, meta)

  override fun getItemStack(amount: Int) = ItemStack(item, amount, meta)

  fun getItemStackWithNBT() = ItemStack(item, 1, meta).apply { tagCompound = nbt }

  fun getItemStackWithNBT(amount: Int) = ItemStack(item, amount, meta).apply { tagCompound = nbt }

  fun isWildcard() = meta == OreDictionary.WILDCARD_VALUE

  fun setItem(item: Item) = this.apply { this.item = item }

  fun setMeta(meta: Int) = this.apply { this.meta = meta }

  fun setNBT(nbt: NBTTagCompound) = this.apply { this.nbt = nbt }

  override fun item(): Item? = item

  override fun metaData(): Int = meta

  override fun nbt(): NBTTagCompound? = nbt

  override fun stackSize(): Int? = null

  fun getItem() = item

  fun getMeta() = meta

  fun getNBT() = nbt

  fun equalItemStack(itemStack: ItemStack) =
      equals(if (isWildcard()) createAsWildcard(itemStack) else createWithoutNBT(itemStack))

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || other !is NovaItemId) return false
    return meta == other.meta && item == other.item && nbt == other.nbt
  }

  override fun hashCode(): Int = Objects.hash(item, meta, nbt)
}
