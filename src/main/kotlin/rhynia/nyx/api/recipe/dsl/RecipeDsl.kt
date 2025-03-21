package rhynia.nyx.api.recipe.dsl

import gregtech.api.interfaces.IRecipeMap

fun injectRecipes(
    backend: IRecipeMap,
    block: RecipeMapBuilder.() -> Unit,
) {
    RecipeMapBuilder(backend).apply(block)
}
