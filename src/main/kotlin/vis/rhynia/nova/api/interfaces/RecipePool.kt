package vis.rhynia.nova.api.interfaces

import gregtech.api.util.GTRecipeBuilder
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.HOUR
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.MINUTE
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.SECOND

interface RecipePool {
  /** Load recipes into the recipe pool at Complete Load Stage */
  fun loadRecipes()

  /** Alias for RA.stdBuilder() */
  fun builder(): GTRecipeBuilder = GTRecipeBuilder.builder().noOptimize()

  fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder = this.duration(seconds * SECOND)

  fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder = this.duration(minutes * MINUTE)

  fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder = this.duration(hours * HOUR)
}
