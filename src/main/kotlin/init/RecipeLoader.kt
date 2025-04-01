package rhynia.nyx.init

import rhynia.nyx.DevEnv
import rhynia.nyx.ModLogger
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.material.generation.NyxMaterialLoader
import rhynia.nyx.common.material.generation.NyxMaterialRecipeLoader
import rhynia.nyx.common.recipe.TestRecipe

object RecipeLoader : Loader {
    override fun load() {
        if (DevEnv) loadTestRecipes()
        loadMaterialRecipes()
    }

    private fun loadTestRecipes() {
        ModLogger.info("Loading test recipes...")
        try {
            TestRecipe().loadRecipes()
        } catch (e: Exception) {
            ModLogger.error("Error occurred on loading test recipes.", e)
            throw e
        }
    }

    private fun loadMaterialRecipes() {
        ModLogger.info("Loading Material recipes...")
        NyxMaterialLoader.MaterialSet.forEach {
            try {
                if (it.skipRecipeGeneration) return@forEach
                NyxMaterialRecipeLoader(it).loadRecipes()
            } catch (e: Exception) {
                ModLogger.error("Error occurred on loading material recipe loader at: ${it.internalName}", e)
                throw e
            }
        }
    }
}
