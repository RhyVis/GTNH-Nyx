package rhynia.nyx.common.recipe.gt

import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeConstants.COIL_HEAT
import gregtech.api.util.GTUtility
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.recipe.RecipePool

class PlasmaForgeRecipePool : RecipePool() {
  override fun loadRecipes() {
    val forge: IRecipeMap = RecipeMaps.plasmaForgeRecipes

    builder()
        .itemInputs(NyxItemList.PreTesseract.get(4))
        .itemOutputs(ItemList.Tesseract.get(4))
        .fluidInputs(MaterialsUEVplus.ExcitedDTRC.getFluid(1000))
        .fluidOutputs(
            MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(
                (NyxValues.RecipeValues.BUCKET / 2).toLong()))
        .eut(NyxValues.RecipeValues.RECIPE_UIV)
        .durSec(20)
        .metadata(COIL_HEAT, 10800 + 900)
        .addTo(forge)
    builder()
        .itemInputs(ItemList.Tesseract.get(0), NyxItemList.PreTesseract.get(16))
        .itemOutputs(ItemList.Tesseract.get(16))
        .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1000))
        .fluidOutputs(
            MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(
                (NyxValues.RecipeValues.BUCKET * 2).toLong()))
        .eut(NyxValues.RecipeValues.RECIPE_UXV)
        .durSec(20)
        .metadata(COIL_HEAT, 10800 + 2700)
        .addTo(forge)

    builder()
        .itemInputs(
            NyxItemList.PreTesseract.get(4),
            GTModHandler.getModItem(Mods.GTPlusPlus.ID, "MU-metaitem.01", 0, 32100))
        .fluidInputs(
            MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(20000),
            Materials.Infinity.getMolten((4 * 1152).toLong()))
        .fluidOutputs(MaterialsUEVplus.SpaceTime.getMolten((4 * 576).toLong()))
        .eut(2_000_000_000)
        .durSec(20)
        .metadata(COIL_HEAT, 10800 + 2700)
        .addTo(forge)
    builder()
        .itemInputs(
            GTUtility.copyAmountUnsafe(4096, NyxItemList.AstriumInfinityGem.get(1)),
            GTUtility.copyAmountUnsafe(2048, ItemList.Tesseract.get(1)),
            NyxItemList.LensAstriumInfinity.get(0))
        .fluidInputs(
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(512),
            MaterialsUEVplus.RawStarMatter.getBucketFluid(256),
            Materials.Infinity.getIngotMolten(512),
            MaterialsUEVplus.Space.getIngotMolten(1024),
            MaterialsUEVplus.Time.getIngotMolten(1024))
        .itemOutputs(NyxItemList.AstriumInfinityComplex.get(1))
        .eut(2_000_000_000)
        .durHour(24)
        .metadata(COIL_HEAT, 10800 + 2700)
        .addTo(forge)
  }
}
