package rhynia.nyx.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import rhynia.nyx.api.util.RegistryUtil
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.block.AbstractMetaBlock
import rhynia.nyx.common.block.NyxMetaBlockItem
import rhynia.nyx.init.registry.BlockRecord.MetaBlock01

@Suppress("UNUSED")
object BlockRegister {
    fun register() {
        registerBlocks()
        registerBlockContainers()
    }

    private fun registerBlocks() =
        arrayOf(
            MetaBlock01,
        ).forEach {
            GameRegistry.registerBlock(it, NyxMetaBlockItem::class.java, it.unlocalizedName)
        }

    private fun registerBlockContainers() {
        NyxItemList.TestMetaBlock01.register(MetaBlock01, 0, arrayOf("我相信它没什么用", "仅作测试之用"))
    }

    private fun IItemContainer.register(
        block: AbstractMetaBlock,
        meta: Int,
    ) {
        this.set(RegistryUtil.registerMetaBlock(block, meta, null))
    }

    private fun IItemContainer.register(
        block: AbstractMetaBlock,
        meta: Int,
        tooltip: Array<String>,
    ) {
        this.set(RegistryUtil.registerMetaBlock(block, meta, tooltip))
    }

    private fun IItemContainer.register(
        block: AbstractMetaBlock,
        meta: Int,
        tooltip: String,
    ) {
        this.set(RegistryUtil.registerMetaBlock(block, meta, tooltip))
    }
}
