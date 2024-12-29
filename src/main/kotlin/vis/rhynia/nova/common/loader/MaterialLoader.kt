package vis.rhynia.nova.common.loader

import bartworks.API.WerkstoffAdderRegistry
import vis.rhynia.nova.common.block.BlockRegister
import vis.rhynia.nova.common.item.ItemRegister
import vis.rhynia.nova.common.material.NovaMaterial

object MaterialLoader {
  fun load() {
    ItemRegister.register()
    BlockRegister.register()
    WerkstoffAdderRegistry.addWerkstoffAdder(NovaMaterial)
  }
}
