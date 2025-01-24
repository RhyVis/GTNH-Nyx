package vis.rhynia.nova.common.recipe.nova

import gregtech.api.enums.Materials
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import vis.rhynia.nova.api.enums.ref.SCPart
import vis.rhynia.nova.api.enums.ref.SolderMaterial
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.api.util.ItemUtil
import vis.rhynia.nova.common.material.NovaMaterial

class SuperconductingFormingRecipePool : RecipePool {
  private val sf = NovaRecipeMaps.superconductingFormingRecipes

  override fun loadRecipes() {
    // Wire x1 from dust
    for (sc in SCPart.entries) {
      builder()
          .itemInputs(
              GTUtility.getIntegratedCircuit(1),
              sc.getPump(4),
              ItemUtil.setStackSize(sc.getDust(1), 128))
          .itemOutputs(ItemUtil.setStackSize(sc.getWire(1), 512))
          .fluidInputs(
              NovaMaterial.Astrium.getMolten(sc.getMultiplier() * INGOTS),
              sc.getSxEqualFluid(32),
              Materials.Helium.getGas(sc.getMultiplier() * 8L * BUCKETS))
          .eut(sc.getRecipeVoltage())
          .durSec(64)
          .addTo(sf)
    }

    // Wire x1 from molten
    for (sc in SCPart.entries) {
      builder()
          .itemInputs(GTUtility.getIntegratedCircuit(11), sc.getPump(4))
          .itemOutputs(ItemUtil.setStackSize(sc.getWire(1), 512))
          .fluidInputs(
              sc.getMolten(128 * INGOTS),
              Materials.Helium.getGas(sc.getMultiplier() * 8L * BUCKETS))
          .eut(sc.getRecipeVoltage())
          .durSec(16)
          .addTo(sf)
    }

    // Frame from molten
    for (sc in SCPart.values()) {
      builder()
          .itemInputs(GTUtility.getIntegratedCircuit(4), ItemUtil.setStackSize(sc.getDust(1), 256))
          .itemOutputs(sc.getFrame(64))
          .fluidInputs(SolderMaterial.SolderingAlloy.getFluidStack(2 * sc.getMultiplier() * INGOTS))
          .eut(sc.getRecipeVoltageLow())
          .durSec(20)
          .addTo(sf)
    }

    // Solenoid from molten
    for (sc in SCPart.entries) {
      builder()
          .itemInputs(GTUtility.getIntegratedCircuit(8), ItemUtil.setStackSize(sc.getWire(1), 128))
          .itemOutputs(sc.getSolenoid(16))
          .fluidInputs(SolderMaterial.IndaAlloy.getFluidStack(sc.getPowValue() * INGOTS))
          .eut(sc.getRecipeVoltageHigh())
          .durSec(16)
          .addTo(sf)
    }

    // Base from Sx
    for (sc in SCPart.entries) {
      builder()
          .itemInputs(
              GTUtility.getIntegratedCircuit(24),
              sc.getSolenoid(0),
              ItemUtil.setStackSize(NovaMaterial.Astrium.getDust(1), 16 * sc.getMultiplier()))
          .itemOutputs(sc.getDust(64))
          .fluidInputs(sc.getSxEqualFluid(16), Materials.UUMatter.getFluid(sc.getPowValue() * 500L))
          .fluidOutputs(sc.getMolten(512 * INGOTS))
          .eut(sc.getRecipeVoltageHigh())
          .durSec(32)
          .addTo(sf)
    }
  }
}
