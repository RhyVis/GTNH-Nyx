package vis.rhynia.nova.common.loader

import net.minecraft.item.ItemStack
import vis.rhynia.nova.common.NovaItemList
import vis.rhynia.nova.common.tile.multi.process.NovaMTEAstralForge

object MachineLoader {
  var AstralForge: ItemStack? = null

  fun load() {
    initializeMachineClass()
    initializeItemList()
  }

  private fun initializeMachineClass() {
    AstralForge = NovaMTEAstralForge(17501, "MTEAstralForge", "星辉锻造台").getStackForm(1)
  }

  private fun initializeItemList() {
    NovaItemList.AstralForge.set(AstralForge)
  }
}
