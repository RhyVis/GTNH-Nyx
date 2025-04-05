package rhynia.nyx.api.recipe.dsl

import gregtech.api.enums.GTValues
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.util.GTRecipeBuilder

fun withRecipeMap(
    backend: IRecipeMap,
    block: RecipeMapBuilder.() -> Unit,
) {
    RecipeMapBuilder(backend).apply(block)
}

class RecipeMapBuilder(
    val backend: IRecipeMap,
) {
    inline fun newRecipe(block: GTRecipeBuilder.() -> Unit) {
        GTValues.RA
            .stdBuilder()
            .apply(block)
            .noOptimize()
            .addTo(backend)
    }

    inline fun newRecipeIf(
        condition: Boolean,
        block: GTRecipeBuilder.() -> Unit,
    ) {
        if (condition) {
            GTValues.RA
                .stdBuilder()
                .apply(block)
                .noOptimize()
                .addTo(backend)
        }
    }

    inline fun <T> newRecipeIter(
        iter: Iterable<T>,
        block: GTRecipeBuilder.(T) -> Unit,
    ) {
        for (item in iter) {
            GTValues.RA
                .stdBuilder()
                .apply { block(this, item) }
                .noOptimize()
                .addTo(backend)
        }
    }
}
