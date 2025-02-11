package rhynia.nyx.common.recipe.gt

import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.common.items.CombType
import gregtech.loaders.misc.GTBees
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.util.FluidUtil
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

class HammerRecipePool : RecipePool() {
  override fun loadRecipes() {
    val hammer: IRecipeMap = RecipeMaps.hammerRecipes

    // region 杂项
    if (Mods.Forestry.isModLoaded) {
      // Nt
      builder()
          .itemInputs(
              GTBees.combs.getStackForType(CombType.NEUTRONIUM, 16),
              NyxMaterials.Astrium.getDust(12),
          )
          .itemOutputs(Materials.Neutronium.getIngots(2))
          .fluidOutputs(
              NyxMaterials.AstriumMagic.getMolten(12),
              Materials.Neutronium.getMolten(2304),
          )
          .eut(NyxValues.RecipeValues.RECIPE_LuV)
          .durSec(25)
          .addTo(hammer)

      // SpNt
      builder()
          .itemInputs(
              GTBees.combs.getStackForType(CombType.COSMICNEUTRONIUM, 16),
              NyxMaterials.Astrium.getDust(12))
          .itemOutputs(Materials.CosmicNeutronium.getIngots(2))
          .fluidOutputs(
              NyxMaterials.AstriumMagic.getMolten(12), Materials.CosmicNeutronium.getMolten(2304))
          .eut(NyxValues.RecipeValues.RECIPE_LuV)
          .durSec(25)
          .addTo(hammer)

      // Kevlar
      builder()
          .itemInputs(
              GTBees.combs.getStackForType(CombType.KEVLAR, 16), NyxMaterials.Astrium.getDust(12))
          .fluidOutputs(
              NyxMaterials.AstriumMagic.getMolten(12),
              FluidUtil.getFluidStack("molten.kevlar", 4608))
          .eut(NyxValues.RecipeValues.RECIPE_LuV)
          .durSec(25)
          .addTo(hammer)
    }
    // endregion

    // region 矩阵
    // 兰波顿矩阵
    builder()
        .itemInputs(getCoreItem("LapotronDust", 64), NyxMaterials.Astrium.getDust(16))
        .itemOutputs(NyxItemList.LapotronMatrix.get(4))
        .fluidInputs(NyxMaterials.LapotronEnhancedFluid.getLiquid(12 * BUCKETS))
        .eut(NyxValues.RecipeValues.RECIPE_LuV)
        .durSec(40)
        .addTo(hammer)

    // 晶体矩阵
    builder()
        .itemInputs(Materials.Aluminiumoxide.getDust(64), NyxMaterials.Astrium.getDust(16))
        .itemOutputs(NyxItemList.CrystalMatrix.get(16))
        .fluidInputs(Materials.Europium.getIngotMolten(4), Materials.Americium.getIngotMolten(4))
        .fluidOutputs()
        .eut(NyxValues.RecipeValues.RECIPE_ZPM)
        .durSec(45)
        .addTo(hammer)

    // endregion

    // region 致密云母
    // 致密云母
    builder()
        .itemInputs(getCoreItem("MicaInsulatorSheet", 4))
        .itemOutputs(NyxItemList.DenseMicaInsulatorFoil.get(1))
        .eut(NyxValues.RecipeValues.RECIPE_LV)
        .durSec(5)
        .addTo(hammer)
    // endregion

  }
}
