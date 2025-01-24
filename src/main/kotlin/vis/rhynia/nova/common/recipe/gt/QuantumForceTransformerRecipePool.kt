package vis.rhynia.nova.common.recipe.gt

import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER
import gregtech.api.util.GTUtility
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.item.chemistry.GenericChem
import gtPlusPlus.core.util.minecraft.ItemUtils
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UEV
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.material.NovaMaterial

class QuantumForceTransformerRecipePool : RecipePool {
  override fun loadRecipes() {
    val qft = GTPPRecipeMaps.quantumForceTransformerRecipes

    builder()
        .itemInputs(
            Materials.Calcium.getDust(32),
            NovaMaterial.Astrium.get(OrePrefixes.dust, 32),
            ItemUtils.getSimpleStack(GenericChem.mRawIntelligenceCatalyst, 0))
        .fluidOutputs(
            Materials.GrowthMediumRaw.getFluid((1000 * 1024).toLong()),
            Materials.GrowthMediumSterilized.getFluid((1000 * 512).toLong()))
        .itemOutputs(GTUtility.copyAmountUnsafe(64 * 48, ItemList.Circuit_Chip_Stemcell.get(1)))
        .eut(RECIPE_UEV)
        .metadata(QFT_FOCUS_TIER, 3)
        .durSec(16)
        .addTo(qft)
  }
}
