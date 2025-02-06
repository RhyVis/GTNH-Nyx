package vis.rhynia.nova.common.material.generation

import gregtech.api.enums.FluidState
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.recipe.RecipeCategories
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTUtility
import net.minecraftforge.fluids.FluidContainerRegistry
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.common.recipe.RecipePool

class SimpleMaterialRecipeLoader(private val material: SimpleMaterial) : RecipePool() {
  override fun loadRecipes() {
    loadDustRecipes()
    loadCellRecipes()
  }

  private fun loadDustRecipes() {
    if (!material.flagDust) return

    GTModHandler.addCraftingRecipe(
        material.get(OrePrefixes.dust),
        arrayOf("TTT", "TTT", "TTT", 'T', material.get(OrePrefixes.dustTiny)))
    GTModHandler.addCraftingRecipe(
        material.get(OrePrefixes.dust),
        arrayOf("TT ", "TT ", 'T', material.get(OrePrefixes.dustSmall)))
    GTModHandler.addCraftingRecipe(
        material.get(OrePrefixes.dustSmall, 4), arrayOf(" T ", 'T', material.get(OrePrefixes.dust)))
    GTModHandler.addCraftingRecipe(
        material.get(OrePrefixes.dustTiny, 9), arrayOf("T  ", 'T', material.get(OrePrefixes.dust)))

    builder()
        .itemInputs(material.get(OrePrefixes.dustTiny, 9), ItemList.Schematic_Dust.get(0L))
        .itemOutputs(material.get(OrePrefixes.dust))
        .durSec(5)
        .eut(4)
        .addTo(RecipeMaps.packagerRecipes)

    builder()
        .itemInputs(material.get(OrePrefixes.dustSmall, 4), ItemList.Schematic_Dust.get(0L))
        .itemOutputs(material.get(OrePrefixes.dust))
        .durSec(5)
        .eut(4)
        .addTo(RecipeMaps.packagerRecipes)

    builder()
        .itemInputs(material.get(OrePrefixes.dustTiny, 9), ItemList.Schematic_3by3.get(0L))
        .itemOutputs(material.get(OrePrefixes.dust))
        .durSec(5)
        .eut(4)
        .addTo(RecipeMaps.packagerRecipes)

    builder()
        .itemInputs(material.get(OrePrefixes.dustSmall, 4), ItemList.Schematic_2by2.get(0L))
        .itemOutputs(material.get(OrePrefixes.dust))
        .durSec(5)
        .eut(4)
        .addTo(RecipeMaps.packagerRecipes)
  }

  private fun loadCellRecipes() {
    if (!material.flagFluid) return
    SimpleMaterialLoader.fluidMap[material]?.forEach { (state, fluid) ->
      if (SimpleMaterial.Companion.shouldHasCell(state))
          FluidContainerRegistry.FluidContainerData(
                  FluidStack(fluid, 1000),
                  material.getCell(state),
                  Materials.Empty.getCells(1),
              )
              .let {
                GTUtility.addFluidContainerData(it)
                FluidContainerRegistry.registerFluidContainer(it)
              }

      if (SimpleMaterial.Companion.shouldHasForestryCell(state))
          FluidContainerRegistry.FluidContainerData(
                  FluidStack(fluid, 1000),
                  GTModHandler.getModItem(Mods.Forestry.ID, "waxCapsule", 1),
                  Materials.Empty.getCells(1),
              )
              .let {
                GTUtility.addFluidContainerData(it)
                FluidContainerRegistry.registerFluidContainer(it)
              }

      if (material.flagDust && state in setOf(FluidState.LIQUID, FluidState.MOLTEN)) {
        val fluidAmount = if (state == FluidState.LIQUID) 1000 else 144
        val eu = if (material.mass > 128) 64 else 30
        builder()
            .itemInputs(material.getDust())
            .fluidOutputs(material.getFluidStack(state, fluidAmount))
            .duration(30)
            .eut(material.mass)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(RecipeMaps.fluidExtractionRecipes)
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(material.getDust())
            .fluidInputs(material.getFluidStack(state, fluidAmount))
            .duration(material.mass)
            .eut(eu)
            .addTo(RecipeMaps.fluidSolidifierRecipes)
      }
    }
  }
}
