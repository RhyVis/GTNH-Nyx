package vis.rhynia.nova.common.loader

import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.recipe.nova.AstralForgeRecipePool
import vis.rhynia.nova.common.recipe.nova.IntegratedAssemblyRecipePool
import vis.rhynia.nova.common.recipe.nova.MicroAssemblyRecipePool

object RecipeLoader {
  fun loadAtCompleteInit() {
    arrayOf<RecipePool>(
            AstralForgeRecipePool(), IntegratedAssemblyRecipePool(), MicroAssemblyRecipePool())
        .forEach { it.loadRecipes() }
  }
}
