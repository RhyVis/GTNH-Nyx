package vis.rhynia.nova.common.loader

import vis.rhynia.nova.Log
import vis.rhynia.nova.api.interfaces.Loader
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.recipe.gt.AssemblerRecipePool
import vis.rhynia.nova.common.recipe.gt.CentrifugeRecipePool
import vis.rhynia.nova.common.recipe.gt.ChemicalReactorRecipePool
import vis.rhynia.nova.common.recipe.gt.CommonRecipePool
import vis.rhynia.nova.common.recipe.gt.CompressorRecipePool
import vis.rhynia.nova.common.recipe.gt.ElectrolyzeRecipePool
import vis.rhynia.nova.common.recipe.gt.FusionRecipePool
import vis.rhynia.nova.common.recipe.gt.HammerRecipePool
import vis.rhynia.nova.common.recipe.gt.LaserEngraverRecipePool
import vis.rhynia.nova.common.recipe.gt.MixerRecipePool
import vis.rhynia.nova.common.recipe.gt.PlasmaForgeRecipePool
import vis.rhynia.nova.common.recipe.gt.QuantumForceTransformerRecipePool
import vis.rhynia.nova.common.recipe.nova.AstralForgeRecipePool
import vis.rhynia.nova.common.recipe.nova.IntegratedAssemblyRecipePool
import vis.rhynia.nova.common.recipe.nova.MicroAssemblyRecipePool
import vis.rhynia.nova.common.recipe.nova.QuarkRefactoringRecipePool
import vis.rhynia.nova.common.recipe.nova.SuperconductingFormingRecipePool
import vis.rhynia.nova.common.recipe.nova.ThermonuclearControlRecipePool
import vis.rhynia.nova.common.recipe.nova.TranscendentReactorRecipePool

object RecipeLoader : Loader {
  override fun load() {
    // Nova
    Log.info("Loading Nova additional recipes...")
    arrayOf<RecipePool>(
            AstralForgeRecipePool(),
            IntegratedAssemblyRecipePool(),
            MicroAssemblyRecipePool(),
            QuarkRefactoringRecipePool(),
            SuperconductingFormingRecipePool(),
            ThermonuclearControlRecipePool(),
            TranscendentReactorRecipePool(),
        )
        .forEach { it.loadRecipes() }
    // GT
    Log.info("Loading GT related recipes...")
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
        .forEach { it.loadRecipes() }
  }
}
