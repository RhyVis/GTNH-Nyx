package rhynia.nyx.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import rhynia.nyx.api.util.RegistryUtil
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.item.AbstractMetaItem

object ItemRegister {
    fun register() {
        registerItems()
        registerItemContainers()
    }

    private fun registerItems() {
        arrayOf(ItemRecord.MetaItem01, ItemRecord.MetaItem02, ItemRecord.DebugItem, ItemRecord.ItemUltimate).forEach {
            GameRegistry.registerItem(it, it.unlocalizedName)
        }
    }

    private fun registerItemContainers() {
        ItemRecord.MetaItem01.let {
            NyxItemList.TestItem01
                .register(
                    item = it,
                    meta = 0,
                    arrayOf(
                        "我相信它没什么用",
                        "如果在配方中发现了它，代表又一个Null被按下不表",
                    ),
                )
        }

        ItemRecord.MetaItem02.let {
            NyxItemList.Calibration
                .register(
                    item = it,
                    meta = 0,
                    "记录着GT:NH世界运行的原理",
                )
        }

        // Special Item
        NyxItemList.ItemUltimate.register(ItemRecord.ItemUltimate)
    }

    private fun IItemContainer.register(
        item: AbstractMetaItem,
        meta: Int,
        tooltip: Array<String>,
    ) {
        this.set(RegistryUtil.registerMetaItem(item, meta, tooltip))
    }

    private fun IItemContainer.register(
        item: AbstractMetaItem,
        meta: Int,
        tooltip: String,
    ) {
        this.set(RegistryUtil.registerMetaItem(item, meta, tooltip))
    }

    private fun IItemContainer.register(item: Item) {
        this.set(ItemStack(item, 1))
    }
}
