package rhynia.nyx.common.recipe.nyx

import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.util.GTUtility
import rhynia.nyx.Config
import rhynia.nyx.api.enums.NyxMods
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_MAX
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UMV
import rhynia.nyx.api.enums.ref.BasicRef
import rhynia.nyx.api.enums.ref.Tier
import rhynia.nyx.api.recipe.NyxRecipeMaps
import rhynia.nyx.api.util.StackUtil.copyAmountUnsafe
import rhynia.nyx.common.recipe.RecipePool

class QuarkRefactoringRecipePool : RecipePool() {
  private val qa = NyxRecipeMaps.quarkRefactoringRecipes

  override fun loadRecipes() {
    loadMain()
    if (Config.loadTstRecipe && NyxMods.TwistSpaceTechnology.isModLoaded) loadTst()
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
            NyxMods.TwistSpaceTechnology.getItem("MetaItem01", 1, 13).copyAmountUnsafe(512))
        .eut(RECIPE_MAX)
        .durSec(16)
        .addTo(qa)
  }
}
