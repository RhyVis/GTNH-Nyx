package vis.rhynia.nova.common.recipe.gt

import gregtech.api.enums.Materials
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import vis.rhynia.nova.api.interfaces.RecipePool

class CompressorRecipePool : RecipePool {
  override fun loadRecipes() {

    val cp: IRecipeMap? = RecipeMaps.compressorRecipes

    // 青金石
    builder()
        .itemInputs(Materials.Lapis.getDust(9))
        .itemOutputs(Materials.Lapis.getBlocks(1))
        .noOptimize()
        .eut(2)
        .durSec(15)
        .addTo(cp)
  }
}
