package rhynia.nyx.api.recipe.dsl

import gregtech.api.enums.GTValues
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.util.GTRecipeBuilder

class RecipeMapBuilder(
    private val backend: IRecipeMap,
) {
    fun newRecipe(block: GTRecipeBuilder.() -> Unit) {
        GTValues.RA
            .stdBuilder()
            .apply(block)
            .noOptimize()
            .addTo(backend)
    }
}
