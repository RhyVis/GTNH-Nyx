package rhynia.constellation.common.recipe.cel

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
import rhynia.constellation.Config
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_MAX
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UEV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UIV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UMV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UXV
import rhynia.constellation.api.enums.ref.SuperConductorPart
import rhynia.constellation.api.recipe.CelRecipeMaps
import rhynia.constellation.api.util.FluidUtil
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class TranscendentReactorRecipePool : RecipePool() {
  private val tr = CelRecipeMaps.transcendentReactorRecipes
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
    altBuilder(tr) {
      itemInputs(
              GTUtility.getIntegratedCircuit(1),
              Materials.Iron.getDust(1).ofSize(recipeModifier),
              Materials.Calcium.getDust(1).ofSize(recipeModifier),
              Materials.Niobium.getDust(1).ofSize(recipeModifier))
          .fluidInputs(
              CelMaterials.AstralCatalystBaseExcited.getLiquid(catalystACA / 4),
              Materials.Helium.getGas(ingotModifier))
          .fluidOutputs(
              MaterialsUEVplus.ExcitedDTCC.getFluid((recipeModifier * outputModifier).toLong()),
              CelMaterials.AstralResidue.getLiquid(catalystACA / 8))
          .specialValue(HeatingCoilLevel.UEV.heat.toInt())
          .eut(RECIPE_UEV)
          .durSec(10)
    }

    // DTPC
    altBuilder(tr) {
      itemInputs(
              GTUtility.getIntegratedCircuit(2),
              Materials.Iron.getDust(1).ofSize(recipeModifier),
              Materials.Calcium.getDust(1).ofSize(recipeModifier),
              Materials.Niobium.getDust(1).ofSize(recipeModifier),
              Materials.Nickel.getDust(1).ofSize(recipeModifier),
              Materials.Boron.getDust(1).ofSize(recipeModifier),
              Materials.Sulfur.getDust(1).ofSize(recipeModifier))
          .fluidInputs(
              CelMaterials.AstralCatalystBaseExcited.getLiquid(catalystACA / 2),
              Materials.Helium.getGas(ingotModifier),
              Materials.Radon.getGas(ingotModifier))
          .fluidOutputs(
              MaterialsUEVplus.ExcitedDTPC.getFluid((recipeModifier * outputModifier).toLong()),
              CelMaterials.AstralResidue.getLiquid(catalystACA / 4))
          .specialValue(HeatingCoilLevel.UIV.heat.toInt())
          .eut(RECIPE_UIV)
          .durSec(10)
    }

    // DTRC
    altBuilder(tr) {
      itemInputs(
              GTUtility.getIntegratedCircuit(3),
              Materials.Iron.getDust(1).ofSize(recipeModifier),
              Materials.Calcium.getDust(1).ofSize(recipeModifier),
              Materials.Niobium.getDust(1).ofSize(recipeModifier),
              Materials.Nickel.getDust(1).ofSize(recipeModifier),
              Materials.Boron.getDust(1).ofSize(recipeModifier),
              Materials.Sulfur.getDust(1).ofSize(recipeModifier),
              Materials.Zinc.getDust(1).ofSize(recipeModifier),
              Materials.Silver.getDust(1).ofSize(recipeModifier),
              Materials.Titanium.getDust(1).ofSize(recipeModifier))
          .fluidInputs(
              CelMaterials.AstralCatalystBaseExcited.getLiquid(catalystACA),
              Materials.Helium.getGas(ingotModifier),
              Materials.Radon.getGas(ingotModifier),
              Materials.Nitrogen.getGas(ingotModifier))
          .fluidOutputs(
              MaterialsUEVplus.ExcitedDTRC.getFluid((recipeModifier * outputModifier).toLong()),
              CelMaterials.AstralResidue.getLiquid(catalystACA / 2))
          .specialValue(HeatingCoilLevel.UMV.heat.toInt())
          .eut(RECIPE_UMV)
          .durSec(10)
    }

    // DTEC
    altBuilder(tr) {
      itemInputs(
              GTUtility.getIntegratedCircuit(4),
              Materials.Iron.getDust(1).ofSize(recipeModifier),
              Materials.Calcium.getDust(1).ofSize(recipeModifier),
              Materials.Niobium.getDust(1).ofSize(recipeModifier),
              Materials.Nickel.getDust(1).ofSize(recipeModifier),
              Materials.Boron.getDust(1).ofSize(recipeModifier),
              Materials.Sulfur.getDust(1).ofSize(recipeModifier),
              Materials.Zinc.getDust(1).ofSize(recipeModifier),
              Materials.Silver.getDust(1).ofSize(recipeModifier),
              Materials.Titanium.getDust(1).ofSize(recipeModifier),
              Materials.Americium.getDust(1).ofSize(recipeModifier),
              Materials.Bismuth.getDust(1).ofSize(recipeModifier),
              Materials.Tin.getDust(1).ofSize(recipeModifier))
          .fluidInputs(
              CelMaterials.AstralCatalystBaseExcited.getLiquid(catalystACA * 2),
              Materials.Helium.getGas(ingotModifier),
              Materials.Radon.getGas(ingotModifier),
              Materials.Nitrogen.getGas(ingotModifier),
              Materials.Oxygen.getGas(ingotModifier))
          .fluidOutputs(
              MaterialsUEVplus.ExcitedDTEC.getFluid((recipeModifier * outputModifier).toLong()),
              CelMaterials.AstralResidue.getLiquid(catalystACA))
          .specialValue(HeatingCoilLevel.UXV.heat.toInt())
          .eut(RECIPE_UXV)
          .durSec(10)
    }

    // DTSC
    altBuilder(tr) {
      itemInputs(
              GTUtility.getIntegratedCircuit(5),
              Materials.Iron.getDust(1).ofSize(recipeModifier),
              Materials.Calcium.getDust(1).ofSize(recipeModifier),
              Materials.Niobium.getDust(1).ofSize(recipeModifier),
              Materials.Nickel.getDust(1).ofSize(recipeModifier),
              Materials.Boron.getDust(1).ofSize(recipeModifier),
              Materials.Sulfur.getDust(1).ofSize(recipeModifier),
              Materials.Zinc.getDust(1).ofSize(recipeModifier),
              Materials.Silver.getDust(1).ofSize(recipeModifier),
              Materials.Titanium.getDust(1).ofSize(recipeModifier),
              Materials.Americium.getDust(1).ofSize(recipeModifier),
              Materials.Bismuth.getDust(1).ofSize(recipeModifier),
              Materials.Tin.getDust(1).ofSize(recipeModifier),
              Materials.Lead.getDust(1).ofSize(recipeModifier),
              Materials.Thorium.getDust(1).ofSize(recipeModifier),
              Materials.Plutonium241.getDust(1).ofSize(recipeModifier))
          .fluidInputs(
              CelMaterials.AstralCatalystBaseExcited.getLiquid(catalystACA * 4),
              Materials.Helium.getGas(ingotModifier),
              Materials.Radon.getGas(ingotModifier),
              Materials.Nitrogen.getGas(ingotModifier),
              Materials.Oxygen.getGas(ingotModifier),
              MaterialsUEVplus.RawStarMatter.getFluid(recipeModifier * 100L))
          .fluidOutputs(
              MaterialsUEVplus.ExcitedDTSC.getFluid((recipeModifier * outputModifier).toLong()),
              CelMaterials.AstralResidue.getLiquid(catalystACA * 2))
          .specialValue(HeatingCoilLevel.MAX.heat.toInt())
          .eut(RECIPE_MAX)
          .durSec(10)
    }
  }

  private fun loadMisc() {

    // region SC
    // UV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), CelItemList.AstriumInfinityGem.get(4))
        .fluidInputs(
            Materials.Samarium.getIngotMolten(1816),
            Materials.Europium.getIngotMolten(1816),
            Materials.Osmiridium.getIngotMolten(5448),
            Materials.Naquadria.getIngotMolten(7264))
        .fluidOutputs(
            SuperConductorPart.UV.getMolten(16344 * INGOTS),
            CelMaterials.AstralResidue.getLiquid(4 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(4)
        .addTo(tr)

    // UHV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), CelItemList.AstriumInfinityGem.get(8))
        .fluidInputs(
            Materials.Draconium.getIngotMolten(2016),
            Materials.Americium.getIngotMolten(2016),
            Materials.CosmicNeutronium.getIngotMolten(2352),
            Materials.Tritanium.getIngotMolten(1680))
        .fluidOutputs(
            SuperConductorPart.UHV.getMolten(8064 * INGOTS),
            CelMaterials.AstralResidue.getLiquid(8 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(8)
        .addTo(tr)

    // UEV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), CelItemList.AstriumInfinityGem.get(16))
        .fluidInputs(
            MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(168 * INGOTS),
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(168 * INGOTS),
            Materials.DraconiumAwakened.getIngotMolten(840),
            Materials.Infinity.getIngotMolten(840),
            Materials.Iron.getIngotMolten(168))
        .fluidOutputs(
            SuperConductorPart.UEV.getMolten(2016 * INGOTS),
            CelMaterials.AstralResidue.getLiquid(16 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(16)
        .addTo(tr)

    // UIV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), CelItemList.AstriumInfinityGem.get(32))
        .fluidInputs(
            FluidUtil.getFluidStack("molten.radoxpoly", 160 * INGOTS),
            MaterialsUEVplus.TranscendentMetal.getIngotMolten(400),
            MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(240 * INGOTS),
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(200 * INGOTS),
            Materials.Bismuth.getIngotMolten(40))
        .fluidOutputs(
            SuperConductorPart.UIV.getMolten(1000 * INGOTS),
            CelMaterials.AstralResidue.getLiquid(32 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(32)
        .addTo(tr)

    // UMV
    builder()
        .itemInputs(GTUtility.getIntegratedCircuit(10), CelItemList.AstriumInfinityGem.get(64))
        .fluidInputs(
            MaterialsUEVplus.SpaceTime.getIngotMolten(216),
            GGMaterial.orundum.getMolten(108 * INGOTS),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(396 * INGOTS),
            MaterialsAlloy.TITANSTEEL.getFluidStack(180 * INGOTS),
            FluidUtil.getFluidStack("molten.dragonblood", 72 * INGOTS),
            Materials.Oxygen.getGas(36 * INGOTS.toLong()))
        .fluidOutputs(
            SuperConductorPart.UMV.getMolten(972 * INGOTS),
            CelMaterials.AstralResidue.getLiquid(64 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UXV)
        .durSec(64)
        .addTo(tr)
    // endregion

    // Sx ONE-STEP
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(10),
            CelMaterials.AstralCatalystBase.getDust(12),
            Materials.TengamPurified.getDust(11),
            Materials.Infinity.getDust(10),
            Materials.CosmicNeutronium.getDust(8),
            Materials.NaquadahEnriched.getDust(6),
            Materials.Naquadria.getDust(5),
            GGMaterial.orundum.getDust(5),
            MaterialsElements.STANDALONE.ADVANCED_NITINOL.getDust(4),
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(4))
        .itemOutputs(CelMaterials.AstriumMagic.getDust(16))
        .fluidInputs(
            CelMaterials.AstralCatalystBaseExcited.getLiquid(48 * BUCKETS),
            Materials.Helium.getPlasma(6 * BUCKETS.toLong()),
            Materials.DraconiumAwakened.getMolten(12 * INGOTS.toLong()),
            Materials.Indium.getMolten(7 * INGOTS.toLong()),
        )
        .fluidOutputs(
            CelMaterials.SuperconductorFlux.getLiquid(72 * INGOTS),
            CelMaterials.AstralResidue.getLiquid(24 * BUCKETS))
        .specialValue(HeatingCoilLevel.UEV.heat.toInt())
        .noOptimize()
        .eut(RECIPE_UIV)
        .durSec(20)
        .addTo(tr)

    // SpaceTime Extract
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(10),
            CelItemList.AstriumInfinityGem.get(16),
            ItemList.EnergisedTesseract.get(1))
        .itemOutputs(
            CelMaterials.Astrium.getDust(64),
            CelMaterials.AstriumMagic.getDust(64),
            CelMaterials.AstriumInfinity.getDust(64),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.AstriumMagic.getDust(64),
            CelMaterials.AstriumInfinity.getDust(64))
        .outputChances(5000, 5000, 5000, 2500, 2500, 2500)
        .fluidInputs(MaterialsUEVplus.SpaceTime.getIngotMolten(512))
        .fluidOutputs(
            MaterialsUEVplus.Space.getIngotMolten(512),
            MaterialsUEVplus.Time.getIngotMolten(512),
            CelMaterials.AstralResidue.getLiquid(10 * BUCKETS),
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(10))
        .specialValue(HeatingCoilLevel.UMV.heat.toInt())
        .noOptimize()
        .eut(RECIPE_UMV)
        .durSec(20)
        .addTo(tr)

    // SpaceTime Transform
    builder()
        .itemInputs(
            GTUtility.getIntegratedCircuit(11),
            CelItemList.LensAstriumInfinity.get(0),
            CelItemList.AstriumInfinityGem.get(4))
        .itemOutputs(
            GTUtility.copyAmountUnsafe(
                256, GTModHandler.getModItem(Mods.GTPlusPlus.ID, "MU-metaitem.01", 1, 32105)))
        .fluidInputs(
            MaterialsUEVplus.SpaceTime.getIngotMolten(16),
            CelMaterials.AstralCatalystBaseExcited.getLiquid(4 * BUCKETS))
        .fluidOutputs(
            CelMaterials.AstralResidue.getLiquid(10 * BUCKETS),
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(16))
        .specialValue(HeatingCoilLevel.UIV.heat.toInt())
        .noOptimize()
        .eut(RECIPE_UMV)
        .durSec(16)
        .addTo(tr)
  }
}
