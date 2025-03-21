package rhynia.nyx.common.recipe.gt

import goodgenerator.items.GGMaterial
import gregtech.api.enums.Materials
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.material.MaterialsElements
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UIV
import rhynia.nyx.api.util.FluidUtil
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class MixerRecipePool : RecipePool() {
    override fun loadRecipes() {
        val mx: IRecipeMap = RecipeMaps.mixerRecipes
        val mxNoCell: IRecipeMap = GTPPRecipeMaps.mixerNonCellRecipes

        // region 杂项
        // 深渊铁
        builder()
            .itemInputs(
                Materials.Iron.getDust(1),
                Materials.Thaumium.getDust(3),
                GTUtility.getIntegratedCircuit(2),
            ).itemOutputs(Materials.ShadowIron.getDust(3))
            .noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_HV)
            .durSec(21)
            .addTo(mx)

        // 星辉催化剂
        builder()
            .itemInputs(NyxMaterials.AstriumInfinity.getDust(8), Materials.InfinityCatalyst.getDust(17))
            .itemOutputs(NyxMaterials.AstralCatalystBase.getDust(50))
            .fluidInputs(
                Materials.Helium.getGas(12 * BUCKETS.toLong()),
                NyxMaterials.Astrium.getMolten(4 * INGOTS),
            ).noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_UHV)
            .durSec(12)
            .addTo(mxNoCell)

        // 超导通流
        builder()
            .itemInputs(
                NyxMaterials.AstralCatalystBase.getDust(12),
                Materials.TengamPurified.getDust(11),
                Materials.Infinity.getDust(10),
                Materials.CosmicNeutronium.getDust(8),
                Materials.NaquadahEnriched.getDust(6),
                Materials.Naquadria.getDust(5),
                GGMaterial.orundum.getDust(5),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getDust(4),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(4),
            ).itemOutputs(
                NyxMaterials.SuperconductorFluxRaw.getDust(64),
                NyxMaterials.SuperconductorFluxRaw.getDust(15),
            ).fluidInputs(
                Materials.Helium.getPlasma(6 * BUCKETS.toLong()),
                Materials.DraconiumAwakened.getIngotMolten(12),
                Materials.Indium.getIngotMolten(7),
            ).noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_UHV)
            .durSec(30)
            .addTo(mxNoCell)
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                Materials.Clay.getDust(4),
                Materials.Calcite.getDust(4),
                Materials.Quartzite.getDust(4),
            ).fluidInputs(Materials.Water.getBucketFluid(16))
            .fluidOutputs(FluidUtil.getFluidStack("wet.concrete", 18 * BUCKETS))
            .noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_MV)
            .durSec(5)
            .addTo(mx)

        // 太初物质（测试）
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(8), NyxItemList.PreTesseract.get(4))
            .fluidInputs(NyxMaterials.PrimordialEssence.getPlasma(1000))
            .fluidOutputs(NyxMaterials.PrimordialEssence.getIngotMolten(2))
            .eut(RECIPE_UIV)
            .durSec(16)
            .addTo(mx)

        // endregion

        // endregion

        // region 生物培养基
        // 培养基
        builder()
            .itemInputs(
                Materials.Calcium.getDust(3),
                NyxMaterials.Astrium.getDust(4),
                GTUtility.getIntegratedCircuit(15),
            ).fluidInputs(GTModHandler.getDistilledWater(4000))
            .fluidOutputs(Materials.GrowthMediumRaw.getFluid(8000))
            .noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_LuV)
            .durSec(15)
            .addTo(mx)

        // 生物培养基
        builder()
            .itemInputs(
                Materials.NetherStar.getDust(2),
                NyxMaterials.Astrium.getDust(6),
                GTUtility.getIntegratedCircuit(16),
            ).fluidInputs(GTModHandler.getDistilledWater(4000))
            .fluidOutputs(Materials.BioMediumRaw.getFluid(8000))
            .noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_ZPM)
            .durSec(20)
            .addTo(mx)

        // endregion

        // region 极寒-烈焰

        // 极寒
        builder()
            .itemInputs(
                Materials.CallistoIce.getDust(12),
                Materials.Ledox.getDust(12),
                NyxMaterials.Astrium.getDust(18),
                GTUtility.getIntegratedCircuit(15),
            ).itemOutputs(Materials.Cryotheum.getDust(128))
            .fluidInputs(GTModHandler.getDistilledWater(64 * BUCKETS.toLong()))
            .fluidOutputs(FluidUtil.getFluidStack("cryotheum", 256 * BUCKETS))
            .noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_IV)
            .durSec(32)
            .addTo(mx)

        // 烈焰
        builder()
            .itemInputs(
                Materials.Sulfur.getDust(12),
                Materials.Coal.getDust(12),
                NyxMaterials.Astrium.getDust(18),
                GTUtility.getIntegratedCircuit(16),
            ).itemOutputs(Materials.Pyrotheum.getDust(128))
            .fluidInputs(Materials.Lava.getBucketFluid(64))
            .fluidOutputs(FluidUtil.getFluidStack("pyrotheum", 256 * BUCKETS))
            .noOptimize()
            .eut(NyxValues.RecipeValues.RECIPE_IV)
            .durSec(32)
            .addTo(mx)

        // endregion
    }
}
