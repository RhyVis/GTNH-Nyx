package rhynia.nyx.init

import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.material.generation.NyxMaterialLoader
import rhynia.nyx.init.registry.BlockRegister
import rhynia.nyx.init.registry.ItemRegister

object MaterialLoader : Loader {
  override fun load() {
    ItemRegister.register()
    BlockRegister.register()
    NyxMaterials.load()
    NyxMaterialLoader.load()
  }
}
