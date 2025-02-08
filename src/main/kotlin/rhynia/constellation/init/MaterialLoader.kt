package rhynia.constellation.init

import rhynia.constellation.api.interfaces.Loader
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.material.generation.CelMaterialLoader
import rhynia.constellation.init.registry.BlockRegister
import rhynia.constellation.init.registry.ItemRegister

object MaterialLoader : Loader {
  override fun load() {
    ItemRegister.register()
    BlockRegister.register()
    CelMaterials.load()
    CelMaterialLoader.load()
  }
}
