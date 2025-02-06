package vis.rhynia.nova.common.loader

import vis.rhynia.nova.api.interfaces.Loader
import vis.rhynia.nova.common.block.BlockRegister
import vis.rhynia.nova.common.item.ItemRegister
import vis.rhynia.nova.common.material.NovaMaterials
import vis.rhynia.nova.common.material.generation.SimpleMaterialLoader

object MaterialLoader : Loader {
  override fun load() {
    ItemRegister.register()
    BlockRegister.register()
    NovaMaterials.load()
    SimpleMaterialLoader.load()
  }
}
