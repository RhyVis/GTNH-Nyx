package vis.rhynia.nova.common.recipe.nova

import goodgenerator.items.GGMaterial
import gregtech.api.enums.HeatingCoilLevel
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsAlloy
import gtPlusPlus.core.material.MaterialsElements
import vis.rhynia.nova.Config
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_MAX
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UEV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UIV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UMV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UXV
import vis.rhynia.nova.api.enums.ref.SuperConductorPart
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.api.util.FluidUtil
import vis.rhynia.nova.api.util.ItemUtil
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterial
import vis.rhynia.nova.common.recipe.RecipePool

class TranscendentReactorRecipePool : RecipePool() {
  private val tr = NovaRecipeMaps.transcendentReactorRecipes
  private val recipeModifier: Int = Config.Recipe_TR_RecipeModifier
  private val outputModifier: Int = Config.Recipe_TR_OutputModifier
  private val catalystACA: Int = Config.Recipe_TR_CatalystACAModifier

  override fun loadRecipes() {
    loadCatalyst()
    loadMisc()
  }

  private fun loadCatalyst() {
    val ingotModifier = recipeModifier.toLong() * INGOTS

    // region 催化剂
    // DTCC
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(1),
            ItemUtil.setStackSize(Materials.Iron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Calcium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Niobium.getDust(1), recipeModifier))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(catalystACA / 4),
            Materials.Helium.getGas(ingotModifier))
        .fluidOutputs(
            MaterialsUEVplus.ExcitedDTCC.getFluid((recipeModifier * outputModifier).toLong()),
            NovaMaterial.AstralResidue.getFluidOrGas(catalystACA / 8))
        .specialValue(HeatingCoilLevel.UEV.getHeat().toInt())
        .noOptimize()
        .eut(RECIPE_UEV)
        .durSec(10)
        .addTo(tr)

    // DTPC
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(2),
            ItemUtil.setStackSize(Materials.Iron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Calcium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Niobium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Nickel.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Boron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Sulfur.getDust(1), recipeModifier))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(catalystACA / 2),
            Materials.Helium.getGas(ingotModifier),
            Materials.Radon.getGas(ingotModifier))
        .fluidOutputs(
            MaterialsUEVplus.ExcitedDTPC.getFluid((recipeModifier * outputModifier).toLong()),
            NovaMaterial.AstralResidue.getFluidOrGas(catalystACA / 4))
        .specialValue(HeatingCoilLevel.UIV.heat.toInt())
        .noOptimize()
        .eut(RECIPE_UIV)
        .durSec(10)
        .addTo(tr)

    // DTRC
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(3),
            ItemUtil.setStackSize(Materials.Iron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Calcium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Niobium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Nickel.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Boron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Sulfur.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Zinc.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Silver.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Titanium.getDust(1), recipeModifier))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(catalystACA),
            Materials.Helium.getGas(ingotModifier),
            Materials.Radon.getGas(ingotModifier),
            Materials.Nitrogen.getGas(ingotModifier))
        .fluidOutputs(
            MaterialsUEVplus.ExcitedDTRC.getFluid((recipeModifier * outputModifier).toLong()),
            NovaMaterial.AstralResidue.getFluidOrGas(catalystACA / 2))
        .specialValue(HeatingCoilLevel.UMV.heat.toInt())
        .noOptimize()
        .eut(RECIPE_UMV)
        .durSec(10)
        .addTo(tr)

    // DTEC
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(4),
            ItemUtil.setStackSize(Materials.Iron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Calcium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Niobium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Nickel.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Boron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Sulfur.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Zinc.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Silver.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Titanium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Americium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Bismuth.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Tin.getDust(1), recipeModifier))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(catalystACA * 2),
            Materials.Helium.getGas(ingotModifier),
            Materials.Radon.getGas(ingotModifier),
            Materials.Nitrogen.getGas(ingotModifier),
            Materials.Oxygen.getGas(ingotModifier))
        .fluidOutputs(
            MaterialsUEVplus.ExcitedDTEC.getFluid((recipeModifier * outputModifier).toLong()),
            NovaMaterial.AstralResidue.getFluidOrGas(catalystACA))
        .specialValue(HeatingCoilLevel.UXV.getHeat().toInt())
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(10)
        .addTo(tr)

    // DTSC
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(5),
            ItemUtil.setStackSize(Materials.Iron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Calcium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Niobium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Nickel.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Boron.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Sulfur.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Zinc.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Silver.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Titanium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Americium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Bismuth.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Tin.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Lead.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Thorium.getDust(1), recipeModifier),
            ItemUtil.setStackSize(Materials.Plutonium241.getDust(1), recipeModifier))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(catalystACA * 4),
            Materials.Helium.getGas(ingotModifier),
            Materials.Radon.getGas(ingotModifier),
            Materials.Nitrogen.getGas(ingotModifier),
            Materials.Oxygen.getGas(ingotModifier),
            MaterialsUEVplus.RawStarMatter.getFluid(recipeModifier * 100L))
        .fluidOutputs(
            MaterialsUEVplus.ExcitedDTSC.getFluid((recipeModifier * outputModifier).toLong()),
            NovaMaterial.AstralResidue.getFluidOrGas(catalystACA * 2))
        .specialValue(HeatingCoilLevel.MAX.heat.toInt())
        .noOptimize()
        .eut(RECIPE_MAX)
        .durSec(10)
        .addTo(tr)
  }

  private fun loadMisc() {

    // region SC
    // UV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), NovaItemList.AstriumInfinityGem.get(4))
        .fluidInputs(
            Materials.Samarium.getIngotMolten(1816),
            Materials.Europium.getIngotMolten(1816),
            Materials.Osmiridium.getIngotMolten(5448),
            Materials.Naquadria.getIngotMolten(7264))
        .fluidOutputs(
            SuperConductorPart.UV.getMolten(16344 * INGOTS),
            NovaMaterial.AstralResidue.getFluidOrGas(4 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(4)
        .addTo(tr)

    // UHV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), NovaItemList.AstriumInfinityGem.get(8))
        .fluidInputs(
            Materials.Draconium.getIngotMolten(2016),
            Materials.Americium.getIngotMolten(2016),
            Materials.CosmicNeutronium.getIngotMolten(2352),
            Materials.Tritanium.getIngotMolten(1680))
        .fluidOutputs(
            SuperConductorPart.UHV.getMolten(8064 * INGOTS),
            NovaMaterial.AstralResidue.getFluidOrGas(8 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(8)
        .addTo(tr)

    // UEV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), NovaItemList.AstriumInfinityGem.get(16))
        .fluidInputs(
            MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(168 * INGOTS),
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(168 * INGOTS),
            Materials.DraconiumAwakened.getIngotMolten(840),
            Materials.Infinity.getIngotMolten(840),
            Materials.Iron.getIngotMolten(168))
        .fluidOutputs(
            SuperConductorPart.UEV.getMolten(2016 * INGOTS),
            NovaMaterial.AstralResidue.getFluidOrGas(16 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(16)
        .addTo(tr)

    // UIV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), NovaItemList.AstriumInfinityGem.get(32))
        .fluidInputs(
            FluidUtil.getFluidStack("molten.radoxpoly", 160 * INGOTS),
            MaterialsUEVplus.TranscendentMetal.getIngotMolten(400),
            MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(240 * INGOTS),
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(200 * INGOTS),
            Materials.Bismuth.getIngotMolten(40))
        .fluidOutputs(
            SuperConductorPart.UIV.getMolten(1000 * INGOTS),
            NovaMaterial.AstralResidue.getFluidOrGas(32 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(32)
        .addTo(tr)

    // UMV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), NovaItemList.AstriumInfinityGem.get(64))
        .fluidInputs(
            MaterialsUEVplus.SpaceTime.getIngotMolten(216),
            GGMaterial.orundum.getMolten(108 * INGOTS),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(396 * INGOTS),
            MaterialsAlloy.TITANSTEEL.getFluidStack(180 * INGOTS),
            FluidUtil.getFluidStack("molten.dragonblood", 72 * INGOTS),
            Materials.Oxygen.getGas(36 * INGOTS.toLong()))
        .fluidOutputs(
            SuperConductorPart.UMV.getMolten(972 * INGOTS),
            NovaMaterial.AstralResidue.getFluidOrGas(64 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(64)
        .addTo(tr)
    // endregion

    // Sx ONE-STEP
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(10),
            NovaMaterial.AstralCatalystBase.getDust(12),
            Materials.TengamPurified.getDust(11),
            Materials.Infinity.getDust(10),
            Materials.CosmicNeutronium.getDust(8),
            Materials.NaquadahEnriched.getDust(6),
            Materials.Naquadria.getDust(5),
            GGMaterial.orundum.getDust(5),
            MaterialsElements.STANDALONE.ADVANCED_NITINOL.getDust(4),
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(4))
        .itemOutputs(NovaMaterial.AstriumMagic.getDust(16))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(48 * BUCKETS),
            Materials.Helium.getPlasma(6 * BUCKETS.toLong()),
            Materials.DraconiumAwakened.getMolten(12 * INGOTS.toLong()),
            Materials.Indium.getMolten(7 * INGOTS.toLong()),
        )
        .fluidOutputs(
            NovaMaterial.SuperconductorFlux.getFluidOrGas(72 * INGOTS),
            NovaMaterial.AstralResidue.getFluidOrGas(24 * BUCKETS))
        .specialValue(HeatingCoilLevel.UEV.getHeat().toInt())
        .noOptimize()
        .eut(RECIPE_UIV)
        .durSec(20)
        .addTo(tr)

    // SpaceTime Extract
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(10),
            NovaItemList.AstriumInfinityGem.get(16),
            ItemList.EnergisedTesseract.get(1))
        .itemOutputs(
            NovaMaterial.Astrium.getDust(64),
            NovaMaterial.AstriumMagic.getDust(64),
            NovaMaterial.AstriumInfinity.getDust(64),
            NovaMaterial.Astrium.getDust(64),
            NovaMaterial.AstriumMagic.getDust(64),
            NovaMaterial.AstriumInfinity.getDust(64))
        .outputChances(5000, 5000, 5000, 2500, 2500, 2500)
        .fluidInputs(MaterialsUEVplus.SpaceTime.getIngotMolten(512))
        .fluidOutputs(
            MaterialsUEVplus.Space.getIngotMolten(512),
            MaterialsUEVplus.Time.getIngotMolten(512),
            NovaMaterial.AstralResidue.getFluidOrGas(10 * BUCKETS),
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(10))
        .specialValue(HeatingCoilLevel.UMV.getHeat().toInt())
        .noOptimize()
        .eut(RECIPE_UMV)
        .durSec(20)
        .addTo(tr)

    // SpaceTime Transform
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(11),
            NovaItemList.LensAstriumInfinity.get(0),
            NovaItemList.AstriumInfinityGem.get(4))
        .itemOutputs(
            GTUtility.copyAmountUnsafe(
                256, GTModHandler.getModItem(Mods.GTPlusPlus.ID, "MU-metaitem.01", 1, 32105)))
        .fluidInputs(
            MaterialsUEVplus.SpaceTime.getIngotMolten(16),
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(4 * BUCKETS))
        .fluidOutputs(
            NovaMaterial.AstralResidue.getFluidOrGas(10 * BUCKETS),
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(16))
        .specialValue(HeatingCoilLevel.UIV.getHeat().toInt())
        .noOptimize()
        .eut(RECIPE_UMV)
        .durSec(16)
        .addTo(tr)
  }
}
