package vis.rhynia.nova.api.recipe

import com.gtnewhorizons.modularui.common.widget.ProgressBar
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBuilder
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.recipe.frontend.AstralForgeRecipeFrontend
import vis.rhynia.nova.api.recipe.frontend.IntegratedAssemblyFrontend
import vis.rhynia.nova.api.recipe.frontend.MicroAssemblyFrontend
import vis.rhynia.nova.api.recipe.frontend.ThermonuclearControlFrontend
import vis.rhynia.nova.api.recipe.frontend.TranscendentReactorFrontend
import vis.rhynia.nova.common.loader.container.NovaItemList

object NovaRecipeMaps {
  /** Astral Forge (AF) Recipe */
  val astralForgeRecipes: RecipeMap<RecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.astralForge")
          .maxIO(16, 16, 8, 8)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .logoSize(17, 17)
          .logoPos(79, 52)
          .neiTransferRect(80, 30, 15, 15)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(15, 15)
          .progressBarPos(80, 30)
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.AstralForge.get(1)).setMaxRecipesPerPage(2)
          }
          .neiRecipeBackgroundSize(170, 10 + 6 * 18)
          .frontend(::AstralForgeRecipeFrontend)
          .build()

  /** Thermonuclear Control (TC) Recipe */
  val thermonuclearControlRecipes: RecipeMap<RecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.thermonuclearControl")
          .maxIO(4, 0, 2, 4)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .logoSize(17, 17)
          .logoPos(79, 52)
          .progressBar(GTUITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW)
          .progressBarSize(17, 17)
          .progressBarPos(79, 27)
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.AtomMacro.get(1)).setMaxRecipesPerPage(2)
          }
          .neiRecipeBackgroundSize(170, 10 + 4 * 18)
          .frontend(::ThermonuclearControlFrontend)
          .build()

  /** Transcendent Reactor (TR) Recipe (w/ stack 64+) */
  val transcendentReactorRecipes: RecipeMap<NovaRecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.transcendentReactor", ::NovaRecipeMapBackend)
          .maxIO(16, 6, 8, 6)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .logoSize(17, 17)
          .logoPos(84, 80)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(15, 15)
          .progressBarPos(85, 27)
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.AtomMacro.get(1)).setMaxRecipesPerPage(1)
          }
          .neiRecipeBackgroundSize(170, 10 + 6 * 18)
          .frontend(::TranscendentReactorFrontend)
          .build()

  /** Integrated Assembly (IA) Recipe (w/ stack 64+) */
  val integratedAssemblyRecipes: RecipeMap<NovaRecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.integratedAssembly", ::NovaRecipeMapBackend)
          .maxIO(12, 1, 8, 0)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .logoSize(17, 17)
          .logoPos(79 + 18 * 4, 8 + 18 * 4)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(17, 17)
          .progressBarPos(9 + 88, 27)
          .neiTransferRect(9 + 88, 27, 17, 17)
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.AstralForge.get(1)).setMaxRecipesPerPage(1)
          }
          .neiRecipeBackgroundSize(170, 10 + 5 * 18)
          .frontend(::IntegratedAssemblyFrontend)
          .build()

  /** Micro Assembly (MA) Recipe (w/ stack 64+) */
  val microAssemblyRecipes: RecipeMap<NovaRecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.microAssembly", ::NovaRecipeMapBackend)
          .maxIO(8, 1, 8, 0)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .logoSize(17, 17)
          .logoPos(79 + 18 * 4, 8 + 18 * 3)
          .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
          .progressBarSize(17, 17)
          .progressBarPos(9 + 88, 27)
          .neiTransferRect(9 + 88, 27, 17, 17)
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.AstralForge.get(1)).setMaxRecipesPerPage(2)
          }
          .neiRecipeBackgroundSize(170, 10 + 4 * 18)
          .frontend(::MicroAssemblyFrontend)
          .build()

  /** Superconducting Forming (SF) Recipe (w/ stack 64+) */
  val superconductingFormingRecipes: RecipeMap<NovaRecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.superconductingForming", ::NovaRecipeMapBackend)
          .maxIO(6, 2, 3, 2)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .disableOptimize()
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.AssemblyMatrix.get(1)).setMaxRecipesPerPage(2)
          }
          .build()

  /** Quark Refactoring (QR) Recipe (w/ stack 64+) */
  val quarkRefactoringRecipes: RecipeMap<NovaRecipeMapBackend> =
      RecipeMapBuilder.of("nova.recipe.quarkRefactoring", ::NovaRecipeMapBackend)
          .maxIO(6, 2, 3, 2)
          .minInputs(1, 0)
          .logo(NovaValues.TextureSets.NovaLogo32)
          .disableOptimize()
          .neiHandlerInfo {
            it.setDisplayStack(NovaItemList.DenseEndpoint.get(1)).setMaxRecipesPerPage(2)
          }
          .build()
}
