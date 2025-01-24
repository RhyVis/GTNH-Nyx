package vis.rhynia.nova.common.recipe.gt

import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import net.minecraftforge.fluids.FluidRegistry
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UEV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UHV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_ZPM
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterial

class LaserEngraverRecipePool : RecipePool {
  override fun loadRecipes() {

    val laser: IRecipeMap? = RecipeMaps.laserEngraverRecipes

    // region 光刻
    // 异氙
    builder()
        .itemInputs(
            NovaMaterial.AstriumMagic.get(OrePrefixes.dust, 1),
            NovaItemList.LensAstriumMagic.get(0))
        .fluidInputs(Materials.UUMatter.getFluid(16), WerkstoffLoader.Xenon.getFluidOrGas(1000))
        .fluidOutputs(FluidRegistry.getFluidStack("xenoxene", 500))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 活性晶体
    builder()
        .itemInputs(
            GTModHandler.getModItem("gregtech", "gt.metaitem.03", 1, 32071),
            NovaItemList.LensAstriumMagic.get(0))
        .itemOutputs(GTModHandler.getModItem("gregtech", "gt.metaitem.01", 4, 32668))
        .fluidInputs(NovaMaterial.Astrium.getMolten(50))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(45)
        .addTo(laser)

    // 皮米晶圆
    builder()
        .itemInputs(getCoreItem("RawPicoWafer"), NovaItemList.LensAstriumInfinity.get(0))
        .itemOutputs(getCoreItem("PicoWafer", 4))
        .fluidInputs(
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(100),
            Materials.Neutronium.getMolten((2 * 144).toLong()))
        .fluidOutputs(NovaMaterial.AstralResidue.getFluidOrGas(50))
        .noOptimize()
        .eut(RECIPE_UEV)
        .durSec(15)
        .addTo(laser)

    // 生物活性晶圆
    builder()
        .itemInputs(ItemList.Circuit_Silicon_Wafer6.get(1), NovaItemList.LensAstriumMagic.get(0))
        .itemOutputs(ItemList.Circuit_Wafer_Bioware.get(4))
        .fluidInputs(
            Materials.BioMediumSterilized.getFluid(4000),
            NovaMaterial.Astrium.getMolten(10 * INGOTS))
        .fluidOutputs(Materials.UUMatter.getFluid(500))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(40)
        .addTo(laser)

    // 光子强化晶圆
    builder()
        .itemInputs(
            ItemList.Circuit_Silicon_Wafer6.get(1),
            Materials.Glowstone.getNanite(1),
            NovaItemList.LensAstriumMagic.get(0),
            GTModHandler.getModItem(Mods.SuperSolarPanels.ID, "solarsplitter", 0L, 0))
        .itemOutputs(ItemList.Circuit_Silicon_Wafer7.get(4L))
        .fluidInputs(
            WerkstoffLoader.Oganesson.getFluidOrGas(2500),
            NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(800))
        .fluidOutputs(
            NovaMaterial.AstralResidue.getFluidOrGas(400), NovaMaterial.Astrium.getMolten(500))
        .noOptimize()
        .eut(RECIPE_UEV)
        .durSec(20)
        .addTo(laser)

    // 增殖星辉
    builder()
        .itemInputs(
            NovaMaterial.Astrium.get(OrePrefixes.dust, 64), NovaItemList.LensAstriumInfinity.get(0))
        .fluidOutputs(NovaMaterial.Astrium.getMolten(96 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UV)
        .durSec(60)
        .addTo(laser)

    // 激活催化剂
    builder()
        .itemInputs(NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(NovaMaterial.AstralCatalystBase.getFluidOrGas(1000))
        .fluidOutputs(NovaMaterial.AstralCatalystBaseExcited.getFluidOrGas(1000))
        .noOptimize()
        .eut(RECIPE_ZPM)
        .durSec(45)
        .addTo(laser)

    // endregion

    // endregion

    // region 矩阵
    // 兰波顿矩阵
    builder()
        .itemInputs(NovaItemList.LapotronMatrix.get(1), NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.EnergeticAlloy.getMolten(1440))
        .itemOutputs(
            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64),
            ItemList.Circuit_Parts_Crystal_Chip_Master.get(64))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(15)
        .addTo(laser)

    // 晶体矩阵-绿
    builder()
        .itemInputs(NovaItemList.CrystalMatrix.get(1), NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Europium.getIngotMolten(16))
        .itemOutputs(
            ItemList.Circuit_Chip_CrystalCPU.get(64),
            ItemList.Circuit_Chip_CrystalCPU.get(64),
            ItemList.Circuit_Chip_CrystalCPU.get(64),
            ItemList.Circuit_Chip_CrystalCPU.get(64))
        .noOptimize()
        .eut(RECIPE_UV)
        .durSec(25)
        .addTo(laser)

    // 晶体矩阵-红
    builder()
        .itemInputs(NovaItemList.CrystalMatrix.get(1), NovaItemList.LensAstriumInfinity.get(0))
        .fluidInputs(Materials.Americium.getIngotMolten(16))
        .itemOutputs(
            ItemList.Circuit_Chip_CrystalSoC.get(64), ItemList.Circuit_Chip_CrystalSoC.get(32))
        .noOptimize()
        .eut(RECIPE_UHV)
        .durSec(15)
        .addTo(laser)
    // endregion
  }
}
