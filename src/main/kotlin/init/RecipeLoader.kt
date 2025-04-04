package rhynia.nyx.init

import rhynia.nyx.DevEnv
import rhynia.nyx.ModLogger
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.api.recipe.RecipePool
import rhynia.nyx.common.material.generation.NyxMaterialLoader
import rhynia.nyx.common.material.generation.NyxMaterialRecipeLoader
import rhynia.nyx.common.recipe.GTAddonRecipes
import rhynia.nyx.common.recipe.GTEasyWirelessRecipes
import rhynia.nyx.common.recipe.NyxMainRecipes
import rhynia.nyx.common.recipe.TestRecipe

object RecipeLoader : Loader {
    override fun load() {
        if (DevEnv) loadTestRecipes()
        loadCommonRecipes()
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

    private fun loadCommonRecipes() {
        ModLogger.info("Loading common recipes...")
        arrayOf<RecipePool>(
            GTAddonRecipes(),
            GTEasyWirelessRecipes(),
            NyxMainRecipes(),
        ).forEach { pool ->
            try {
                pool.loadRecipes()
            } catch (e: Exception) {
                ModLogger.error("Error occurred on loading common recipes.", e)
                throw e
            }
        }
    }

    private fun loadMaterialRecipes() {
        ModLogger.info("Loading Material recipes...")
        NyxMaterialLoader.MaterialSet.forEach { materialRecipePool ->
            try {
                if (materialRecipePool.skipRecipeGeneration) return@forEach
                NyxMaterialRecipeLoader(materialRecipePool).loadRecipes()
            } catch (e: Exception) {
                ModLogger.error(
                    "Error occurred on loading material recipe loader at: ${
                        materialRecipePool.internalName
                    }",
                    e,
                )
                throw e
            }
        }
    }
}
