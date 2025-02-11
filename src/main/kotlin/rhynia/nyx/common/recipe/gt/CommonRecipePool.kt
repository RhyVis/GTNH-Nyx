package rhynia.nyx.common.recipe.gt

import bartworks.API.recipe.BartWorksRecipeMaps
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsElements
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

class CommonRecipePool : RecipePool() {
  override fun loadRecipes() {
    val ems: IRecipeMap = RecipeMaps.electroMagneticSeparatorRecipes
    val imp: IRecipeMap = BartWorksRecipeMaps.electricImplosionCompressorRecipes

    // region 星辉
    // 磁析神秘
    builder()
        .itemInputs(Materials.Thaumium.getDust(8))
        .itemOutputs(Materials.Iron.getDust(6), NyxMaterials.Astrium.getDust(4))
        .eut(NyxValues.RecipeValues.RECIPE_MV)
        .durSec(21)
        .addTo(ems)

    // 聚爆星辉
    builder()
        .itemInputs(NyxMaterials.AstriumInfinity.getDust(8))
        .fluidInputs(NyxMaterials.Astrium.getIngotMolten(2))
        .itemOutputs(NyxItemList.AstriumInfinityGem.get(1))
        .eut(6000000)
        .duration(1)
        .addTo(imp)
    builder()
        .itemInputs(
            MaterialsUEVplus.SpaceTime.getDust(64),
            MaterialsElements.STANDALONE.HYPOGEN.getDust(32))
        .fluidInputs(NyxMaterials.AstriumInfinity.getIngotMolten(48))
        .itemOutputs(GTUtility.copyAmountUnsafe(256, NyxItemList.AstriumInfinityGem.get(1)))
        .eut(24000000)
        .duration(1)
        .addTo(imp)

    // 星辉残留
    builder()
        .fluidInputs(NyxMaterials.AstralResidue.getLiquid(32 * BUCKETS))
        .fluidOutputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * INGOTS))
        .eut(8000000)
        .duration(1)
        .addTo(imp)
    builder()
        .itemInputs(NyxItemList.PreTesseract.get(1))
        .fluidInputs(NyxMaterials.AstralResidue.getLiquid(16 * BUCKETS))
        .fluidOutputs(MaterialsUEVplus.TranscendentMetal.getIngotMolten(4))
        .eut(32000000)
        .duration(1)
        .addTo(imp)
  }
}
