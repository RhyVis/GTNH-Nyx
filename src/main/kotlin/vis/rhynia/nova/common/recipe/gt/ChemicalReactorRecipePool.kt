package vis.rhynia.nova.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTUtility
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_EV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_HV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LuV
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.material.NovaMaterial

class ChemicalReactorRecipePool : RecipePool {
  override fun loadRecipes() {
    val lcr: IRecipeMap? = RecipeMaps.multiblockChemicalReactorRecipes
    // region 杂项
    // 磷化钙
    builder()
        .itemInputs(
            Materials.Calcium.getDust(3),
            Materials.Phosphate.getDust(8),
            GTUtility.getIntegratedCircuit(2))
        .itemOutputs(Materials.TricalciumPhosphate.getGems(5))
        .eut(RECIPE_HV)
        .durSec(21)
        .addTo(lcr)

    // 磷酸
    builder()
        .itemInputs(Materials.Phosphate.getDust(8))
        .fluidInputs(Materials.Water.getFluid(12000))
        .fluidOutputs(Materials.PhosphoricAcid.getFluid(10000))
        .eut(RECIPE_LV)
        .durSec(21)
        .addTo(lcr)

    // 氟锑酸
    builder()
        .itemInputs(Materials.Antimony.getDust(8), GTUtility.getIntegratedCircuit(20))
        .fluidInputs(Materials.HydrofluoricAcid.getFluid(96000))
        .fluidOutputs(
            FluidStack(FluidRegistry.getFluid("fluoroantimonic acid"), 16000),
            Materials.Hydrogen.getGas(80000))
        .eut(RECIPE_EV)
        .durSec(45)
        .addTo(lcr)

    // 钯金属粉处理
    builder()
        .itemInputs(
            WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 8),
            GTUtility.getIntegratedCircuit(24))
        .itemOutputs(WerkstoffLoader.PDRawPowder.get(OrePrefixes.dust, 8))
        .eut(RECIPE_LV)
        .durSec(15)
        .addTo(lcr)

    // 干细胞
    builder()
        .itemInputs(NovaMaterial.Astrium.get(OrePrefixes.dust, 12), Materials.Osmiridium.getDust(8))
        .itemOutputs(
            GTModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32073),
            GTModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32073),
            GTModHandler.getModItem("gregtech", "gt.metaitem.03", 64, 32073),
            Materials.MysteriousCrystal.getDust(2))
        .fluidInputs(Materials.GrowthMediumSterilized.getFluid(4000))
        .fluidOutputs(FluidStack(FluidRegistry.getFluid("bacterialsludge"), 4000))
        .eut(RECIPE_LuV)
        .durSec(21)
        .addTo(lcr)
  }
}
