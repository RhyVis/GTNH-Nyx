package rhynia.constellation.api.recipe

import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder

class CelRecipeMapBackend(builder: RecipeMapBackendPropertiesBuilder) : RecipeMapBackend(builder) {
  override fun reInit() {}
}
