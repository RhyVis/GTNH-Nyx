package rhynia.nyx.api.recipe.dsl

import gregtech.api.enums.GTValues
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.util.GTRecipeBuilder

/**
 * Alias for standard recipe builder in [GTValues.RA].
 */
val RecipeBuilder: GTRecipeBuilder get() = GTValues.RA.stdBuilder()

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
        RecipeBuilder
            .apply(block)
            .addTo(backend)
    }

    inline fun newRecipeIf(
        condition: Boolean,
        block: GTRecipeBuilder.() -> Unit,
    ) {
        if (condition) {
            RecipeBuilder
                .apply(block)
                .addTo(backend)
        }
    }

    inline fun <T> newRecipeIter(
        iter: Iterable<T>,
        block: GTRecipeBuilder.(T) -> Unit,
    ) {
        for (item in iter) {
            RecipeBuilder
                .apply { block(this, item) }
                .addTo(backend)
        }
    }
}
