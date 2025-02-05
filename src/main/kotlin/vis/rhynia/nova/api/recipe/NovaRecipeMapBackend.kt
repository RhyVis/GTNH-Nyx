package vis.rhynia.nova.api.recipe

import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder

class NovaRecipeMapBackend(builder: RecipeMapBackendPropertiesBuilder) : RecipeMapBackend(builder) {
  override fun reInit() {}
}
