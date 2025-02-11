package rhynia.nyx.api.recipe.frontend

import com.gtnewhorizons.modularui.api.math.Pos2d
import gregtech.api.recipe.BasicUIPropertiesBuilder
import gregtech.api.recipe.NEIRecipePropertiesBuilder
import gregtech.api.recipe.RecipeMapFrontend
import gregtech.common.gui.modularui.UIHelper

class TranscendentReactorFrontend(
    uiBuilder: BasicUIPropertiesBuilder,
    neiBuilder: NEIRecipePropertiesBuilder
) : RecipeMapFrontend(uiBuilder, neiBuilder) {
  override fun getItemInputPositions(itemInputCount: Int): List<Pos2d?> {
    return UIHelper.getGridPositions(itemInputCount, 6, 8, 4)
  }

  override fun getItemOutputPositions(itemOutputCount: Int): List<Pos2d?> {
    return UIHelper.getGridPositions(itemOutputCount, 9 + 98, 8 + 9, 3)
  }

  override fun getFluidInputPositions(fluidInputCount: Int): List<Pos2d?> {
    return UIHelper.getGridPositions(fluidInputCount, 6, 8 + 4 * 18, 4)
  }

  override fun getFluidOutputPositions(fluidOutputCount: Int): List<Pos2d?> {
    return UIHelper.getGridPositions(fluidOutputCount, 9 + 98, 8 + 4 * 18 - 9, 3)
  }
}
