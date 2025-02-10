package rhynia.constellation.init

import rhynia.constellation.Log
import rhynia.constellation.api.interfaces.Loader
import rhynia.constellation.common.material.generation.CelMaterialLoader
import rhynia.constellation.common.material.generation.CelMaterialRecipeLoader
import rhynia.constellation.common.recipe.RecipePool
import rhynia.constellation.common.recipe.cel.AstralForgeRecipePool
import rhynia.constellation.common.recipe.cel.IntegratedAssemblyRecipePool
import rhynia.constellation.common.recipe.cel.MicroAssemblyRecipePool
import rhynia.constellation.common.recipe.cel.QuarkRefactoringRecipePool
import rhynia.constellation.common.recipe.cel.SuperconductingFormingRecipePool
import rhynia.constellation.common.recipe.cel.ThermonuclearControlRecipePool
import rhynia.constellation.common.recipe.cel.TranscendentReactorRecipePool
import rhynia.constellation.common.recipe.gt.AssemblerRecipePool
import rhynia.constellation.common.recipe.gt.CentrifugeRecipePool
import rhynia.constellation.common.recipe.gt.ChemicalReactorRecipePool
import rhynia.constellation.common.recipe.gt.CommonRecipePool
import rhynia.constellation.common.recipe.gt.CompressorRecipePool
import rhynia.constellation.common.recipe.gt.ElectrolyzeRecipePool
import rhynia.constellation.common.recipe.gt.FusionRecipePool
import rhynia.constellation.common.recipe.gt.HammerRecipePool
import rhynia.constellation.common.recipe.gt.LaserEngraverRecipePool
import rhynia.constellation.common.recipe.gt.MixerRecipePool
import rhynia.constellation.common.recipe.gt.PlasmaForgeRecipePool
import rhynia.constellation.common.recipe.gt.QuantumForceTransformerRecipePool

object RecipeLoader : Loader {
  override fun load() {
    // Constellation
    Log.info("Loading Constellation additional recipes...")
    arrayOf<RecipePool>(
            AstralForgeRecipePool(),
            IntegratedAssemblyRecipePool(),
            MicroAssemblyRecipePool(),
            QuarkRefactoringRecipePool(),
            SuperconductingFormingRecipePool(),
            ThermonuclearControlRecipePool(),
            TranscendentReactorRecipePool(),
        )
        .forEach {
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
        )
        .forEach {
          try {
            it.loadRecipes()
          } catch (e: Exception) {
            Log.error("Error occurred on loading recipe pool at: ${it.javaClass.simpleName}", e)
            throw e
          }
        }
    // Material System
    Log.info("Loading Material related recipes...")
    CelMaterialLoader.MaterialSet.forEach {
      try {
        if (it.skipRecipeGeneration) return@forEach
        CelMaterialRecipeLoader(it).loadRecipes()
      } catch (e: Exception) {
        Log.error("Error occurred on loading material recipe loader at: ${it.internalName}", e)
        throw e
      }
    }
  }
}
