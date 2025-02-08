package rhynia.constellation.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods.GTNHIntergalactic
import gregtech.api.enums.Mods.GregTech
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTUtility
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.api.util.FluidUtil
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class ChemicalReactorRecipePool : RecipePool() {
  override fun loadRecipes() {
    val lcr: IRecipeMap = RecipeMaps.multiblockChemicalReactorRecipes
    // region 杂项
    // 磷化钙
    builder()
        .itemInputs(
            Materials.Calcium.getDust(3),
            Materials.Phosphate.getDust(8),
            GTUtility.getIntegratedCircuit(2))
        .itemOutputs(Materials.TricalciumPhosphate.getGems(5))
        .eut(CelValues.RecipeValues.RECIPE_HV)
        .durSec(21)
        .addTo(lcr)

    // 磷酸
    builder()
        .itemInputs(Materials.Phosphate.getDust(8))
        .fluidInputs(Materials.Water.getFluid(12000))
        .fluidOutputs(Materials.PhosphoricAcid.getFluid(10000))
        .eut(CelValues.RecipeValues.RECIPE_LV)
        .durSec(21)
        .addTo(lcr)

    // 氟锑酸
    builder()
        .itemInputs(Materials.Antimony.getDust(8), GTUtility.getIntegratedCircuit(20))
        .fluidInputs(Materials.HydrofluoricAcid.getFluid(96000))
        .fluidOutputs(
            FluidUtil.getFluidStack("fluoroantimonic acid", 16000),
            Materials.Hydrogen.getGas(80000))
        .eut(CelValues.RecipeValues.RECIPE_EV)
        .durSec(45)
        .addTo(lcr)

    // 钯金属粉处理
    builder()
        .itemInputs(WerkstoffLoader.PDMetallicPowder.getDust(8), GTUtility.getIntegratedCircuit(24))
        .itemOutputs(WerkstoffLoader.PDRawPowder.getDust(8))
        .eut(CelValues.RecipeValues.RECIPE_LV)
        .durSec(15)
        .addTo(lcr)

    // 干细胞
    val stemCell = GregTech.getItem("gt.metaitem.03", 64, 32073)
    builder(GTNHIntergalactic)?.also {
      it.itemInputs(CelMaterials.Astrium.getDust(12), Materials.Osmiridium.getDust(8))
          .itemOutputs(*Array(3) { stemCell }, Materials.MysteriousCrystal.getDust(2))
          .fluidInputs(Materials.GrowthMediumSterilized.getFluid(4000))
          .fluidOutputs(FluidUtil.getFluidStack("bacterialsludge", 4000))
          .eut(CelValues.RecipeValues.RECIPE_LuV)
          .durSec(21)
          .addTo(lcr)
    }
  }
}
