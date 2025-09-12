package rhynia.nyx.api.recipe

import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMapBuilder
import gregtech.api.recipe.maps.FuelBackend
import gregtech.nei.formatter.FuelSpecialValueFormatter

@Suppress("SpellCheckingInspection")
object NyxRecipeMaps {
    object Generator {
        val restoneGenerator: RecipeMap<FuelBackend> =
            RecipeMapBuilder
                .of(
                    "nyx.recipe.restoneGenerator",
                ) { FuelBackend(it) }
                .maxIO(1, 1, 0, 0)
                .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
                .build()
    }
}
