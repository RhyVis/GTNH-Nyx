package rhynia.nyx.init.registry

import cpw.mods.fml.common.registry.GameRegistry
import gregtech.api.interfaces.IItemContainer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import rhynia.nyx.MOD_NAME
import rhynia.nyx.api.util.RegistryUtil
import rhynia.nyx.api.util.debugItem
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.item.AbstractMetaItem
import rhynia.nyx.init.registry.ItemRecord.DebugItem
import rhynia.nyx.init.registry.ItemRecord.ItemUltimate
import rhynia.nyx.init.registry.ItemRecord.MetaItem01
import rhynia.nyx.init.registry.ItemRecord.MetaItem02

object ItemRegister {
    fun register() {
        registerItems()
        registerItemContainers()
    }

    private fun registerItems() {
        arrayOf(
            MetaItem01,
            MetaItem02,
            DebugItem,
            ItemUltimate,
        ).forEach {
            GameRegistry.registerItem(it, it.unlocalizedName)
        }
        debugItem("$MOD_NAME Initialised!")
    }

    private fun registerItemContainers() {
        MetaItem01.let {
            NyxItemList.TestItem01
                .register(
                    item = it,
                    meta = 0,
                    "我相信它没什么用",
                    "如果在配方中发现了它，代表又一个Null被按下不表",
                )
        }

        MetaItem02.let {
            NyxItemList.Calibration
                .register(
                    item = it,
                    meta = 0,
                    "记录着GT:NH世界运行的原理",
                )
        }

        // Special Item
        NyxItemList.ItemUltimate.register(ItemUltimate)
    }

    private fun IItemContainer.register(
        item: AbstractMetaItem,
        meta: Int,
        vararg tooltip: String,
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
