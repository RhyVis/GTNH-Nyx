package vis.rhynia.nova.common.recipe.nova

import gregtech.api.enums.Materials
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import vis.rhynia.nova.api.enums.ref.SolderMaterial
import vis.rhynia.nova.api.enums.ref.SuperConductorPart
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.common.material.NovaMaterials
import vis.rhynia.nova.common.recipe.RecipePool

class SuperconductingFormingRecipePool : RecipePool() {
  private val sf = NovaRecipeMaps.superconductingFormingRecipes

  override fun loadRecipes() {
    // Wire x1 from dust
    for (sc in SuperConductorPart.entries) altBuilder(sf) {
      itemInputs(GTUtility.getIntegratedCircuit(1), sc.getPump(4), sc.getDust(1).ofSize(128))
          .itemOutputs(sc.getWire(1).ofSize(512))
          .fluidInputs(
              NovaMaterials.Astrium.getMolten(sc.multiplier * INGOTS),
              sc.getSxEqualFluid(32),
              Materials.Helium.getGas(sc.multiplier * 8L * BUCKETS))
          .eut(sc.recipeVoltage)
          .durSec(64)
    }

    // Wire x1 from molten
    for (sc in SuperConductorPart.entries) altBuilder(sf) {
      itemInputs(GTUtility.getIntegratedCircuit(11), sc.getPump(4))
          .itemOutputs(sc.getWire(1).ofSize(512))
          .fluidInputs(
              sc.getMolten(128 * INGOTS), Materials.Helium.getGas(sc.multiplier * 8L * BUCKETS))
          .eut(sc.recipeVoltage)
          .durSec(16)
    }

    // Frame from molten
    for (sc in SuperConductorPart.entries) altBuilder(sf) {
      itemInputs(GTUtility.getIntegratedCircuit(4), sc.getDust(1).ofSize(256))
          .itemOutputs(sc.getFrame(64))
          .fluidInputs(SolderMaterial.SolderingAlloy.getFluidStack(2 * sc.multiplier * INGOTS))
          .eut(sc.recipeVoltageLow)
          .durSec(20)
    }

    // Solenoid from molten
    for (sc in SuperConductorPart.entries) altBuilder(sf) {
      itemInputs(GTUtility.getIntegratedCircuit(8), sc.getWire(1).ofSize(128))
          .itemOutputs(sc.getSolenoid(16))
          .fluidInputs(SolderMaterial.IndaAlloy.getFluidStack(sc.powVal * INGOTS))
          .eut(sc.recipeVoltageHigh)
          .durSec(16)
    }

    // Base from Sx
    for (sc in SuperConductorPart.entries) altBuilder(sf) {
      itemInputs(
              GTUtility.getIntegratedCircuit(24),
              sc.getSolenoid(0),
              NovaMaterials.Astrium.getDust(1).ofSize(16 * sc.multiplier))
          .itemOutputs(sc.getDust(64))
          .fluidInputs(sc.getSxEqualFluid(16), Materials.UUMatter.getFluid(sc.powVal * 500L))
          .fluidOutputs(sc.getMolten(512 * INGOTS))
          .eut(sc.recipeVoltageHigh)
          .durSec(32)
    }
  }
}
