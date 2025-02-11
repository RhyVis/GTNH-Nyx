package rhynia.nyx.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.util.FluidUtil
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class LaserEngraverRecipePool : RecipePool() {
  override fun loadRecipes() {
    val laser: IRecipeMap = RecipeMaps.laserEngraverRecipes

    // region 光刻
    // 异氙
    builder()
        .itemInputs(NyxMaterials.AstriumMagic.getDust(1), NyxItemList.LensAstriumMagic.get(0))
        .fluidInputs(Materials.UUMatter.getFluid(16), WerkstoffLoader.Xenon.getFluidOrGas(1000))
        .fluidOutputs(FluidUtil.getFluidStack("xenoxene", 500))
        .eut(NyxValues.RecipeValues.RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 活性晶体
    builder()
        .itemInputs(
            Mods.GregTech.getItem("gt.metaitem.03", 1, 32071),
            NyxItemList.LensAstriumMagic.get(0),
        )
        .itemOutputs(
            Mods.GregTech.getItem("gt.metaitem.01", 4, 32668),
        )
        .fluidInputs(NyxMaterials.Astrium.getMolten(50))
        .eut(NyxValues.RecipeValues.RECIPE_UHV)
        .durSec(45)
        .addTo(laser)

    // 皮米晶圆
    builder()
        .itemInputs(getCoreItem("RawPicoWafer"), NyxItemList.LensAstriumInfinity.get(0))
        .itemOutputs(getCoreItem("PicoWafer", 4))
        .fluidInputs(
            NyxMaterials.AstralCatalystBaseExcited.getLiquid(100),
            Materials.Neutronium.getIngotMolten(2))
        .fluidOutputs(NyxMaterials.AstralResidue.getLiquid(50))
        .eut(NyxValues.RecipeValues.RECIPE_UEV)
        .durSec(15)
        .addTo(laser)

    // 生物活性晶圆
    builder()
        .itemInputs(ItemList.Circuit_Silicon_Wafer6.get(1), NyxItemList.LensAstriumMagic.get(0))
        .itemOutputs(ItemList.Circuit_Wafer_Bioware.get(4))
        .fluidInputs(
            Materials.BioMediumSterilized.getFluid(4000), NyxMaterials.Astrium.getIngotMolten(10))
        .fluidOutputs(Materials.UUMatter.getFluid(500))
        .eut(NyxValues.RecipeValues.RECIPE_UHV)
        .durSec(40)
        .addTo(laser)

    // 光子强化晶圆
    builder()
        .itemInputs(
            ItemList.Circuit_Silicon_Wafer6.get(1),
            Materials.Glowstone.getNanite(1),
            NyxItemList.LensAstriumMagic.get(0),
            Mods.SuperSolarPanels.getItem("solarsplitter", 0))
        .itemOutputs(ItemList.Circuit_Silicon_Wafer7.get(4L))
        .fluidInputs(
            WerkstoffLoader.Oganesson.getFluidOrGas(2500),
            NyxMaterials.AstralCatalystBaseExcited.getLiquid(800))
        .fluidOutputs(
            NyxMaterials.AstralResidue.getLiquid(400), NyxMaterials.Astrium.getMolten(500))
        .eut(NyxValues.RecipeValues.RECIPE_UEV)
        .durSec(20)
        .addTo(laser)

    // 增殖星辉
    builder()
        .itemInputs(NyxMaterials.Astrium.getDust(64), NyxItemList.LensAstriumInfinity.get(0))
        .fluidOutputs(NyxMaterials.Astrium.getBucketMolten(96))
        .eut(NyxValues.RecipeValues.RECIPE_UV)
        .durSec(60)
        .addTo(laser)

    // 激活催化剂
    builder()
        .itemInputs(NyxItemList.LensAstriumInfinity.get(0))
        .fluidInputs(NyxMaterials.AstralCatalystBase.getLiquid(1000))
        .fluidOutputs(NyxMaterials.AstralCatalystBaseExcited.getLiquid(1000))
        .eut(NyxValues.RecipeValues.RECIPE_ZPM)
        .durSec(45)
        .addTo(laser)

    // endregion

    // endregion

    // region 矩阵
    // 兰波顿矩阵
    val crystalChipMaster = ItemList.Circuit_Parts_Crystal_Chip_Master.get(64)
    builder()
        .itemInputs(NyxItemList.LapotronMatrix.get(1), NyxItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.EnergeticAlloy.getMolten(1440))
        .itemOutputs(*Array(4) { crystalChipMaster })
        .eut(NyxValues.RecipeValues.RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 晶体矩阵-绿
    val crystalCPU = ItemList.Circuit_Chip_CrystalCPU.get(64)
    builder()
        .itemInputs(NyxItemList.CrystalMatrix.get(1), NyxItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Europium.getIngotMolten(16))
        .itemOutputs(*Array(4) { crystalCPU })
        .eut(NyxValues.RecipeValues.RECIPE_UV)
        .durSec(25)
        .addTo(laser)

    // 晶体矩阵-红
    builder()
        .itemInputs(NyxItemList.CrystalMatrix.get(1), NyxItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Americium.getIngotMolten(16))
        .itemOutputs(
            ItemList.Circuit_Chip_CrystalSoC.get(64), ItemList.Circuit_Chip_CrystalSoC.get(32))
        .eut(NyxValues.RecipeValues.RECIPE_UHV)
        .durSec(15)
        .addTo(laser)
    // endregion
  }
}
