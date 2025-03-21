package rhynia.nyx.init

import rhynia.nyx.Log
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.material.generation.NyxMaterialLoader
import rhynia.nyx.common.material.generation.NyxMaterialRecipeLoader
import rhynia.nyx.common.recipe.RecipePool
import rhynia.nyx.common.recipe.gt.AssemblerRecipePool
import rhynia.nyx.common.recipe.gt.CentrifugeRecipePool
import rhynia.nyx.common.recipe.gt.ChemicalReactorRecipePool
import rhynia.nyx.common.recipe.gt.CommonRecipePool
import rhynia.nyx.common.recipe.gt.CompressorRecipePool
import rhynia.nyx.common.recipe.gt.ElectrolyzeRecipePool
import rhynia.nyx.common.recipe.gt.FusionRecipePool
import rhynia.nyx.common.recipe.gt.HammerRecipePool
import rhynia.nyx.common.recipe.gt.LaserEngraverRecipePool
import rhynia.nyx.common.recipe.gt.MixerRecipePool
import rhynia.nyx.common.recipe.gt.PlasmaForgeRecipePool
import rhynia.nyx.common.recipe.gt.QuantumForceTransformerRecipePool
import rhynia.nyx.common.recipe.nyx.AstralForgeRecipePool
import rhynia.nyx.common.recipe.nyx.IntegratedAssemblyRecipePool
import rhynia.nyx.common.recipe.nyx.MicroAssemblyRecipePool
import rhynia.nyx.common.recipe.nyx.QuarkRefactoringRecipePool
import rhynia.nyx.common.recipe.nyx.SuperconductingFormingRecipePool
import rhynia.nyx.common.recipe.nyx.ThermonuclearControlRecipePool
import rhynia.nyx.common.recipe.nyx.TranscendentReactorRecipePool

object RecipeLoader : Loader {
    override fun load() {
        // Nyx
        Log.info("Loading Nyx additional recipes...")
        arrayOf<RecipePool>(
            AstralForgeRecipePool(),
            IntegratedAssemblyRecipePool(),
            MicroAssemblyRecipePool(),
            QuarkRefactoringRecipePool(),
            SuperconductingFormingRecipePool(),
            ThermonuclearControlRecipePool(),
            TranscendentReactorRecipePool(),
        ).forEach {
            try {
                it.loadRecipes()
            } catch (e: Exception) {
                Log.error("Error occurred on loading recipe pool at: ${it.javaClass.simpleName}", e)
                throw e
            }
        }
        // GT
        Log.info("Loading GregTech related recipes...")
        arrayOf<RecipePool>(
            AssemblerRecipePool(),
            CentrifugeRecipePool(),
            ChemicalReactorRecipePool(),
            CommonRecipePool(),
            CompressorRecipePool(),
            ElectrolyzeRecipePool(),
            FusionRecipePool(),
            HammerRecipePool(),
            LaserEngraverRecipePool(),
            MixerRecipePool(),
            PlasmaForgeRecipePool(),
            QuantumForceTransformerRecipePool(),
        ).forEach {
            try {
                it.loadRecipes()
            } catch (e: Exception) {
                Log.error("Error occurred on loading recipe pool at: ${it.javaClass.simpleName}", e)
                throw e
            }
        }
        // Material System
        Log.info("Loading Material related recipes...")
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
