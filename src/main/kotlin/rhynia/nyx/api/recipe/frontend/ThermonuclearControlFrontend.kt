package rhynia.nyx.api.recipe.frontend

import com.gtnewhorizons.modularui.api.math.Pos2d
import gregtech.api.recipe.BasicUIPropertiesBuilder
import gregtech.api.recipe.NEIRecipePropertiesBuilder
import gregtech.api.recipe.RecipeMapFrontend
import gregtech.common.gui.modularui.UIHelper

class ThermonuclearControlFrontend(
    uiBuilder: BasicUIPropertiesBuilder,
    neiBuilder: NEIRecipePropertiesBuilder,
) : RecipeMapFrontend(uiBuilder, neiBuilder) {
    override fun getItemInputPositions(itemInputCount: Int): List<Pos2d?> = UIHelper.getGridPositions(itemInputCount, 24, 17, 2)

    override fun getItemOutputPositions(itemOutputCount: Int): List<Pos2d?> = UIHelper.getGridPositions(itemOutputCount, 116, 8 + 18, 2)

    override fun getFluidInputPositions(fluidInputCount: Int): List<Pos2d?> = UIHelper.getGridPositions(fluidInputCount, 24, 17 + 2 * 18, 2)

    override fun getFluidOutputPositions(fluidOutputCount: Int): List<Pos2d?> = UIHelper.getGridPositions(fluidOutputCount, 116, 17 + 9, 2)
}
