package vis.rhynia.nova.common.recipe.gt

import gregtech.api.enums.Materials
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.common.items.CombType
import gregtech.loaders.misc.GTBees
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LuV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_ZPM
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.api.util.FluidUtil
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterial

class HammerRecipePool : RecipePool {
  override fun loadRecipes() {
    val hammer: IRecipeMap = RecipeMaps.hammerRecipes

    // region 杂项
    // Nt
    builder()
        .itemInputs(
            GTBees.combs.getStackForType(CombType.NEUTRONIUM, 16), NovaMaterial.Astrium.getDust(12))
        .itemOutputs(Materials.Neutronium.getIngots(2))
        .fluidOutputs(NovaMaterial.AstriumMagic.getMolten(12), Materials.Neutronium.getMolten(2304))
        .eut(RECIPE_LuV)
        .durSec(25)
        .addTo(hammer)

    // SpNt
    builder()
        .itemInputs(
            GTBees.combs.getStackForType(CombType.COSMICNEUTRONIUM, 16),
            NovaMaterial.Astrium.getDust(12))
        .itemOutputs(Materials.CosmicNeutronium.getIngots(2))
        .fluidOutputs(
            NovaMaterial.AstriumMagic.getMolten(12), Materials.CosmicNeutronium.getMolten(2304))
        .eut(RECIPE_LuV)
        .durSec(25)
        .addTo(hammer)

    // Kevlar
    builder()
        .itemInputs(
            GTBees.combs.getStackForType(CombType.KEVLAR, 16), NovaMaterial.Astrium.getDust(12))
        .fluidOutputs(
            NovaMaterial.AstriumMagic.getMolten(12), FluidUtil.getFluidStack("molten.kevlar", 4608))
        .eut(RECIPE_LuV)
        .durSec(25)
        .addTo(hammer)
    // endregion

    // region 矩阵
    // 兰波顿矩阵
    builder()
        .itemInputs(getCoreItem("LapotronDust", 64), NovaMaterial.Astrium.getDust(16))
        .itemOutputs(NovaItemList.LapotronMatrix.get(4))
        .fluidInputs(NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(12 * BUCKETS))
        .eut(RECIPE_LuV)
        .durSec(40)
        .addTo(hammer)

    // 晶体矩阵
    builder()
        .itemInputs(Materials.Aluminiumoxide.getDust(64), NovaMaterial.Astrium.getDust(16))
        .itemOutputs(NovaItemList.CrystalMatrix.get(16))
        .fluidInputs(Materials.Europium.getIngotMolten(4), Materials.Americium.getIngotMolten(4))
        .fluidOutputs()
        .eut(RECIPE_ZPM)
        .durSec(45)
        .addTo(hammer)

    // endregion

    // region 致密云母
    // 致密云母
    builder()
        .itemInputs(getCoreItem("MicaInsulatorSheet", 4))
        .itemOutputs(NovaItemList.DenseMicaInsulatorFoil.get(1))
        .eut(RECIPE_LV)
        .durSec(5)
        .addTo(hammer)
    // endregion

  }
}
