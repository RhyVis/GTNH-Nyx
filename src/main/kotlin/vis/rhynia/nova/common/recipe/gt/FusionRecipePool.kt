package vis.rhynia.nova.common.recipe.gt

import goodgenerator.items.GGMaterial
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_ZPM
import vis.rhynia.nova.common.material.NovaMaterials
import vis.rhynia.nova.common.recipe.RecipePool

class FusionRecipePool : RecipePool() {
  override fun loadRecipes() {
    val fs: IRecipeMap = RecipeMaps.fusionRecipes

    // ACR
    builder()
        .fluidInputs(
            NovaMaterials.AstralCatalystBaseExcited.getLiquid(500),
            GGMaterial.orundum.getMolten(288))
        .fluidOutputs(NovaMaterials.AstralCatalystReforged.getLiquid(125))
        .durSec(2)
        .eut(RECIPE_ZPM)
        .metadata(FUSION_THRESHOLD, 400_000_000)
        .addTo(fs)
  }
}
