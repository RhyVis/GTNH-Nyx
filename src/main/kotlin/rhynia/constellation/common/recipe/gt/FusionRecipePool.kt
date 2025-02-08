package rhynia.constellation.common.recipe.gt

import goodgenerator.items.GGMaterial
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

class FusionRecipePool : RecipePool() {
  override fun loadRecipes() {
    val fs: IRecipeMap = RecipeMaps.fusionRecipes

    // ACR
    builder()
        .fluidInputs(
            CelMaterials.AstralCatalystBaseExcited.getLiquid(500),
            GGMaterial.orundum.getMolten(288))
        .fluidOutputs(CelMaterials.AstralCatalystReforged.getLiquid(125))
        .durSec(2)
        .eut(CelValues.RecipeValues.RECIPE_ZPM)
        .metadata(FUSION_THRESHOLD, 400_000_000)
        .addTo(fs)
  }
}
