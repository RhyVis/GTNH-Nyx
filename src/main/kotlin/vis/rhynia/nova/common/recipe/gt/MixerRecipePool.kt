package vis.rhynia.nova.common.recipe.gt

import goodgenerator.items.GGMaterial
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.material.MaterialsElements
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_HV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_IV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LuV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_MV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UHV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_ZPM
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.api.util.FluidUtil
import vis.rhynia.nova.common.material.NovaMaterial

class MixerRecipePool : RecipePool {
  override fun loadRecipes() {
    val mx: IRecipeMap? = RecipeMaps.mixerRecipes
    val mxNoCell: IRecipeMap = GTPPRecipeMaps.mixerNonCellRecipes

    // region 杂项
    // 深渊铁
    builder()
        .itemInputs(
            Materials.Iron.getDust(1),
            Materials.Thaumium.getDust(3),
            GTUtility.getIntegratedCircuit(2))
        .itemOutputs(Materials.ShadowIron.getDust(3))
        .noOptimize()
        .eut(RECIPE_HV)
        .durSec(21)
        .addTo(mx)

    // 星辉催化剂
    builder()
        .itemInputs(
            NovaMaterial.AstriumInfinity.get(OrePrefixes.dust, 8),
            Materials.InfinityCatalyst.getDust(17))
        .itemOutputs(NovaMaterial.AstralCatalystBase.get(OrePrefixes.dust, 50))
        .fluidInputs(
            Materials.Helium.getGas(12 * BUCKETS.toLong()),
            NovaMaterial.Astrium.getMolten(4 * INGOTS))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(12)
        .addTo(mxNoCell)

    // 超导通流
    builder()
        .itemInputs(
            NovaMaterial.AstralCatalystBase.get(OrePrefixes.dust, 12),
            Materials.TengamPurified.getDust(11),
            Materials.Infinity.getDust(10),
            Materials.CosmicNeutronium.getDust(8),
            Materials.NaquadahEnriched.getDust(6),
            Materials.Naquadria.getDust(5),
            GGMaterial.orundum.get(OrePrefixes.dust, 5),
            MaterialsElements.STANDALONE.ADVANCED_NITINOL.getDust(4),
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(4))
        .itemOutputs(
            NovaMaterial.SuperconductorFluxRaw.get(OrePrefixes.dust, 64),
            NovaMaterial.SuperconductorFluxRaw.get(OrePrefixes.dust, 15))
        .fluidInputs(
            Materials.Helium.getPlasma(6 * BUCKETS.toLong()),
            Materials.DraconiumAwakened.getIngotMolten(12),
            Materials.Indium.getIngotMolten(7))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(30)
        .addTo(mxNoCell)
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(3),
            Materials.Clay.getDust(4),
            Materials.Calcite.getDust(4),
            Materials.Quartzite.getDust(4))
        .fluidInputs(Materials.Water.getBucketFluid(16))
        .fluidOutputs(FluidUtil.getFluidStackByName("wet.concrete", 18 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_MV)
        .durSec(5)
        .addTo(mx)

    // endregion

    // endregion

    // region 生物培养基
    // 培养基
    builder()
        .itemInputs(
            Materials.Calcium.getDust(3),
            NovaMaterial.Astrium.get(OrePrefixes.dust, 4),
            GTUtility.getIntegratedCircuit(15))
        .fluidInputs(GTModHandler.getDistilledWater(4000))
        .fluidOutputs(Materials.GrowthMediumRaw.getFluid(8000))
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(15)
        .addTo(mx)

    // 生物培养基
    builder()
        .itemInputs(
            Materials.NetherStar.getDust(2),
            NovaMaterial.Astrium.get(OrePrefixes.dust, 6),
            GTUtility.getIntegratedCircuit(16))
        .fluidInputs(GTModHandler.getDistilledWater(4000))
        .fluidOutputs(Materials.BioMediumRaw.getFluid(8000))
        .noOptimize()
        .eut(RECIPE_ZPM)
        .durSec(20)
        .addTo(mx)

    // endregion

    // region 极寒-烈焰
    // 极寒
    builder()
        .itemInputs(
            Materials.CallistoIce.getDust(12),
            Materials.Ledox.getDust(12),
            NovaMaterial.Astrium.get(OrePrefixes.dust, 18),
            GTUtility.getIntegratedCircuit(15))
        .itemOutputs(Materials.Cryotheum.getDust(128))
        .fluidInputs(GTModHandler.getDistilledWater(64 * BUCKETS.toLong()))
        .fluidOutputs(FluidUtil.getFluidStackByName("cryotheum", 256 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_IV)
        .durSec(32)
        .addTo(mx)

    // 烈焰
    builder()
        .itemInputs(
            Materials.Sulfur.getDust(12),
            Materials.Coal.getDust(12),
            NovaMaterial.Astrium.get(OrePrefixes.dust, 18),
            GTUtility.getIntegratedCircuit(16))
        .itemOutputs(Materials.Pyrotheum.getDust(128))
        .fluidInputs(Materials.Lava.getBucketFluid(64))
        .fluidOutputs(FluidUtil.getFluidStackByName("pyrotheum", 256 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_IV)
        .durSec(32)
        .addTo(mx)

    // endregion
  }
}
