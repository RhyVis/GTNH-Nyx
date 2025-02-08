package rhynia.constellation.common.recipe.cel

import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.util.GTUtility
import rhynia.constellation.Config
import rhynia.constellation.api.enums.CelMods
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_MAX
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UMV
import rhynia.constellation.api.enums.ref.BasicRef
import rhynia.constellation.api.enums.ref.Tier
import rhynia.constellation.api.recipe.CelRecipeMaps
import rhynia.constellation.api.util.StackUtil.copyAmountUnsafe
import rhynia.constellation.common.recipe.RecipePool

class QuarkRefactoringRecipePool : RecipePool() {
  private val qa = CelRecipeMaps.quarkRefactoringRecipes

  override fun loadRecipes() {
    loadMain()
    if (Config.loadTstRecipe && CelMods.TwistSpaceTechnology.isModLoaded) loadTst()
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
            CelMods.TwistSpaceTechnology.getItem("MetaItem01", 1, 13).copyAmountUnsafe(512))
        .eut(RECIPE_MAX)
        .durSec(16)
        .addTo(qa)
  }
}
