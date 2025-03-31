package rhynia.nyx.init

import rhynia.nyx.Log
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.material.generation.NyxMaterialLoader
import rhynia.nyx.common.material.generation.NyxMaterialRecipeLoader

object RecipeLoader : Loader {
    override fun load() {
        // Material System
        Log.info("Loading Material recipes...")
        NyxMaterialLoader.MaterialSet.forEach {
            try {
                if (it.skipRecipeGeneration) return@forEach
                NyxMaterialRecipeLoader(it).loadRecipes()
            } catch (e: Exception) {
                Log.error("Error occurred on loading material recipe loader at: ${it.internalName}", e)
                throw e
            }
        }
    }
}
