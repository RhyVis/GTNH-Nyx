package rhynia.nyx.api.recipe

import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder

class NyxRecipeMapBackend(builder: RecipeMapBackendPropertiesBuilder) : RecipeMapBackend(builder) {
  override fun reInit() {}
}
