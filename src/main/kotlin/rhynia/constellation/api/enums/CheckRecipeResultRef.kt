package rhynia.constellation.api.enums

import gregtech.api.recipe.check.SimpleCheckRecipeResult

object CheckRecipeResultRef {
  val INSUFFICIENT_POWER_NO_VAL by lazy {
    SimpleCheckRecipeResult.ofFailure("insufficient_power_no_val")
  }
  val NO_PLANET_BLOCK by lazy { SimpleCheckRecipeResult.ofFailure("no_planet_block") }
  val NO_SELECTED_ENERGY_CORE by lazy {
    SimpleCheckRecipeResult.ofFailure("no_selected_energy_core_set")
  }
}
