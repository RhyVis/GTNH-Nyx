package vis.rhynia.nova.common.recipe.nova

import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.util.GTUtility
import vis.rhynia.nova.Config
import vis.rhynia.nova.api.enums.NovaMods
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_MAX
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UMV
import vis.rhynia.nova.api.enums.ref.BasicRef
import vis.rhynia.nova.api.enums.ref.Tier
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.api.util.StackUtil.copyAmountUnsafe
import vis.rhynia.nova.common.recipe.RecipePool

class QuarkRefactoringRecipePool : RecipePool() {
  private val qa = NovaRecipeMaps.quarkRefactoringRecipes

  override fun loadRecipes() {
    loadMain()
    if (Config.loadTstRecipe && NovaMods.TwistSpaceTechnology.isModLoaded) loadTst()
  }

  private fun loadMain() {
    // 量子反常
    builder()
        .itemInputs(Tier.UXV.getCircuitWrap(1), BasicRef.getFusionMatrixCatalyst())
        .fluidInputs(
            Materials.Duranium.getIngotMolten(256), MaterialsUEVplus.SpaceTime.getIngotMolten(64))
        .itemOutputs(GTUtility.copyAmountUnsafe(4096, BasicRef.getQuantumAnomaly(1)))
        .eut(RECIPE_UMV)
        .durSec(16)
        .addTo(qa)
  }

  private fun loadTst() {
    // 临界光子
    builder()
        .itemInputs(
            GTUtility.copyAmountUnsafe(128, BasicRef.getQuantumAnomaly(1)),
            BasicRef.getFusionMatrixCatalyst())
        .itemOutputs(
            // GTUtility.copyAmountUnsafe(512, GTCMItemList.CriticalPhoton.get(1))
            NovaMods.TwistSpaceTechnology.getItem("MetaItem01", 1, 13).copyAmountUnsafe(512))
        .eut(RECIPE_MAX)
        .durSec(16)
        .addTo(qa)
  }
}
