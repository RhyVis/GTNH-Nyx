package rhynia.nyx.common.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.util.GTLanguageManager
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import rhynia.nyx.api.interfaces.block.GregTechTileInfo
import rhynia.nyx.client.NyxTab

@Suppress("SpellCheckingInspection")
class NyxMetaBlockItem(
    block: Block,
) : ItemBlock(block) {
    init {
        hasSubtypes = true
        maxDamage = 0
        creativeTab = NyxTab.TabBlock
    }

    private val metaBlock: AbstractMetaBlock
        get() = field_150939_a as AbstractMetaBlock

    override fun getUnlocalizedName(stack: ItemStack?): String = "$unlocalizedName.${stack?.itemDamage ?: 0}"

    override fun getMetadata(meta: Int): Int = metaBlock.isValidVariant(meta).let { if (it) meta else 0 }

    @SideOnly(Side.CLIENT)
    override fun addInformation(
        stack: ItemStack,
        player: EntityPlayer?,
        list: MutableList<String?>,
        isAdvancedMode: Boolean,
    ) {
        metaBlock.getTooltips(stack.itemDamage)?.let { list.addAll(it) }
        if (metaBlock is GregTechTileInfo) {
            if ((metaBlock as GregTechTileInfo).infoNoMobSpawn) list.add(mNoMobsToolTip)
            if ((metaBlock as GregTechTileInfo).infoNotTileEntity) list.add(mNoTileEntityToolTip)
        }
    }

    companion object {
        private val mNoMobsToolTip: String by lazy { GTLanguageManager.getTranslation("gt.nomobspawnsonthisblock") }
        private val mNoTileEntityToolTip: String by lazy { GTLanguageManager.getTranslation("gt.notileentityinthisblock") }
    }
}
