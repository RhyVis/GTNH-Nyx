package rhynia.constellation.common.recipe.gt

import bartworks.API.recipe.BartWorksRecipeMaps
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsElements
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

class CommonRecipePool : RecipePool() {
  override fun loadRecipes() {
    val ems: IRecipeMap = RecipeMaps.electroMagneticSeparatorRecipes
    val imp: IRecipeMap = BartWorksRecipeMaps.electricImplosionCompressorRecipes

    // region 星辉
    // 磁析神秘
    builder()
        .itemInputs(Materials.Thaumium.getDust(8))
        .itemOutputs(Materials.Iron.getDust(6), CelMaterials.Astrium.getDust(4))
        .eut(CelValues.RecipeValues.RECIPE_MV)
        .durSec(21)
        .addTo(ems)

    // 聚爆星辉
    builder()
        .itemInputs(CelMaterials.AstriumInfinity.getDust(8))
        .fluidInputs(CelMaterials.Astrium.getIngotMolten(2))
        .itemOutputs(CelItemList.AstriumInfinityGem.get(1))
        .eut(6000000)
        .duration(1)
        .addTo(imp)
    builder()
        .itemInputs(
            MaterialsUEVplus.SpaceTime.getDust(64),
            MaterialsElements.STANDALONE.HYPOGEN.getDust(32))
        .fluidInputs(CelMaterials.AstriumInfinity.getIngotMolten(48))
        .itemOutputs(GTUtility.copyAmountUnsafe(256, CelItemList.AstriumInfinityGem.get(1)))
        .eut(24000000)
        .duration(1)
        .addTo(imp)

    // 星辉残留
    builder()
        .fluidInputs(CelMaterials.AstralResidue.getLiquid(32 * BUCKETS))
        .fluidOutputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * INGOTS))
        .eut(8000000)
        .duration(1)
        .addTo(imp)
    builder()
        .itemInputs(CelItemList.PreTesseract.get(1))
        .fluidInputs(CelMaterials.AstralResidue.getLiquid(16 * BUCKETS))
        .fluidOutputs(MaterialsUEVplus.TranscendentMetal.getIngotMolten(4))
        .eut(32000000)
        .duration(1)
        .addTo(imp)
  }
}
