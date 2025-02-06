package vis.rhynia.nova.common.recipe.gt

import gregtech.api.enums.Materials
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gtPlusPlus.core.material.MaterialMisc
import gtPlusPlus.core.material.MaterialsElements
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_MV
import vis.rhynia.nova.common.material.NovaMaterials
import vis.rhynia.nova.common.recipe.RecipePool

class ElectrolyzeRecipePool : RecipePool() {

  override fun loadRecipes() {
    val ec: IRecipeMap = RecipeMaps.electrolyzerRecipes

    // region 焙烧粉还原
    // 焙烧铁粉
    builder()
        .itemInputs(Materials.RoastedIron.getDust(4))
        .itemOutputs(Materials.Iron.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧铅粉
    builder()
        .itemInputs(Materials.RoastedLead.getDust(4))
        .itemOutputs(Materials.Lead.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧镍粉
    builder()
        .itemInputs(Materials.RoastedNickel.getDust(4))
        .itemOutputs(Materials.Nickel.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧锌粉
    builder()
        .itemInputs(Materials.RoastedZinc.getDust(4))
        .itemOutputs(Materials.Zinc.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧铜粉
    builder()
        .itemInputs(Materials.RoastedCopper.getDust(4))
        .itemOutputs(Materials.Copper.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧钴粉
    builder()
        .itemInputs(Materials.RoastedCobalt.getDust(4))
        .itemOutputs(Materials.Cobalt.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧锑粉
    builder()
        .itemInputs(Materials.RoastedAntimony.getDust(4))
        .itemOutputs(Materials.Antimony.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // 焙烧砷粉
    builder()
        .itemInputs(Materials.RoastedArsenic.getDust(4))
        .itemOutputs(Materials.Arsenic.getDust(4), NovaMaterials.Astrium.getDust(1))
        .outputChances(10000, 5000)
        .eut(RECIPE_LV)
        .durSec(8)
        .addTo(ec)

    // endregion

    // region 电解钨-钛系列
    // 钛铁矿
    builder()
        .itemInputs(Materials.Ilmenite.getDust(8))
        .itemOutputs(Materials.Titanium.getDust(5), Materials.Iron.getDust(4))
        .fluidOutputs(Materials.Oxygen.getGas(6000))
        .eut(RECIPE_MV)
        .durSec(8)
        .addTo(ec)

    // endregion

    // region 电解杂项矿
    // 金红石 Rutile
    builder()
        .itemInputs(Materials.Rutile.getDust(3))
        .itemOutputs(Materials.Titanium.getDust(1))
        .fluidOutputs(Materials.Oxygen.getGas(1000))
        .eut(RECIPE_MV)
        .durSec(4)
        .addTo(ec)

    // 二氧化锶
    builder()
        .itemInputs(MaterialMisc.STRONTIUM_OXIDE.getDust(6))
        .itemOutputs(Materials.Strontium.getDust(4))
        .fluidOutputs(Materials.Oxygen.getGas(4500))
        .eut(RECIPE_MV)
        .durSec(8)
        .addTo(ec)

    // 黑物质
    builder()
        .itemInputs(MaterialsElements.STANDALONE.BLACK_METAL.getDust(16))
        .itemOutputs(
            Materials.Lead.getDust(3),
            Materials.Manganese.getDust(5),
            MaterialsElements.getInstance().RHENIUM.getDust(4),
            MaterialsElements.getInstance().THALLIUM.getDust(4),
            Materials.Copper.getDust(3),
            Materials.Molybdenum.getDust(1))
        .fluidOutputs(Materials.Oxygen.getGas(200), Materials.Hydrogen.getGas(150))
        .eut(RECIPE_MV)
        .durSec(28)
        .addTo(ec)

    // endregion
  }
}
