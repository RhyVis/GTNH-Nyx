package vis.rhynia.nova.api.recipe.backend

import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder

/** Skip reInit() method to avoid GT ItemStack reducing to 64. */
class NovaRecipeBackend(builder: RecipeMapBackendPropertiesBuilder) : RecipeMapBackend(builder) {
  override fun reInit() {
    // Do nothing
  }
}
