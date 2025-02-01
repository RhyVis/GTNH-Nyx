package vis.rhynia.nova.common.recipe.gt

import bartworks.API.recipe.BartWorksRecipeMaps
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsElements
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_MV
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterial

class CommonRecipePool : RecipePool {
  override fun loadRecipes() {
    val ems: IRecipeMap = RecipeMaps.electroMagneticSeparatorRecipes
    val imp: IRecipeMap = BartWorksRecipeMaps.electricImplosionCompressorRecipes

    // region 星辉
    // 磁析神秘
    builder()
        .itemInputs(Materials.Thaumium.getDust(8))
        .itemOutputs(Materials.Iron.getDust(6), NovaMaterial.Astrium.getDust(4))
        .eut(RECIPE_MV)
        .durSec(21)
        .addTo(ems)

    // 聚爆星辉
    builder()
        .itemInputs(NovaMaterial.AstriumInfinity.getDust(8))
        .fluidInputs(NovaMaterial.Astrium.getIngotMolten(2))
        .itemOutputs(NovaItemList.AstriumInfinityGem.get(1))
        .eut(6000000)
        .duration(1)
        .addTo(imp)
    builder()
        .itemInputs(
            MaterialsUEVplus.SpaceTime.getDust(64),
            MaterialsElements.STANDALONE.HYPOGEN.getDust(32))
        .fluidInputs(NovaMaterial.AstriumInfinity.getIngotMolten(48))
        .itemOutputs(GTUtility.copyAmountUnsafe(256, NovaItemList.AstriumInfinityGem.get(1)))
        .eut(24000000)
        .duration(1)
        .addTo(imp)

    // 星辉残留
    builder()
        .fluidInputs(NovaMaterial.AstralResidue.getFluidOrGas(32 * BUCKETS))
        .fluidOutputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * INGOTS))
        .eut(8000000)
        .duration(1)
        .addTo(imp)
    builder()
        .itemInputs(NovaItemList.PreTesseract.get(1))
        .fluidInputs(NovaMaterial.AstralResidue.getFluidOrGas(16 * BUCKETS))
        .fluidOutputs(MaterialsUEVplus.TranscendentMetal.getIngotMolten(4))
        .eut(32000000)
        .duration(1)
        .addTo(imp)
  }
}
