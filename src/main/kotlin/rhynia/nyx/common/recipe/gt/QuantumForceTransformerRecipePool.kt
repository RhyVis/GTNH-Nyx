package rhynia.nyx.common.recipe.gt

import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.item.chemistry.GenericChem
import gtPlusPlus.core.util.minecraft.ItemUtils
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UEV
import rhynia.nyx.api.recipe.dsl.injectRecipes
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

class QuantumForceTransformerRecipePool : RecipePool() {
    override fun loadRecipes() =
        injectRecipes(GTPPRecipeMaps.quantumForceTransformerRecipes) {
            newRecipe {
                itemOutputs(
                    Materials.Calcium.getDust(32),
                    NyxMaterials.Astrium.getDust(32),
                    ItemUtils.getSimpleStack(GenericChem.mRawIntelligenceCatalyst, 0),
                )
                fluidInputs(
                    Materials.GrowthMediumRaw.getBucketFluid(1024),
                    Materials.GrowthMediumSterilized.getBucketFluid(512),
                )
                itemOutputs(
                    ItemList.Circuit_Chip_Stemcell.getAmountUnsafe(48 * 64),
                )
                eut(RECIPE_UEV)
                metadata(QFT_FOCUS_TIER, 3)
                durSec(16)
            }
        }
}
