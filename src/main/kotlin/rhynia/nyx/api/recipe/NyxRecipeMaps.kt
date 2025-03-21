package rhynia.nyx.api.recipe

import com.gtnewhorizons.modularui.common.widget.ProgressBar
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBuilder
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.recipe.frontend.AstralForgeRecipeFrontend
import rhynia.nyx.api.recipe.frontend.IntegratedAssemblyFrontend
import rhynia.nyx.api.recipe.frontend.MicroAssemblyFrontend
import rhynia.nyx.api.recipe.frontend.ThermonuclearControlFrontend
import rhynia.nyx.api.recipe.frontend.TranscendentReactorFrontend
import rhynia.nyx.common.container.NyxItemList

object NyxRecipeMaps {
    /** Astral Forge (AF) Recipe */
    val astralForgeRecipes: RecipeMap<RecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.astralForge")
            .maxIO(16, 16, 8, 8)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .logoSize(17, 17)
            .logoPos(79, 52)
            .neiTransferRect(80, 30, 15, 15)
            .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
            .progressBarSize(15, 15)
            .progressBarPos(80, 30)
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.AstralForge.get(1)).setMaxRecipesPerPage(2)
            }.neiRecipeBackgroundSize(170, 10 + 6 * 18)
            .frontend(::AstralForgeRecipeFrontend)
            .build()

    /** Thermonuclear Control (TC) Recipe */
    val thermonuclearControlRecipes: RecipeMap<RecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.thermonuclearControl")
            .maxIO(4, 0, 2, 4)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .logoSize(17, 17)
            .logoPos(79, 52)
            .progressBar(GTUITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW)
            .progressBarSize(17, 17)
            .progressBarPos(79, 27)
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.AtomMacro.get(1)).setMaxRecipesPerPage(2)
            }.neiRecipeBackgroundSize(170, 10 + 4 * 18)
            .frontend(::ThermonuclearControlFrontend)
            .build()

    /** Transcendent Reactor (TR) Recipe (w/ stack 64+) */
    val transcendentReactorRecipes: RecipeMap<NyxRecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.transcendentReactor", ::NyxRecipeMapBackend)
            .maxIO(16, 6, 8, 6)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .logoSize(17, 17)
            .logoPos(84, 80)
            .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
            .progressBarSize(15, 15)
            .progressBarPos(85, 27)
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.AtomMacro.get(1)).setMaxRecipesPerPage(1)
            }.neiRecipeBackgroundSize(170, 10 + 6 * 18)
            .frontend(::TranscendentReactorFrontend)
            .build()

    /** Integrated Assembly (IA) Recipe (w/ stack 64+) */
    val integratedAssemblyRecipes: RecipeMap<NyxRecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.integratedAssembly", ::NyxRecipeMapBackend)
            .maxIO(12, 1, 8, 0)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .logoSize(17, 17)
            .logoPos(79 + 18 * 4, 8 + 18 * 4)
            .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
            .progressBarSize(17, 17)
            .progressBarPos(9 + 88, 27)
            .neiTransferRect(9 + 88, 27, 17, 17)
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.AstralForge.get(1)).setMaxRecipesPerPage(1)
            }.neiRecipeBackgroundSize(170, 10 + 5 * 18)
            .frontend(::IntegratedAssemblyFrontend)
            .build()

    /** Micro Assembly (MA) Recipe (w/ stack 64+) */
    val microAssemblyRecipes: RecipeMap<NyxRecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.microAssembly", ::NyxRecipeMapBackend)
            .maxIO(8, 1, 8, 0)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .logoSize(17, 17)
            .logoPos(79 + 18 * 4, 8 + 18 * 3)
            .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
            .progressBarSize(17, 17)
            .progressBarPos(9 + 88, 27)
            .neiTransferRect(9 + 88, 27, 17, 17)
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.AstralForge.get(1)).setMaxRecipesPerPage(2)
            }.neiRecipeBackgroundSize(170, 10 + 4 * 18)
            .frontend(::MicroAssemblyFrontend)
            .build()

    /** Superconducting Forming (SF) Recipe (w/ stack 64+) */
    val superconductingFormingRecipes: RecipeMap<NyxRecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.superconductingForming", ::NyxRecipeMapBackend)
            .maxIO(6, 2, 3, 2)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .disableOptimize()
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.AssemblyMatrix.get(1)).setMaxRecipesPerPage(2)
            }.build()

    /** Quark Refactoring (QR) Recipe (w/ stack 64+) */
    val quarkRefactoringRecipes: RecipeMap<NyxRecipeMapBackend> =
        RecipeMapBuilder
            .of("nyx.recipe.quarkRefactoring", ::NyxRecipeMapBackend)
            .maxIO(6, 2, 3, 2)
            .minInputs(1, 0)
            .logo(NyxValues.TextureSets.Logo32)
            .disableOptimize()
            .neiHandlerInfo {
                it.setDisplayStack(NyxItemList.DenseEndpoint.get(1)).setMaxRecipesPerPage(2)
            }.build()
}
