package vis.rhynia.nova.common.recipe.gt

import gregtech.api.enums.ItemList
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_EV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.SECOND
import vis.rhynia.nova.api.enums.ref.Tier
import vis.rhynia.nova.api.interfaces.RecipePool

class AssemblerRecipePool : RecipePool {
  private val assembler = RecipeMaps.assemblerRecipes

  override fun loadRecipes() {
    loadMain()
    loadWirelessHatchEasy()
  }

  private fun loadMain() {
    // Stocking input hatch
    builder()
        .itemInputs(
            ItemList.Super_Tank_EV.get(1),
            Tier.HV.getCircuit(1),
            Tier.HV.getComponent(Tier.Component.ElectricPump, 1),
            GTUtility.getIntegratedCircuit(23))
        .itemOutputs(ItemList.Hatch_Input_ME.get(1))
        .fluidInputs(Tier.HV.getSolder(72))
        .eut(RECIPE_EV)
        .durSec(8)
        .addTo(assembler)

    // Crafting input bus
    builder()
        .itemInputs(
            ItemList.Hatch_Input_Bus_ME.get(1),
            Tier.HV.getCircuit(2),
            Tier.HV.getComponent(Tier.Component.RobotArm, 2),
            GTUtility.getIntegratedCircuit(23))
        .itemOutputs(ItemList.Hatch_CraftingInput_Bus_ME_ItemOnly.get(1))
        .fluidInputs(Tier.HV.getSolder(72))
        .eut(RECIPE_EV)
        .durSec(8)
        .addTo(assembler)

    // Crafting input buffer
    builder()
        .itemInputs(
            ItemList.Hatch_Input_Bus_ME.get(1),
            ItemList.Hatch_Input_ME.get(1),
            Tier.EV.getCircuit(2),
            Tier.EV.getComponent(Tier.Component.RobotArm, 2),
            GTUtility.getIntegratedCircuit(23))
        .itemOutputs(ItemList.Hatch_CraftingInput_Bus_ME.get(1))
        .fluidInputs(Tier.EV.getSolder(72))
        .eut(RECIPE_EV)
        .durSec(8)
        .addTo(assembler)
  }

  private fun loadWirelessHatchEasy() {
    val time = 10 * SECOND

    for (tier in
        arrayOf<Tier>(
            Tier.LV,
            Tier.MV,
            Tier.HV,
            Tier.EV,
            Tier.IV,
            Tier.LuV,
            Tier.ZPM,
            Tier.UV,
            Tier.UHV,
            Tier.UEV,
            Tier.UIV,
            Tier.UMV,
            Tier.UXV)) {
      builder()
          .itemInputs(
              tier.getEnergyHatch(1),
              tier.getCircuit(1),
              tier.getComponent(Tier.Component.Emitter, 1),
              tier.getComponent(Tier.Component.Sensor, 1),
              GTUtility.getIntegratedCircuit(22))
          .fluidInputs(tier.getSolder(INGOTS))
          .itemOutputs(tier.getEnergyWireless(1))
          .eut(tier.voltageRecipe)
          .duration(time)
          .addTo(assembler)
      builder()
          .itemInputs(
              tier.getDynamoHatch(1),
              tier.getCircuit(1),
              tier.getComponent(Tier.Component.Emitter, 1),
              tier.getComponent(Tier.Component.Sensor, 1),
              GTUtility.getIntegratedCircuit(22))
          .fluidInputs(tier.getSolder(INGOTS))
          .itemOutputs(tier.getDynamoWireless(1))
          .eut(tier.voltageRecipe)
          .duration(time)
          .addTo(assembler)
    }

    for (tier in
        arrayOf<Tier>(
            Tier.EV,
            Tier.IV,
            Tier.LuV,
            Tier.ZPM,
            Tier.UV,
            Tier.UHV,
            Tier.UEV,
            Tier.UIV,
            Tier.UMV,
            Tier.UXV)) {
      builder()
          .itemInputs(
              tier.getEnergyHatch4A(1),
              tier.getCircuit(2),
              tier.getComponent(Tier.Component.Emitter, 2),
              tier.getComponent(Tier.Component.Sensor, 2),
              GTUtility.getIntegratedCircuit(22))
          .fluidInputs(tier.getSolder(4 * INGOTS))
          .itemOutputs(tier.getEnergyWireless4A(1))
          .eut(tier.voltageRecipe)
          .duration(time)
          .addTo(assembler)
      builder()
          .itemInputs(
              tier.getEnergyHatch16A(1),
              tier.getCircuit(4),
              tier.getComponent(Tier.Component.Emitter, 4),
              tier.getComponent(Tier.Component.Sensor, 4),
              GTUtility.getIntegratedCircuit(22))
          .fluidInputs(tier.getSolder(16 * INGOTS))
          .itemOutputs(tier.getEnergyWireless16A(1))
          .eut(tier.voltageRecipe)
          .duration(time)
          .addTo(assembler)
      builder()
          .itemInputs(
              tier.getEnergyHatch64A(1),
              tier.getCircuit(8),
              tier.getComponent(Tier.Component.Emitter, 8),
              tier.getComponent(Tier.Component.Sensor, 8),
              GTUtility.getIntegratedCircuit(22))
          .fluidInputs(tier.getSolder(64 * INGOTS))
          .itemOutputs(tier.getEnergyWireless64A(1))
          .eut(tier.voltageRecipe)
          .duration(time)
          .addTo(assembler)
    }

    for (tier in
        arrayOf<Tier>(
            Tier.IV,
            Tier.LuV,
            Tier.ZPM,
            Tier.UV,
            Tier.UHV,
            Tier.UEV,
            Tier.UIV,
            Tier.UMV,
            Tier.UXV)) {
      for (i in 1..5) {
        builder()
            .itemInputs(
                tier.getLaserTarget(i, 1),
                tier.getCircuit(16),
                tier.getComponent(Tier.Component.Emitter, 16),
                tier.getComponent(Tier.Component.Sensor, 16),
                GTUtility.getIntegratedCircuit(22))
            .fluidInputs(tier.getSolder(64 * INGOTS))
            .itemOutputs(tier.getLaserWireless(i, 1))
            .eut(tier.voltageRecipe)
            .duration(time)
            .addTo(assembler)
      }
    }
  }
}
