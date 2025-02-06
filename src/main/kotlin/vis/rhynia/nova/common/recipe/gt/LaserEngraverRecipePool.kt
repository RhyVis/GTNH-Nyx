package vis.rhynia.nova.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UEV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UHV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_ZPM
import vis.rhynia.nova.api.util.FluidUtil
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterials
import vis.rhynia.nova.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class LaserEngraverRecipePool : RecipePool() {
  override fun loadRecipes() {
    val laser: IRecipeMap = RecipeMaps.laserEngraverRecipes

    // region 光刻
    // 异氙
    builder()
        .itemInputs(NovaMaterials.AstriumMagic.getDust(1), NovaItemList.LensAstriumMagic.get(0))
        .fluidInputs(Materials.UUMatter.getFluid(16), WerkstoffLoader.Xenon.getFluidOrGas(1000))
        .fluidOutputs(FluidUtil.getFluidStack("xenoxene", 500))
        .eut(RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 活性晶体
    builder()
        .itemInputs(
            Mods.GregTech.getItem("gt.metaitem.03", 1, 32071),
            NovaItemList.LensAstriumMagic.get(0),
        )
        .itemOutputs(
            Mods.GregTech.getItem("gt.metaitem.01", 4, 32668),
        )
        .fluidInputs(NovaMaterials.Astrium.getMolten(50))
        .eut(RECIPE_UHV)
        .durSec(45)
        .addTo(laser)

    // 皮米晶圆
    builder()
        .itemInputs(getCoreItem("RawPicoWafer"), NovaItemList.LensAstriumInfinity.get(0))
        .itemOutputs(getCoreItem("PicoWafer", 4))
        .fluidInputs(
            NovaMaterials.AstralCatalystBaseExcited.getLiquid(100),
            Materials.Neutronium.getIngotMolten(2))
        .fluidOutputs(NovaMaterials.AstralResidue.getLiquid(50))
        .eut(RECIPE_UEV)
        .durSec(15)
        .addTo(laser)

    // 生物活性晶圆
    builder()
        .itemInputs(ItemList.Circuit_Silicon_Wafer6.get(1), NovaItemList.LensAstriumMagic.get(0))
        .itemOutputs(ItemList.Circuit_Wafer_Bioware.get(4))
        .fluidInputs(
            Materials.BioMediumSterilized.getFluid(4000), NovaMaterials.Astrium.getIngotMolten(10))
        .fluidOutputs(Materials.UUMatter.getFluid(500))
        .eut(RECIPE_UHV)
        .durSec(40)
        .addTo(laser)

    // 光子强化晶圆
    builder()
        .itemInputs(
            ItemList.Circuit_Silicon_Wafer6.get(1),
            Materials.Glowstone.getNanite(1),
            NovaItemList.LensAstriumMagic.get(0),
            Mods.SuperSolarPanels.getItem("solarsplitter", 0))
        .itemOutputs(ItemList.Circuit_Silicon_Wafer7.get(4L))
        .fluidInputs(
            WerkstoffLoader.Oganesson.getFluidOrGas(2500),
            NovaMaterials.AstralCatalystBaseExcited.getLiquid(800))
        .fluidOutputs(
            NovaMaterials.AstralResidue.getLiquid(400), NovaMaterials.Astrium.getMolten(500))
        .eut(RECIPE_UEV)
        .durSec(20)
        .addTo(laser)

    // 增殖星辉
    builder()
        .itemInputs(NovaMaterials.Astrium.getDust(64), NovaItemList.LensAstriumInfinity.get(0))
        .fluidOutputs(NovaMaterials.Astrium.getBucketMolten(96))
        .eut(RECIPE_UV)
        .durSec(60)
        .addTo(laser)

    // 激活催化剂
    builder()
        .itemInputs(NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(NovaMaterials.AstralCatalystBase.getLiquid(1000))
        .fluidOutputs(NovaMaterials.AstralCatalystBaseExcited.getLiquid(1000))
        .eut(RECIPE_ZPM)
        .durSec(45)
        .addTo(laser)

    // endregion

    // endregion

    // region 矩阵
    // 兰波顿矩阵
    val crystalChipMaster = ItemList.Circuit_Parts_Crystal_Chip_Master.get(64)
    builder()
        .itemInputs(NovaItemList.LapotronMatrix.get(1), NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.EnergeticAlloy.getMolten(1440))
        .itemOutputs(*Array(4) { crystalChipMaster })
        .eut(RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 晶体矩阵-绿
    val crystalCPU = ItemList.Circuit_Chip_CrystalCPU.get(64)
    builder()
        .itemInputs(NovaItemList.CrystalMatrix.get(1), NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Europium.getIngotMolten(16))
        .itemOutputs(*Array(4) { crystalCPU })
        .eut(RECIPE_UV)
        .durSec(25)
        .addTo(laser)

    // 晶体矩阵-红
    builder()
        .itemInputs(NovaItemList.CrystalMatrix.get(1), NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Americium.getIngotMolten(16))
        .itemOutputs(
            ItemList.Circuit_Chip_CrystalSoC.get(64), ItemList.Circuit_Chip_CrystalSoC.get(32))
        .eut(RECIPE_UHV)
        .durSec(15)
        .addTo(laser)
    // endregion
  }
}
