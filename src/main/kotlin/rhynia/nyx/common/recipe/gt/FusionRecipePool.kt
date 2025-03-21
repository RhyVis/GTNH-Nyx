package rhynia.nyx.common.recipe.gt

import goodgenerator.items.GGMaterial
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

class FusionRecipePool : RecipePool() {
    override fun loadRecipes() {
        val fs: IRecipeMap = RecipeMaps.fusionRecipes

        // ACR
        builder()
            .fluidInputs(
                NyxMaterials.AstralCatalystBaseExcited.getLiquid(500),
                GGMaterial.orundum.getMolten(288),
            ).fluidOutputs(NyxMaterials.AstralCatalystReforged.getLiquid(125))
            .durSec(2)
            .eut(NyxValues.RecipeValues.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 400_000_000)
            .addTo(fs)
    }
}
