package vis.rhynia.nova.common.block.casing

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.GregTechAPI
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vis.rhynia.nova.api.util.MetaItemStack
import vis.rhynia.nova.client.NovaTab
import vis.rhynia.nova.client.block.BlockDataClient.ICON_EOH_CORE_MAP
import vis.rhynia.nova.common.block.BlockRecord.EyeOfHarmonyCoreCasing
import vis.rhynia.nova.common.block.base.BlockBase01

class EyeOfHarmonyCore() : BlockBase01() {
  init {
    setHardness(9.0f)
    setResistance(5.0f)
    setHarvestLevel("wrench", 1)
    setCreativeTab(NovaTab.TabBlock01)
  }

  constructor(rawName: String) : this() {
    this.rawName = rawName
  }

  companion object {
    val EyeOfHarmonyCoreCasingSet = mutableSetOf<Int>()

    fun eyeOfHarmonyCoreCasingMeta(meta: Int): ItemStack {
      return MetaItemStack.initMetaItemStack(
          meta, EyeOfHarmonyCoreCasing, EyeOfHarmonyCoreCasingSet)
    }
  }

  private var blockIcon: IIcon? = null
  private var rawName: String? = null

  override fun getUnlocalizedName(): String {
    return "nova.$rawName"
  }

  @SideOnly(Side.CLIENT)
  override fun getIcon(side: Int, meta: Int): IIcon {
    return if (ICON_EOH_CORE_MAP.containsKey(meta)) ICON_EOH_CORE_MAP[meta]!!
    else ICON_EOH_CORE_MAP[0]!!
  }

  @SideOnly(Side.CLIENT)
  override fun registerBlockIcons(reg: IIconRegister) {
    this.blockIcon = reg.registerIcon("nova:EyeOfHarmonyCore/0")
    for (meta in EyeOfHarmonyCoreCasingSet) {
      ICON_EOH_CORE_MAP.put(meta, reg.registerIcon("nova:EyeOfHarmonyCore/$meta"))
    }
  }

  override fun onBlockAdded(aWorld: World, aX: Int, aY: Int, aZ: Int) {
    if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
      GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ)
    }
  }

  override fun breakBlock(
      aWorld: World,
      aX: Int,
      aY: Int,
      aZ: Int,
      aBlock: Block?,
      aMetaData: Int
  ) {
    if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
      GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ)
    }
  }

  @SideOnly(Side.CLIENT)
  override fun getSubBlocks(aItem: Item?, aCreativeTabs: CreativeTabs?, list: List<ItemStack>) {
    EyeOfHarmonyCoreCasingSet.forEach { list.plus(ItemStack(aItem, 1, it)) }
  }
}
