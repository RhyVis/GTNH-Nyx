package rhynia.nyx.common.recipe

import gregtech.api.enums.ItemList
import gregtech.api.recipe.RecipeMaps
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UMV
import rhynia.nyx.api.recipe.RecipePool
import rhynia.nyx.api.recipe.dsl.withRecipeMap
import rhynia.nyx.api.util.debugItem

class TestRecipe : RecipePool() {
    override fun loadRecipes() {
        withRecipeMap(RecipeMaps.assemblerRecipes) {
            newRecipe {
                itemInputs(
                    debugItem("What?1"),
                    debugItem("What?2"),
                    debugItem("What?3"),
                    debugItem("What?4", "How?"),
                )
                itemOutputs(
                    ItemList.AlloySmelterUV.get(1),
                )
                eut(RECIPE_UMV)
                durSec(1)
            }
        }
    }
}
