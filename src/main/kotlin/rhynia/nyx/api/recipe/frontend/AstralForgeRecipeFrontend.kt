package rhynia.nyx.api.recipe.frontend

import com.gtnewhorizons.modularui.api.math.Pos2d
import gregtech.api.recipe.BasicUIPropertiesBuilder
import gregtech.api.recipe.NEIRecipePropertiesBuilder
import gregtech.api.recipe.RecipeMapFrontend
import gregtech.common.gui.modularui.UIHelper

class AstralForgeRecipeFrontend(
    uiBuilder: BasicUIPropertiesBuilder,
    neiBuilder: NEIRecipePropertiesBuilder
) : RecipeMapFrontend(uiBuilder, neiBuilder) {

  companion object {
    private const val Y_ORIGIN = 8
    private const val X_ORIGIN_L = 6
    private const val X_ORIGIN_R = 98
    private const val ROW_WIDTH = 18
    private const val X_DIRECTION_MAX = 4
  }

  override fun getItemInputPositions(itemInputCount: Int): List<Pos2d> {
    return UIHelper.getGridPositions(itemInputCount, X_ORIGIN_L, Y_ORIGIN, X_DIRECTION_MAX)
  }

  override fun getItemOutputPositions(itemOutputCount: Int): List<Pos2d> {
    return UIHelper.getGridPositions(itemOutputCount, X_ORIGIN_R, Y_ORIGIN, X_DIRECTION_MAX)
  }

  override fun getFluidInputPositions(fluidInputCount: Int): List<Pos2d> {
    return UIHelper.getGridPositions(
        fluidInputCount, X_ORIGIN_L, Y_ORIGIN + 4 * ROW_WIDTH, X_DIRECTION_MAX)
  }

  override fun getFluidOutputPositions(fluidOutputCount: Int): List<Pos2d> {
    return UIHelper.getGridPositions(
        fluidOutputCount, X_ORIGIN_R, Y_ORIGIN + 4 * ROW_WIDTH, X_DIRECTION_MAX)
  }
}
