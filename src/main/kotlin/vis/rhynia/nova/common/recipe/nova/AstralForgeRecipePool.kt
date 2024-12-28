package vis.rhynia.nova.common.recipe.nova

import gregtech.api.enums.Materials
import gregtech.api.util.GTUtility
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LuV
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.api.recipe.NovaRecipeMaps

class AstralForgeRecipePool : RecipePool {
  private val af = NovaRecipeMaps.astralForgeRecipes

  override fun loadRecipes() {
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10))
        .fluidInputs(Materials.UUMatter.getFluid(2048))
        .fluidOutputs(Materials.UUMatter.getFluid(32768))
        .eut(RECIPE_LuV)
        .durSec(40)
        .addTo(af)
  }
}
