package vis.rhynia.nova.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.Materials
import gregtech.api.recipe.RecipeMaps
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.material.MaterialMisc
import gtPlusPlus.core.material.MaterialsElements
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_HV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_MV
import vis.rhynia.nova.common.material.NovaMaterial
import vis.rhynia.nova.common.recipe.RecipePool

class CentrifugeRecipePool : RecipePool() {
  private val cf = RecipeMaps.centrifugeRecipes
  private val cfNoCell = GTPPRecipeMaps.centrifugeNonCellRecipes

  override fun loadRecipes() {
    // region 杂项系列
    // 褐煤制煤
    builder()
        .itemInputs(Materials.Lignite.getDust(4))
        .itemOutputs(Materials.Coal.getDust(3))
        .eut(RECIPE_LV)
        .durSec(4)
        .addTo(cf)

    // 氟碳铈
    builder()
        .itemInputs(Materials.Bastnasite.getDust(12))
        .itemOutputs(
            Materials.Cerium.getDust(2),
            Materials.Gadolinium.getDust(1),
            Materials.Samarium.getDust(1),
            Materials.Carbon.getDust(2))
        .fluidOutputs(Materials.Oxygen.getGas(6000), Materials.Fluorine.getGas(2000))
        .eut(RECIPE_HV)
        .durSec(8)
        .addTo(cfNoCell)

    // 褐铜
    builder()
        .itemInputs(Materials.Rubracium.getDust(12))
        .itemOutputs(
            Materials.Copper.getDust(8),
            Materials.Ledox.getDust(2),
            WerkstoffLoader.Roquesit.getDust(1),
            Materials.Firestone.getDust(1),
            NovaMaterial.Astrium.getDust(1))
        .outputChances(10000, 10000, 10000, 10000, 5000)
        .eut(RECIPE_HV)
        .durSec(8)
        .addTo(cf)

    // 山铜
    builder()
        .itemInputs(Materials.Orichalcum.getDust(12))
        .itemOutputs(Materials.Copper.getDust(10), NovaMaterial.Astrium.getDust(4))
        .outputChances(10000, 5000)
        .eut(RECIPE_HV)
        .durSec(4)
        .addTo(cf)

    // 离心秘银
    builder()
        .itemInputs(Materials.Mithril.getDust(6))
        .itemOutputs(
            Materials.Pyrotheum.getDust(1),
            WerkstoffLoader.PTMetallicPowder.getDust(2),
            NovaMaterial.Astrium.getDust(2))
        .outputChances(10000, 10000, 5000)
        .eut(RECIPE_MV)
        .durSec(16)
        .addTo(cf)

    // 离心深空秘银
    builder()
        .itemInputs(Materials.Mytryl.getDust(8))
        .itemOutputs(
            Materials.Mithril.getDust(4),
            Materials.Thaumium.getDust(3),
            Materials.AstralSilver.getDust(1),
            WerkstoffLoader.PTMetallicPowder.getDust(2),
            NovaMaterial.Astrium.getDust(2))
        .outputChances(10000, 10000, 10000, 10000, 5000)
        .eut(RECIPE_MV)
        .durSec(16)
        .addTo(cf)

    // 离心方钍石
    builder()
        .itemInputs(WerkstoffLoader.Thorianit.getDust(8))
        .itemOutputs(Materials.Thorium.getDust(6), WerkstoffLoader.Thorium232.getDust(2))
        .outputChances(10000, 2000)
        .eut(RECIPE_HV)
        .durSec(8)
        .addTo(cf)

    // 离心金刚砂
    builder()
        .itemInputs(Materials.Emery.getDust(18))
        .itemOutputs(Materials.Quartzite.getDust(10), Materials.Diamond.getDust(8))
        .outputChances(10000, 7500)
        .eut(RECIPE_HV)
        .durSec(8)
        .addTo(cf)

    // 离心幽冥毒晶
    builder()
        .itemInputs(Materials.Vyroxeres.getDust(7))
        .itemOutputs(
            Materials.Uranium235.getDust(3),
            MaterialsElements.getInstance().RHENIUM.getDust(2),
            MaterialsElements.getInstance().THALLIUM.getDust(2))
        .outputChances(10000, 5000, 5000)
        .eut(RECIPE_HV)
        .durSec(8)
        .addTo(cf)

    // 离心神秘蓝金
    builder()
        .itemInputs(Materials.Alduorite.getDust(16))
        .itemOutputs(
            Materials.ElectrumFlux.getDust(8),
            Materials.Thaumium.getDust(2),
            Materials.MysteriousCrystal.getDust(2),
            NovaMaterial.Astrium.getDust(4))
        .outputChances(10000, 10000, 6500, 5000)
        .eut(RECIPE_HV)
        .durSec(8)
        .addTo(cf)

    // 离心暗影秘银
    builder()
        .itemInputs(Materials.Ceruclase.getDust(11))
        .itemOutputs(
            Materials.Silver.getDust(3),
            Materials.ShadowIron.getDust(3),
            Materials.ElectrumFlux.getDust(2),
            NovaMaterial.Astrium.getDust(1))
        .outputChances(10000, 10000, 6500, 5000)
        .eut(RECIPE_HV)
        .durSec(4)
        .addTo(cf)

    // 离心胶木
    builder()
        .itemInputs(Materials.Vulcanite.getDust(16))
        .itemOutputs(Materials.RawRubber.getDust(8))
        .fluidOutputs(
            MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(16000),
            Materials.AdvancedGlue.getFluid(8000))
        .eut(RECIPE_HV)
        .durSec(16)
        .addTo(cfNoCell)

    // endregion

    // region 钨处理
    // 白钨
    builder()
        .itemInputs(Materials.Scheelite.getDust(4))
        .itemOutputs(Materials.Tungsten.getDust(3), Materials.Calcium.getDust(3))
        .fluidOutputs(Materials.Oxygen.getGas(2000))
        .noOptimize()
        .eut(RECIPE_MV)
        .durSec(8)
        .addTo(cf)

    // 钨酸锂
    builder()
        .itemInputs(Materials.Tungstate.getDust(6))
        .itemOutputs(Materials.Tungsten.getDust(3), Materials.Lithium.getDust(5))
        .fluidOutputs(Materials.Oxygen.getGas(1500))
        .noOptimize()
        .eut(RECIPE_MV)
        .durSec(8)
        .addTo(cf)

    // 钨铁
    builder()
        .itemInputs(WerkstoffLoader.Ferberite.getDust(6))
        .itemOutputs(Materials.Tungsten.getDust(3), Materials.Iron.getDust(2))
        .fluidOutputs(Materials.Oxygen.getGas(1500))
        .noOptimize()
        .eut(RECIPE_MV)
        .durSec(8)
        .addTo(cf)

    // 钨酸锰
    builder()
        .itemInputs(WerkstoffLoader.Huebnerit.getDust(8))
        .itemOutputs(Materials.Tungsten.getDust(3), Materials.Manganese.getDust(4))
        .fluidOutputs(Materials.Oxygen.getGas(2000))
        .noOptimize()
        .eut(RECIPE_MV)
        .durSec(8)
        .addTo(cf)
    // endregion
  }
}
