package rhynia.constellation.common.recipe.gt

import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.common.items.CombType
import gregtech.loaders.misc.GTBees
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.api.util.FluidUtil
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

class HammerRecipePool : RecipePool() {
  override fun loadRecipes() {
    val hammer: IRecipeMap = RecipeMaps.hammerRecipes

    // region 杂项
    if (Mods.Forestry.isModLoaded) {
      // Nt
      builder()
          .itemInputs(
              GTBees.combs.getStackForType(CombType.NEUTRONIUM, 16),
              CelMaterials.Astrium.getDust(12),
          )
          .itemOutputs(Materials.Neutronium.getIngots(2))
          .fluidOutputs(
              CelMaterials.AstriumMagic.getMolten(12),
              Materials.Neutronium.getMolten(2304),
          )
          .eut(CelValues.RecipeValues.RECIPE_LuV)
          .durSec(25)
          .addTo(hammer)

      // SpNt
      builder()
          .itemInputs(
              GTBees.combs.getStackForType(CombType.COSMICNEUTRONIUM, 16),
              CelMaterials.Astrium.getDust(12))
          .itemOutputs(Materials.CosmicNeutronium.getIngots(2))
          .fluidOutputs(
              CelMaterials.AstriumMagic.getMolten(12), Materials.CosmicNeutronium.getMolten(2304))
          .eut(CelValues.RecipeValues.RECIPE_LuV)
          .durSec(25)
          .addTo(hammer)

      // Kevlar
      builder()
          .itemInputs(
              GTBees.combs.getStackForType(CombType.KEVLAR, 16), CelMaterials.Astrium.getDust(12))
          .fluidOutputs(
              CelMaterials.AstriumMagic.getMolten(12),
              FluidUtil.getFluidStack("molten.kevlar", 4608))
          .eut(CelValues.RecipeValues.RECIPE_LuV)
          .durSec(25)
          .addTo(hammer)
    }
    // endregion

    // region 矩阵
    // 兰波顿矩阵
    builder()
        .itemInputs(getCoreItem("LapotronDust", 64), CelMaterials.Astrium.getDust(16))
        .itemOutputs(CelItemList.LapotronMatrix.get(4))
        .fluidInputs(CelMaterials.LapotronEnhancedFluid.getLiquid(12 * BUCKETS))
        .eut(CelValues.RecipeValues.RECIPE_LuV)
        .durSec(40)
        .addTo(hammer)

    // 晶体矩阵
    builder()
        .itemInputs(Materials.Aluminiumoxide.getDust(64), CelMaterials.Astrium.getDust(16))
        .itemOutputs(CelItemList.CrystalMatrix.get(16))
        .fluidInputs(Materials.Europium.getIngotMolten(4), Materials.Americium.getIngotMolten(4))
        .fluidOutputs()
        .eut(CelValues.RecipeValues.RECIPE_ZPM)
        .durSec(45)
        .addTo(hammer)

    // endregion

    // region 致密云母
    // 致密云母
    builder()
        .itemInputs(getCoreItem("MicaInsulatorSheet", 4))
        .itemOutputs(CelItemList.DenseMicaInsulatorFoil.get(1))
        .eut(CelValues.RecipeValues.RECIPE_LV)
        .durSec(5)
        .addTo(hammer)
    // endregion

  }
}
