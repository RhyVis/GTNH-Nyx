package vis.rhynia.nova.common.loader

import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.recipe.nova.AstralForgeRecipePool

object RecipeLoader {
  fun loadAtCompleteInit() {
    arrayOf<RecipePool>(AstralForgeRecipePool()).forEach { it.loadRecipes() }
  }
}
