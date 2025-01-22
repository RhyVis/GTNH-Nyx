package vis.rhynia.nova.api.recipe

import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBuilder
import vis.rhynia.nova.api.recipe.frontend.AstralForgeRecipeFrontend
import vis.rhynia.nova.api.recipe.frontend.IntegratedAssemblyFrontend
import vis.rhynia.nova.api.recipe.frontend.MicroAssemblyFrontend
import vis.rhynia.nova.common.NovaItemList

object NovaRecipeMaps {
  val astralForgeRecipes: RecipeMap<RecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.astralForge")
          .maxIO(16, 16, 8, 8)
          .minInputs(1, 0)
          // .logo(VA_Values.TextureSets.VA_LOGO_32)
          .logoSize(17, 17)
          .logoPos(79, 52)
          .neiTransferRect(80, 30, 15, 15)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(15, 15)
          .progressBarPos(80, 30)
          .neiHandlerInfo { it.setDisplayStack(NovaItemList.AstralForge.get(1)) }
          .neiRecipeBackgroundSize(170, 10 + 6 * 18)
          .frontend(::AstralForgeRecipeFrontend)
          .build()
  val integratedAssemblyRecipes: RecipeMap<RecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.integratedAssembly")
          .maxIO(12, 1, 8, 0)
          .minInputs(1, 0)
          // .logo(VA_Values.TextureSets.VA_LOGO_32)
          .logoSize(17, 17)
          .logoPos(79 + 18 * 4, 8 + 18 * 4)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(17, 17)
          .progressBarPos(9 + 88, 27)
          .neiTransferRect(9 + 88, 27, 17, 17)
          .neiHandlerInfo { it.setDisplayStack(NovaItemList.AstralForge.get(1)) }
          .neiRecipeBackgroundSize(170, 10 + 5 * 18)
          .frontend(::IntegratedAssemblyFrontend)
          .build()
  val microAssemblyRecipes: RecipeMap<RecipeMapBackend> =
      RecipeMapBuilder.of("va.recipe.microAssembly")
          .maxIO(8, 1, 8, 0)
          .minInputs(1, 0)
          // .logo(VA_Values.TextureSets.VA_LOGO_32)
          .logoSize(17, 17)
          .logoPos(79 + 18 * 4, 8 + 18 * 3)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(17, 17)
          .progressBarPos(9 + 88, 27)
          .neiTransferRect(9 + 88, 27, 17, 17)
          .neiHandlerInfo { it.setDisplayStack(NovaItemList.AstralForge.get(1)) }
          .neiRecipeBackgroundSize(170, 10 + 4 * 18)
          .frontend(::MicroAssemblyFrontend)
          .build()
}
