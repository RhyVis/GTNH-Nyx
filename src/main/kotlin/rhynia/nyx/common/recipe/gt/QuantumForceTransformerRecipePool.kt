package rhynia.nyx.common.recipe.gt

import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.item.chemistry.GenericChem
import gtPlusPlus.core.util.minecraft.ItemUtils
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.util.StackUtil.copyAmountUnsafe
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

class QuantumForceTransformerRecipePool : RecipePool() {
  override fun loadRecipes() {
    val qft = GTPPRecipeMaps.quantumForceTransformerRecipes

    builder()
        .itemInputs(
            Materials.Calcium.getDust(32),
            NyxMaterials.Astrium.getDust(32),
            ItemUtils.getSimpleStack(GenericChem.mRawIntelligenceCatalyst, 0))
        .fluidOutputs(
            Materials.GrowthMediumRaw.getBucketFluid(1024),
            Materials.GrowthMediumSterilized.getBucketFluid(512))
        .itemOutputs(ItemList.Circuit_Chip_Stemcell.get(1).copyAmountUnsafe(48 * 64))
        .eut(NyxValues.RecipeValues.RECIPE_UEV)
        .metadata(QFT_FOCUS_TIER, 3)
        .durSec(16)
        .addTo(qft)
  }
}
