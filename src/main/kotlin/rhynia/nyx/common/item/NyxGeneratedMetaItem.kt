package rhynia.nyx.common.item

import bartworks.client.textures.PrefixTextureLinker
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IIconContainer
import gregtech.api.items.MetaGeneratedItem
import gregtech.api.util.GTOreDictUnificator
import kotlin.collections.get
import net.minecraft.client.gui.GuiScreen.isShiftKeyDown
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting.BOLD
import net.minecraft.util.EnumChatFormatting.GRAY
import net.minecraft.util.EnumChatFormatting.RESET
import net.minecraft.util.EnumChatFormatting.WHITE
import net.minecraft.util.IIcon
import rhynia.nyx.api.util.firstCharUpperCase
import rhynia.nyx.client.NyxTab
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.material.generation.NyxMaterialLoader.MaterialMap
import rhynia.nyx.common.material.generation.NyxMaterialLoader.MaterialSet

class NyxGeneratedMetaItem(val orePrefix: OrePrefixes) :
    MetaGeneratedItem("GenItem${orePrefix.name.firstCharUpperCase()}", 32766, 0) {
  init {
    creativeTab = NyxTab.TabItem
    MaterialSet.forEach {
      if (!it.isTypeValid(orePrefix)) return@forEach
      GTOreDictUnificator.registerOre(
          orePrefix.name.firstCharUpperCase() + it.internalNameTitleCase,
          ItemStack(this, 1, it.id.toInt()))
    }
  }

  companion object {
    private val prefixLocalizedMap =
        mapOf<OrePrefixes, String>(
            OrePrefixes.dust to "%material粉",
            OrePrefixes.dustSmall to "小堆%material粉",
            OrePrefixes.dustTiny to "小撮%material粉",
            OrePrefixes.nugget to "%material粒",
            OrePrefixes.ingot to "%material锭",
            OrePrefixes.ingotHot to "热%material锭",
            OrePrefixes.ingotDouble to "双重%material锭",
            OrePrefixes.ingotTriple to "三重%material锭",
            OrePrefixes.ingotQuadruple to "四重%material锭",
            OrePrefixes.ingotQuintuple to "五重%material锭",
            OrePrefixes.cell to "%material单元",
            OrePrefixes.cellPlasma to "%material离子单元",
            OrePrefixes.cellMolten to "熔融%material单元",
            OrePrefixes.capsule to "%material胶囊",
            OrePrefixes.capsuleMolten to "熔融%material胶囊",
            OrePrefixes.plate to "%material板",
            OrePrefixes.plateDense to "致密%material板",
            OrePrefixes.plateDouble to "双重%material板",
            OrePrefixes.plateTriple to "三重%material板",
            OrePrefixes.plateQuadruple to "四重%material板",
            OrePrefixes.plateQuintuple to "五重%material板",
            OrePrefixes.stick to "%material棒",
            OrePrefixes.stickLong to "长%material棒",
            OrePrefixes.spring to "%material弹簧",
            OrePrefixes.springSmall to "小型%material弹簧",
            OrePrefixes.gearGt to "%material齿轮",
            OrePrefixes.gearGtSmall to "小型%material齿轮",
            OrePrefixes.ring to "%material环",
            OrePrefixes.rotor to "%material转子",
            OrePrefixes.screw to "%material螺丝",
            OrePrefixes.foil to "%material箔",
            OrePrefixes.frame to "%material框架",
            OrePrefixes.gem to "%material",
            OrePrefixes.gemChipped to "破碎的%material",
            OrePrefixes.gemExquisite to "精致的%material",
            OrePrefixes.gemFlawed to "有瑕的%material",
            OrePrefixes.gemFlawless to "无瑕的%material",
            OrePrefixes.lens to "%material透镜",
            OrePrefixes.crystal to "%material晶体",
            OrePrefixes.wireFine to "%material线",
            OrePrefixes.wireFine to "细%material线",
        )

    private fun getLocalizedTypeName(prefix: OrePrefixes): String =
        prefixLocalizedMap[prefix]
            ?: "${prefix.mLocalizedMaterialPre}%material${prefix.mLocalizedMaterialPost}"
  }

  private val typedName: String
    get() = getLocalizedTypeName(orePrefix)

  override fun addAdditionalToolTips(
      aList: MutableList<String>,
      aStack: ItemStack?,
      aPlayer: EntityPlayer?
  ) {
    val material = MaterialMap[aStack?.itemDamage?.toShort()] ?: return
    material.elementTooltip.forEach { aList.add(it) }
    if (material.extraTooltip.isNotEmpty()) {
      if (isShiftKeyDown()) material.extraTooltip.forEach { aList.add(it) }
      else aList.add("按下 ${BOLD}${WHITE}Shift${RESET}${GRAY} 键以查看更多信息")
    }
  }

  override fun getItemStackDisplayName(stack: ItemStack): String? {
    val material = MaterialMap[stack.itemDamage.toShort()] ?: NyxMaterials.Null
    return typedName.replace("%material", material.displayName)
  }

  override fun getIconContainer(aMetaData: Int): IIconContainer? {
    return if (MaterialMap[aMetaData.toShort()] == null) null
    else if (orePrefix.mTextureIndex.toInt() == -1) proxyIconContainerBartWorks(aMetaData)
    else
        MaterialMap[aMetaData.toShort()]
            ?.textureSet
            ?.mTextures
            ?.get(orePrefix.mTextureIndex.toInt())
  }

  @SideOnly(Side.CLIENT)
  private fun proxyIconContainerBartWorks(aMetaData: Int): IIconContainer? {
    return PrefixTextureLinker.texMap[orePrefix]?.get(MaterialMap[aMetaData.toShort()]?.textureSet)
  }

  override fun getSubItems(
      aItem: Item?,
      aCreativeTab: CreativeTabs?,
      aList: MutableList<ItemStack>
  ) {
    MaterialSet.forEach { material ->
      if (!material.isTypeValid(orePrefix)) return@forEach
      aList.add(ItemStack(this, 1, material.id.toInt()))
    }
  }

  override fun getRGBa(aStack: ItemStack?): ShortArray {
    return MaterialMap[aStack?.itemDamage?.toShort()]?.color ?: shortArrayOf(0, 0, 0, 255)
  }

  override fun getIconFromDamage(aMetaData: Int): IIcon? {
    return if (aMetaData < 0 || aMetaData >= MaterialSet.size) null
    else if (MaterialMap[aMetaData.toShort()] == null) null else getIconContainer(aMetaData)?.icon
  }

  override fun getItemStackLimit(aStack: ItemStack?): Int {
    return orePrefix.mDefaultStackSize.toInt()
  }

  override fun getCapacity(aStack: ItemStack?): Int {
    return when (orePrefix) {
      OrePrefixes.cell,
      OrePrefixes.capsule,
      OrePrefixes.cellPlasma -> 1000
      OrePrefixes.cellMolten,
      OrePrefixes.capsuleMolten -> 144
      else -> 0
    }
  }

  override fun getContainerItem(aStack: ItemStack?): ItemStack? {
    return when (orePrefix) {
      OrePrefixes.cell,
      OrePrefixes.cellPlasma,
      OrePrefixes.cellMolten -> Materials.Empty.getCells(1)
      else -> null
    }
  }
}
