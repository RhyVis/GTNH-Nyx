package rhynia.constellation.common.recipe.cel

import bartworks.system.material.WerkstoffLoader
import goodgenerator.items.GGMaterial
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods.HardcoreEnderExpansion
import gregtech.api.enums.Mods.NewHorizonsCoreMod
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility.getIntegratedCircuit
import gtPlusPlus.core.item.ModItems
import gtPlusPlus.core.material.MaterialMisc
import gtPlusPlus.core.util.minecraft.ItemUtils
import gtnhlanth.common.register.WerkstoffMaterialPool
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_EV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_HV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_IV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_LuV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UEV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UHV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_UXV
import rhynia.constellation.api.enums.CelValues.RecipeValues.RECIPE_ZPM
import rhynia.constellation.api.recipe.CelRecipeMaps
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.CelMaterials
import rhynia.constellation.common.recipe.RecipePool

class AstralForgeRecipePool : RecipePool() {
  private val af = CelRecipeMaps.astralForgeRecipes

  override fun loadRecipes() {
    // region 10

    // UU-Matter Production
    builder()
        .itemInputs(getIntegratedCircuit(10))
        .fluidInputs(Materials.UUMatter.getFluid(2048))
        .fluidOutputs(Materials.UUMatter.getFluid(32768))
        .eut(RECIPE_LuV)
        .durSec(40)
        .addTo(af)
    // Eternal
    builder()
        .itemInputs(
            getIntegratedCircuit(10),
            CelItemList.LensAstriumInfinity.get(0),
            CelItemList.AstriumInfinityGem.get(1),
        )
        .fluidInputs(
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(4),
            MaterialsUEVplus.SpaceTime.getIngotMolten(4),
        )
        .itemOutputs(
            GGMaterial.shirabon.getDust(64),
            ItemList.Timepiece.get(4),
        )
        .fluidOutputs(
            MaterialsUEVplus.Eternity.getIngotMolten(8),
            MaterialsUEVplus.PrimordialMatter.getBucketFluid(2),
        )
        .outputChances(80_00, 60_00)
        .eut(RECIPE_UXV)
        .durSec(10)
        .addTo(af)

    // endregion

    // region 12

    // 润滑油
    builder()
        .itemInputs(
            getIntegratedCircuit(12),
            CelItemList.LensAstriumMagic.get(0),
            CelMaterials.Astrium.getDust(16),
            Materials.Redstone.getDust(64),
        )
        .fluidInputs(Materials.Water.getBucketFluid(256))
        .fluidOutputs(Materials.Lubricant.getFluid(256))
        .eut(RECIPE_UV)
        .durSec(12)
        .addTo(af)
    // 不稳定金属
    builder()
        .itemInputs(
            getIntegratedCircuit(12),
            CelItemList.LensAstriumMagic.get(0),
            Materials.Iron.getDust(64),
            Materials.Iron.getDust(64),
            Materials.Diamond.getDust(64),
            Materials.Diamond.getDust(64),
        )
        .itemOutputs(Materials.Unstable.getDust(64), Materials.Unstable.getDust(64))
        .eut(RECIPE_IV)
        .durSec(20)
        .addTo(af)
    // 星辉催化剂
    builder()
        .itemInputs(
            getIntegratedCircuit(12),
            CelItemList.LensAstriumMagic.get(0),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.AstralCatalystBase.getDust(16))
        .itemOutputs(CelMaterials.AstriumInfinity.getDust(1))
        .fluidOutputs(CelMaterials.AstralCatalystBase.getLiquid(48 * BUCKETS))
        .noOptimize()
        .eut(RECIPE_UV)
        .durSec(20)
        .addTo(af)

    // endregion

    // region 11

    // 灵魂沙
    val sand = ItemStack(Blocks.sand, 64)
    val soulSand = ItemStack(Blocks.soul_sand, 64)
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            CelItemList.LensAstriumInfinity.get(0),
            ItemStack(Items.blaze_powder, 4, 0),
            *Array(8) { sand },
        )
        .fluidInputs(Materials.Water.getBucketFluid(1), CelMaterials.Astrium.getMolten(16))
        .itemOutputs(soulSand)
        .eut(RECIPE_HV)
        .durSec(18)
        .addTo(af)
    // 烈焰粉
    val blazePowder = ItemStack(Items.blaze_powder, 64)
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            CelItemList.LensAstriumInfinity.get(0),
            Materials.Carbon.getDust(64),
            Materials.Sulfur.getDust(64),
        )
        .fluidInputs(Materials.Lava.getBucketFluid(1), CelMaterials.Astrium.getMolten(16))
        .itemOutputs(*Array(4) { blazePowder })
        .eut(RECIPE_EV)
        .durSec(18)
        .addTo(af)
    // 末影珍珠
    val enderPearl = ItemStack(Items.ender_pearl, 64)
    val endPowder =
        if (HardcoreEnderExpansion.isModLoaded)
            GTModHandler.getModItem(HardcoreEnderExpansion.ID, "end_powder", 64)
        else ItemStack(Items.ender_pearl, 64)
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            CelItemList.LensAstriumMagic.get(0),
            endPowder,
            CelMaterials.Astrium.getDust(64),
        )
        .fluidInputs(CelMaterials.Astrium.getMolten(16))
        .itemOutputs(*Array(4) { enderPearl })
        .eut(RECIPE_IV)
        .durSec(18)
        .addTo(af)
    // 胶质云母
    val micaBasedPulp = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MicaBasedPulp", 64)
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            CelItemList.LensAstriumMagic.get(0),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64),
            Materials.Mica.getDust(64),
            Materials.Mica.getDust(64),
            Materials.Mica.getDust(64),
            Materials.Mica.getDust(64),
        )
        .fluidInputs(CelMaterials.Astrium.getMolten(1000))
        .itemOutputs(*Array(12) { micaBasedPulp })
        .eut(RECIPE_LuV)
        .durSec(20)
        .addTo(af)

    // endregion

    // region 9 - Distillation

    // 净化水
    val mysteriousCrystalLens =
        GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0)
    val chromaticLens = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 0)
    builder()
        .itemInputs(
            getIntegratedCircuit(9),
            CelItemList.LensAstriumInfinity.get(0),
            CelItemList.LensAstriumMagic.get(0),
            WerkstoffLoader.Tiberium.get(OrePrefixes.lens, 0),
            mysteriousCrystalLens,
            chromaticLens,
            CelItemList.LensPrimoium.get(0),
            CelItemList.LensOriginium.get(0),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64),
        )
        .fluidInputs(Materials.Water.getBucketFluid(512), Materials.UUMatter.getBucketFluid(16))
        .itemOutputs(
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 1),
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 7, 1),
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 18, 1),
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 24, 1),
            CelMaterials.AstriumMagic.getDust(32),
            CelMaterials.AstriumInfinity.getDust(32),
        )
        .outputChances(10, 10, 10, 5, 5000, 1000)
        .fluidOutputs(
            Materials.Grade1PurifiedWater.getBucketFluid(128),
            Materials.Grade2PurifiedWater.getBucketFluid(128),
            Materials.Grade3PurifiedWater.getBucketFluid(64),
            Materials.Grade4PurifiedWater.getBucketFluid(64),
            Materials.Grade5PurifiedWater.getBucketFluid(32),
            Materials.Grade6PurifiedWater.getBucketFluid(32),
            Materials.Grade7PurifiedWater.getBucketFluid(32),
            Materials.Grade8PurifiedWater.getBucketFluid(32),
        )
        .eut(RECIPE_UEV)
        .durMin(2)
        .addTo(af)
    // endregion

    // region 8 - Ore

    // SiO2
    val siDust = Materials.Silicon.getDust(64)
    val siSgDust = Materials.SiliconSG.getDust(64)
    arrayOf(
            Materials.SiliconDioxide,
            Materials.NetherQuartz,
            Materials.CertusQuartz,
            Materials.Quartzite)
        .forEach { s ->
          val inputArray =
              arrayOfNulls<ItemStack>(16).apply {
                this[0] = getIntegratedCircuit(8)
                this[1] = CelItemList.LensAstriumInfinity.get(0)
                fill(s.getDust(64), 2)
              }
          builder()
              .itemInputs(*inputArray)
              .itemOutputs(
                  *Array(8) { siDust },
                  *Array(4) { siSgDust },
                  CelMaterials.Astrium.getDust(64),
                  CelMaterials.Astrium.getDust(64))
              .outputChances(*IntArray(8) { 10000 }, 7000, 7000, 7000, 7000, 6000, 6000)
              .eut(RECIPE_HV)
              .durSec(40)
              .addTo(af)
        }
    // 方解石
    val caCo3Dust = Materials.Calcite.getDust(64)
    val caDust = Materials.Calcium.getDust(64)
    val cDust = Materials.Carbon.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(14) { caCo3Dust })
        .itemOutputs(
            *Array(4) { caDust },
            *Array(8) { cDust },
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64),
        )
        .outputChances(*IntArray(12) { 10000 }, 8000, 8000)
        .eut(RECIPE_HV)
        .durSec(40)
        .addTo(af)
    // 磷酸盐
    val phOxDust = Materials.Phosphate.getDust(64)
    val phDust = Materials.Phosphorus.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(14) { phOxDust })
        .itemOutputs(
            *Array(8) { phDust },
            *Array(4) { CelMaterials.Astrium.getDust(64) },
        )
        .outputChances(*IntArray(8) { 10000 }, 8000, 8000, 6000, 6000)
        .eut(RECIPE_HV)
        .durSec(40)
        .addTo(af)
    // 硫酸钠
    val na2So4 = WerkstoffLoader.Sodiumsulfate.getDust(64)
    val naDust = Materials.Sodium.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8), CelItemList.LensAstriumInfinity.get(0), *Array(14) { na2So4 })
        .itemOutputs(
            *Array(10) { naDust },
            Materials.Sulfur.getDust(64),
            Materials.Sulfur.getDust(64),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64))
        .outputChances(*IntArray(12) { 10000 }, 8000, 8000)
        .eut(RECIPE_HV)
        .durSec(40)
        .addTo(af)
    // 钐
    val smMix = WerkstoffMaterialPool.SamariumOreConcentrate.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8), CelItemList.LensAstriumInfinity.get(0), *Array(4) { smMix })
        .itemOutputs(
            Materials.Samarium.getDust(64),
            Materials.Samarium.getDust(32),
            Materials.Lanthanum.getDust(16),
            Materials.Cerium.getDust(16),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64))
        .outputChances(10000, 10000, 9500, 9500, 7500, 7500)
        .noOptimize()
        .eut(RECIPE_HV)
        .durSec(40)
        .addTo(af)
    // 富铈粉
    val ceMix = WerkstoffMaterialPool.CeriumRichMixture.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8), CelItemList.LensAstriumInfinity.get(0), *Array(4) { ceMix })
        .itemOutputs(
            Materials.Cerium.getDust(64),
            Materials.Cerium.getDust(16),
            Materials.Lanthanum.getDust(16),
            Materials.Europium.getDust(16),
            Materials.Gadolinium.getDust(16),
            CelMaterials.Astrium.getDust(64),
            CelMaterials.Astrium.getDust(64))
        .outputChances(10000, 10000, 9500, 8000, 8000, 7500, 7500)
        .noOptimize()
        .eut(RECIPE_HV)
        .durSec(40)
        .addTo(af)
    // 稀土
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            Materials.RareEarth.getDust(64),
            Materials.RareEarth.getDust(64))
        .itemOutputs(
            MaterialMisc.RARE_EARTH_LOW.getDust(48),
            MaterialMisc.RARE_EARTH_MID.getDust(40),
            MaterialMisc.RARE_EARTH_HIGH.getDust(32))
        .noOptimize()
        .eut(RECIPE_IV)
        .durSec(12)
        .addTo(af)
    // 铂金属
    val ptMPowder = WerkstoffLoader.PTMetallicPowder.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(14) { ptMPowder })
        .itemOutputs(
            Materials.Platinum.getDust(64),
            Materials.Platinum.getDust(64),
            Materials.Palladium.getDust(64),
            Materials.Palladium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Osmium.getDust(64),
            WerkstoffLoader.Rhodium.getDust(64),
            WerkstoffLoader.Rhodium.getDust(64),
            WerkstoffLoader.Ruthenium.getDust(64),
            WerkstoffLoader.Ruthenium.getDust(64))
        .outputChances(8000, 3000, 8000, 3000, 8000, 3000, 8000, 3000, 8000, 3000, 8000, 3000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 铱渣
    val irRe = WerkstoffLoader.IrLeachResidue.getDust(64)
    val irDust = Materials.Iridium.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8), CelItemList.LensAstriumInfinity.get(0), *Array(8) { irRe })
        .itemOutputs(
            *Array(8) { irDust },
            Materials.Gold.getDust(64),
            Materials.Gold.getDust(64),
            Materials.Silicon.getDust(64),
            Materials.Silicon.getDust(64))
        .outputChances(10000, 10000, 10000, 10000, 8000, 8000, 6000, 6000, 6000, 6000, 3000, 3000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 浸出渣
    val leachResidue = WerkstoffLoader.LeachResidue.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(8) { leachResidue })
        .itemOutputs(
            Materials.Iridium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Osmium.getDust(64),
            WerkstoffLoader.Ruthenium.getDust(64),
            WerkstoffLoader.Ruthenium.getDust(64),
            WerkstoffLoader.Ruthenium.getDust(64),
            Materials.Gold.getDust(48),
            Materials.Gold.getDust(48),
            Materials.Silicon.getDust(48),
            Materials.Silicon.getDust(48))
        .outputChances(8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 8000, 6000, 6000, 3000, 3000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 锇渣
    val osRe = WerkstoffLoader.IrOsLeachResidue.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8), CelItemList.LensAstriumInfinity.get(0), *Array(8) { osRe })
        .itemOutputs(
            Materials.Iridium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Iridium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Osmium.getDust(64),
            Materials.Gold.getDust(64),
            Materials.Gold.getDust(64),
            Materials.Silicon.getDust(64),
            Materials.Silicon.getDust(64))
        .outputChances(10000, 10000, 8000, 8000, 10000, 10000, 8000, 8000, 6000, 6000, 3000, 3000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 钯金属
    val pdMPowder = WerkstoffLoader.PDMetallicPowder.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(12) { pdMPowder })
        .itemOutputs(*Array(12) { Materials.Palladium.getDust(64) })
        .outputChances(*IntArray(8) { 10000 }, 6000, 6000, 6000, 6000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 粗制铑金属
    val crudeRhMetal = WerkstoffLoader.CrudeRhMetall.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(12) { crudeRhMetal })
        .itemOutputs(*Array(12) { WerkstoffLoader.Rhodium.getDust(64) })
        .outputChances(*IntArray(8) { 10000 }, 6000, 6000, 6000, 6000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 氧化硅岩
    val naqEarth = GGMaterial.naquadahEarth.getDust(64)
    val naqDust = Materials.Naquadah.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(12) { naqEarth })
        .itemOutputs(
            *Array(8) { naqDust },
            Materials.Titanium.getDust(64),
            Materials.Titanium.getDust(64),
            Materials.Titanium.getDust(64),
            Materials.Titanium.getDust(64),
            Materials.Adamantium.getDust(64),
            Materials.Adamantium.getDust(64),
            Materials.Gallium.getDust(64),
            Materials.Gallium.getDust(64))
        .outputChances(
            10000,
            10000,
            10000,
            10000,
            8000,
            6000,
            4000,
            2000,
            8000,
            8000,
            8000,
            8000,
            8000,
            8000,
            8000,
            8000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 超能硅岩
    val naqDrEarth = GGMaterial.naquadriaEarth.getDust(64)
    val naqDrDust = Materials.Naquadria.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(12) { naqDrEarth })
        .itemOutputs(
            *Array(10) { naqDrDust },
            Materials.Barium.getDust(64),
            Materials.Barium.getDust(64),
            Materials.Barium.getDust(64),
            Materials.Indium.getDust(64),
            Materials.Indium.getDust(64),
            Materials.Indium.getDust(64))
        .outputChances(
            10000,
            10000,
            10000,
            10000,
            8000,
            8000,
            8000,
            8000,
            6000,
            6000,
            8000,
            6000,
            6000,
            8000,
            6000,
            6000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)
    // 富集硅岩
    val naqErEarth = GGMaterial.enrichedNaquadahEarth.getDust(64)
    val naqErDust = Materials.NaquadahEnriched.getDust(64)
    builder()
        .itemInputs(
            getIntegratedCircuit(8),
            CelItemList.LensAstriumInfinity.get(0),
            *Array(12) { naqErEarth })
        .itemOutputs(
            *Array(10) { naqErDust },
            Materials.Trinium.getDust(64),
            Materials.Trinium.getDust(64),
            Materials.Trinium.getDust(64),
            Materials.Trinium.getDust(64),
            Materials.Chrome.getDust(64))
        .outputChances(
            10000,
            10000,
            10000,
            10000,
            8000,
            8000,
            8000,
            8000,
            6000,
            6000,
            6000,
            6000,
            6000,
            6000,
            4000)
        .noOptimize()
        .eut(RECIPE_LuV)
        .durSec(16)
        .addTo(af)

    // endregion

    // region 4 - Crafting

    // 合成阳光化合物 Su
    builder()
        .itemInputs(
            getIntegratedCircuit(4),
            CelItemList.LensAstriumInfinity.get(0),
            CelMaterials.Astrium.getDust(64),
            Materials.Glowstone.getDust(64))
        .fluidInputs(Materials.Hydrogen.getGas(48000))
        .itemOutputs(
            Materials.Sunnarium.getDust(64),
            Materials.Sunnarium.getDust(64),
            Materials.Sunnarium.getDust(64),
            Materials.Sunnarium.getDust(64))
        .eut(RECIPE_ZPM)
        .durSec(22)
        .addTo(af)
    // 合成超能硅岩 Nq*
    builder()
        .itemInputs(
            getIntegratedCircuit(4),
            CelItemList.LensAstriumInfinity.get(0),
            CelMaterials.AstriumInfinity.getDust(12),
            CelMaterials.Astrium.getDust(32),
            *Array(4) { naqDust })
        .fluidInputs(Materials.NaquadahEnriched.getIngotMolten(32))
        .itemOutputs(
            Materials.Naquadria.getDust(64),
            Materials.Naquadria.getDust(64),
            Materials.Naquadria.getDust(64),
            Materials.Naquadria.getDust(64))
        .eut(RECIPE_UV)
        .durSec(36)
        .addTo(af)
    // 合成合成玉 Or
    builder()
        .itemInputs(
            getIntegratedCircuit(4),
            CelItemList.LensAstriumInfinity.get(0),
            CelMaterials.Astrium.getDust(60),
            Materials.Naquadah.getDust(60))
        .fluidInputs(Materials.Helium.getGas(120_000), Materials.Quantium.getIngotMolten(32))
        .itemOutputs(GGMaterial.orundum.getDust(64), GGMaterial.orundum.getDust(64))
        .eut(RECIPE_UV)
        .durSec(100)
        .addTo(af)
    // 合成源石
    builder()
        .itemInputs(
            getIntegratedCircuit(4),
            CelItemList.LensAstriumMagic.get(0),
            CelItemList.LensAstriumInfinity.get(0),
            WerkstoffLoader.Tiberium.get(OrePrefixes.lens, 0),
            GGMaterial.orundum.getDust(64),
            GGMaterial.orundum.getDust(64),
            GGMaterial.orundum.getDust(64),
            GGMaterial.orundum.getDust(64),
            Materials.Naquadah.getDust(60),
            CelMaterials.Astrium.getDust(64),
            Materials.InfinityCatalyst.getDust(16))
        .itemOutputs(CelMaterials.Originium.getDust(64), CelMaterials.Originium.getDust(48))
        .eut(RECIPE_UHV)
        .durMin(2)
        .addTo(af)

    // endregion

    // region 1 - Astrium

    // Astrium Dust
    arrayOf(
            Materials.Grade1PurifiedWater,
            Materials.Grade2PurifiedWater,
            Materials.Grade3PurifiedWater,
            Materials.Grade4PurifiedWater,
            Materials.Grade5PurifiedWater,
            Materials.Grade6PurifiedWater,
            Materials.Grade7PurifiedWater)
        .forEachIndexed { index, material ->
          val modifier = 8 * (index + 2)
          builder()
              .itemInputs(getIntegratedCircuit(1), CelItemList.LensPrimoium.get(0))
              .itemOutputs(*Array(16) { CelMaterials.Astrium.getDust(modifier) })
              .fluidInputs(material.getBucketFluid(16))
              .fluidOutputs(
                  CelMaterials.Astrium.getMolten(12 * modifier * INGOTS),
                  CelMaterials.AstriumInfinity.getMolten(4 * modifier * INGOTS),
              )
              .eut(RECIPE_UV)
              .durSec(16)
              .addTo(af)
        }

    // endregion
  }
}
