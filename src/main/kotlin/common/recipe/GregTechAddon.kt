@file:Suppress("ktlint:standard:filename")

package rhynia.nyx.common.recipe

import gregtech.api.enums.Materials
import gregtech.api.recipe.RecipeMaps
import rhynia.nyx.api.enums.ref.Tier
import rhynia.nyx.api.recipe.RecipePool
import rhynia.nyx.api.recipe.dsl.withRecipeMap
import rhynia.nyx.common.material.NyxMaterials

class GregTechAddonRecipes : RecipePool() {
    override fun loadRecipes() {
        withRecipeMap(RecipeMaps.electrolyzerRecipes) {
            newRecipe {
                itemInputs(
                    Materials.Redstone.getDust(32),
                )
                itemOutputs(
                    NyxMaterials.Restone.getDust(24),
                )
                durSec(4)
                eut(Tier.LV)
            }
            newRecipe {
                itemInputs(
                    Materials.Redstone.getBlocks(16),
                )
                itemOutputs(
                    NyxMaterials.Restone.getDust(64),
                    NyxMaterials.Restone.getDust(64),
                )
                durSec(8)
                eut(Tier.MV)
            }
        }
    }
}
