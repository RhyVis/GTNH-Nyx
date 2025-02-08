package rhynia.constellation.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.api.util.FluidUtil
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class LaserEngraverRecipePool : RecipePool() {
  override fun loadRecipes() {
    val laser: IRecipeMap = RecipeMaps.laserEngraverRecipes

    // region 光刻
    // 异氙
    builder()
        .itemInputs(CelMaterials.AstriumMagic.getDust(1), CelItemList.LensAstriumMagic.get(0))
        .fluidInputs(Materials.UUMatter.getFluid(16), WerkstoffLoader.Xenon.getFluidOrGas(1000))
        .fluidOutputs(FluidUtil.getFluidStack("xenoxene", 500))
        .eut(CelValues.RecipeValues.RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 活性晶体
    builder()
        .itemInputs(
            Mods.GregTech.getItem("gt.metaitem.03", 1, 32071),
            CelItemList.LensAstriumMagic.get(0),
        )
        .itemOutputs(
            Mods.GregTech.getItem("gt.metaitem.01", 4, 32668),
        )
        .fluidInputs(CelMaterials.Astrium.getMolten(50))
        .eut(CelValues.RecipeValues.RECIPE_UHV)
        .durSec(45)
        .addTo(laser)

    // 皮米晶圆
    builder()
        .itemInputs(getCoreItem("RawPicoWafer"), CelItemList.LensAstriumInfinity.get(0))
        .itemOutputs(getCoreItem("PicoWafer", 4))
        .fluidInputs(
            CelMaterials.AstralCatalystBaseExcited.getLiquid(100),
            Materials.Neutronium.getIngotMolten(2))
        .fluidOutputs(CelMaterials.AstralResidue.getLiquid(50))
        .eut(CelValues.RecipeValues.RECIPE_UEV)
        .durSec(15)
        .addTo(laser)

    // 生物活性晶圆
    builder()
        .itemInputs(ItemList.Circuit_Silicon_Wafer6.get(1), CelItemList.LensAstriumMagic.get(0))
        .itemOutputs(ItemList.Circuit_Wafer_Bioware.get(4))
        .fluidInputs(
            Materials.BioMediumSterilized.getFluid(4000), CelMaterials.Astrium.getIngotMolten(10))
        .fluidOutputs(Materials.UUMatter.getFluid(500))
        .eut(CelValues.RecipeValues.RECIPE_UHV)
        .durSec(40)
        .addTo(laser)

    // 光子强化晶圆
    builder()
        .itemInputs(
            ItemList.Circuit_Silicon_Wafer6.get(1),
            Materials.Glowstone.getNanite(1),
            CelItemList.LensAstriumMagic.get(0),
            Mods.SuperSolarPanels.getItem("solarsplitter", 0))
        .itemOutputs(ItemList.Circuit_Silicon_Wafer7.get(4L))
        .fluidInputs(
            WerkstoffLoader.Oganesson.getFluidOrGas(2500),
            CelMaterials.AstralCatalystBaseExcited.getLiquid(800))
        .fluidOutputs(
            CelMaterials.AstralResidue.getLiquid(400), CelMaterials.Astrium.getMolten(500))
        .eut(CelValues.RecipeValues.RECIPE_UEV)
        .durSec(20)
        .addTo(laser)

    // 增殖星辉
    builder()
        .itemInputs(CelMaterials.Astrium.getDust(64), CelItemList.LensAstriumInfinity.get(0))
        .fluidOutputs(CelMaterials.Astrium.getBucketMolten(96))
        .eut(CelValues.RecipeValues.RECIPE_UV)
        .durSec(60)
        .addTo(laser)

    // 激活催化剂
    builder()
        .itemInputs(CelItemList.LensAstriumInfinity.get(0))
        .fluidInputs(CelMaterials.AstralCatalystBase.getLiquid(1000))
        .fluidOutputs(CelMaterials.AstralCatalystBaseExcited.getLiquid(1000))
        .eut(CelValues.RecipeValues.RECIPE_ZPM)
        .durSec(45)
        .addTo(laser)

    // endregion

    // endregion

    // region 矩阵
    // 兰波顿矩阵
    val crystalChipMaster = ItemList.Circuit_Parts_Crystal_Chip_Master.get(64)
    builder()
        .itemInputs(CelItemList.LapotronMatrix.get(1), CelItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.EnergeticAlloy.getMolten(1440))
        .itemOutputs(*Array(4) { crystalChipMaster })
        .eut(CelValues.RecipeValues.RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 晶体矩阵-绿
    val crystalCPU = ItemList.Circuit_Chip_CrystalCPU.get(64)
    builder()
        .itemInputs(CelItemList.CrystalMatrix.get(1), CelItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Europium.getIngotMolten(16))
        .itemOutputs(*Array(4) { crystalCPU })
        .eut(CelValues.RecipeValues.RECIPE_UV)
        .durSec(25)
        .addTo(laser)

    // 晶体矩阵-红
    builder()
        .itemInputs(CelItemList.CrystalMatrix.get(1), CelItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Americium.getIngotMolten(16))
        .itemOutputs(
            ItemList.Circuit_Chip_CrystalSoC.get(64), ItemList.Circuit_Chip_CrystalSoC.get(32))
        .eut(CelValues.RecipeValues.RECIPE_UHV)
        .durSec(15)
        .addTo(laser)
    // endregion
  }
}
