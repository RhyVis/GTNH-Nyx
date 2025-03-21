package rhynia.nyx.common.recipe.nyx

import bartworks.system.material.WerkstoffLoader
import goodgenerator.items.GGMaterial
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsAlloy
import gtPlusPlus.core.material.MaterialsElements
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_MAX
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UEV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UHV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UIV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UMV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UV
import rhynia.nyx.api.recipe.NyxRecipeMaps
import rhynia.nyx.api.util.FluidUtil
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool

@Suppress("SpellCheckingInspection")
class ThermonuclearControlRecipePool : RecipePool() {
    private val tc = NyxRecipeMaps.thermonuclearControlRecipes
    private val esCata =
        if (Mods.EternalSingularity.isModLoaded) {
            Mods.EternalSingularity.getItem("eternal_singularity", 0)
        } else {
            NyxItemList.TestItem01.get(0)
        }

    override fun loadRecipes() {
        // region 稀有气体
        // 等离子 He
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
            .fluidInputs(Materials.Helium.getGas(16 * BUCKETS.toLong()))
            .fluidOutputs(Materials.Helium.getPlasma(16 * BUCKETS.toLong()))
            .eut(RECIPE_UV)
            .durSec(12)
            .addTo(tc)

        // 等离子 Ne
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
            .fluidInputs(WerkstoffLoader.Neon.getFluidOrGas(16 * BUCKETS))
            .fluidOutputs(FluidUtil.getFluidStack("plasma.neon", 16 * BUCKETS))
            .eut(RECIPE_UHV)
            .durSec(12)
            .addTo(tc)

        // 等离子 Ar
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
            .fluidInputs(Materials.Argon.getGas(16 * BUCKETS.toLong()))
            .fluidOutputs(Materials.Argon.getPlasma(16 * BUCKETS.toLong()))
            .eut(RECIPE_UHV)
            .durSec(12)
            .addTo(tc)

        // 等离子 Kr
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(16 * BUCKETS))
            .fluidOutputs(FluidUtil.getFluidStack("plasma.krypton", 16 * BUCKETS))
            .eut(RECIPE_UEV)
            .durSec(12)
            .addTo(tc)

        // 等离子 Xe
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
            .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(16 * BUCKETS))
            .fluidOutputs(FluidUtil.getFluidStack("plasma.xenon", 16 * BUCKETS))
            .eut(RECIPE_UEV)
            .durSec(12)
            .addTo(tc)

        // 等离子 Rn
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
            .fluidInputs(Materials.Radon.getGas(16 * BUCKETS.toLong()))
            .fluidOutputs(Materials.Radon.getPlasma(16 * BUCKETS.toLong()))
            .eut(RECIPE_UEV)
            .durSec(12)
            .addTo(tc)
        // endregion

        // region 特殊材料制造

        // 兰波顿核心
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                getCoreItem("LapotronDust", 64),
                getCoreItem("LapotronDust", 64),
            ).fluidInputs(NyxMaterials.AstralCatalystBaseExcited.getLiquid(BUCKETS))
            .fluidOutputs(
                NyxMaterials.LapotronEnhancedFluid.getLiquid(8 * BUCKETS),
                NyxMaterials.AstralResidue.getLiquid(500),
            ).eut(RECIPE_UHV)
            .durSec(115)
            .addTo(tc)

        // 超导通流
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                esCata,
                NyxItemList.LensOriginium.get(0),
                NyxItemList.LensAstriumMagic.get(0),
            ).fluidInputs(
                NyxMaterials.AstralCatalystBaseExcited.getLiquid(32 * BUCKETS),
                NyxMaterials.SuperconductorFluxRaw.getMolten(72 * INGOTS),
            ).fluidOutputs(
                NyxMaterials.SuperconductorFlux.getLiquid(64 * INGOTS),
                NyxMaterials.AstralResidue.getLiquid(16 * BUCKETS),
            ).eut(RECIPE_UEV)
            .durSec(30)
            .addTo(tc)

        // 太初物质（测试）
        builder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), NyxItemList.AstriumInfinityGauge.get(0))
            .fluidInputs(NyxMaterials.AstralResidue.getLiquid(512 * BUCKETS))
            .fluidOutputs(NyxMaterials.PrimordialEssence.getPlasma(100))
            .eut(RECIPE_MAX)
            .durMin(4)
            .addTo(tc)

        // endregion

        // region 聚变

        // MetaStableOg
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(64),
            ).fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(144 * BUCKETS))
            .fluidOutputs(FluidUtil.getFluidStack("molten.metastable oganesson", 1000 * INGOTS))
            .eut(RECIPE_UMV)
            .durSec(115)
            .addTo(tc)

        // AdvNitinol
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(4),
            ).fluidInputs(MaterialsAlloy.NITINOL_60.getFluidStack(1000 * INGOTS))
            .fluidOutputs(FluidUtil.getFluidStack("molten.advancednitinol", 1000 * INGOTS))
            .eut(RECIPE_UHV)
            .durSec(135)
            .addTo(tc)
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(8),
            ).fluidInputs(Materials.Nickel.getIngotMolten(400), Materials.Titanium.getIngotMolten(600))
            .fluidOutputs(FluidUtil.getFluidStack("molten.advancednitinol", 1000 * INGOTS))
            .eut(RECIPE_UEV)
            .durSec(100)
            .addTo(tc)

        // AstralTitanium
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(8),
            ).fluidInputs(Materials.Titanium.getIngotMolten(1000))
            .fluidOutputs(FluidUtil.getFluidStack("molten.astraltitanium", 1000 * INGOTS))
            .eut(RECIPE_UEV)
            .durSec(120)
            .addTo(tc)

        // ChromaticGlass
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(8),
            ).fluidInputs(Materials.Glass.getIngotMolten(1000))
            .fluidOutputs(FluidUtil.getFluidStack("molten.chromaticglass", 1000 * INGOTS))
            .eut(RECIPE_UEV)
            .durSec(195)
            .addTo(tc)

        // CelestialTungsten
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(8),
            ).fluidInputs(Materials.Tungsten.getIngotMolten(1000))
            .fluidOutputs(FluidUtil.getFluidStack("molten.celestialtungsten", 1000 * INGOTS))
            .eut(RECIPE_UEV)
            .durSec(145)
            .addTo(tc)

        // DragonBlood
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(16),
            ).fluidInputs(
                Materials.DraconiumAwakened.getIngotMolten(1000),
                NyxMaterials.Astrium.getMolten(12 * BUCKETS),
            ).fluidOutputs(FluidUtil.getFluidStack("molten.dragonblood", 1000 * INGOTS))
            .eut(RECIPE_UEV)
            .durMin(3)
            .addTo(tc)

        // Np
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(16),
            ).fluidInputs(
                Materials.Radon.getGas(750 * INGOTS.toLong()),
                Materials.Nitrogen.getGas(750 * INGOTS.toLong()),
            ).fluidOutputs(FluidUtil.getFluidStack("plasma.neptunium", 1000 * INGOTS))
            .eut(RECIPE_UIV)
            .durSec(40)
            .addTo(tc)

        // Fm
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(16),
            ).fluidInputs(Materials.Americium.getIngotMolten(750), Materials.Boron.getIngotMolten(750))
            .fluidOutputs(FluidUtil.getFluidStack("plasma.fermium", 1000 * INGOTS))
            .eut(RECIPE_UIV)
            .durSec(40)
            .addTo(tc)

        // Transcendent Metal
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.PreTesseract.get(64),
            ).fluidInputs(
                Materials.CosmicNeutronium.getIngotMolten(64),
                NyxMaterials.AstralCatalystBaseExcited.getLiquid(4 * BUCKETS),
            ).fluidOutputs(
                MaterialsUEVplus.TranscendentMetal.getIngotMolten(32 * 64),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(16 * 64 * INGOTS),
                NyxMaterials.AstralResidue.getLiquid(4 * BUCKETS),
            ).eut(RECIPE_UIV)
            .durSec(30)
            .addTo(tc)

        // Shirabon
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                NyxItemList.AstriumInfinityGem.get(32),
            ).fluidInputs(
                GGMaterial.metastableOganesson.getMolten(16 * INGOTS),
                NyxMaterials.AstralCatalystReforged.getLiquid(250),
            ).fluidOutputs(
                GGMaterial.shirabon.getMolten(16 * INGOTS),
                NyxMaterials.AstralResidue.getLiquid(500),
            ).eut(RECIPE_UMV)
            .durSec(25)
            .addTo(tc)

        // ELEMENT.STANDALONE.RHUGNOR.getFluidStack(240 * INGOTS)
        builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                NyxItemList.LensAstriumInfinity.get(0),
                esCata,
                GTUtility.copyAmountUnsafe(96, NyxItemList.AstriumInfinityGem.get(1)),
            ).fluidInputs(
                Materials.Infinity.getIngotMolten(256),
                MaterialsAlloy.QUANTUM.getFluidStack(256 * INGOTS),
            ).fluidOutputs(
                MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(512 * INGOTS),
                NyxMaterials.AstralResidue.getLiquid(5000),
            ).eut(RECIPE_UIV)
            .durSec(80)
            .addTo(tc)

        // endregion

        loadBatchAdder()
    }

    private fun loadBatchAdder() {
        // region Gas to Plasma (UEV)
        val gas2plasma =
            arrayOf(
                Materials.Hydrogen,
                Materials.Nitrogen,
                Materials.Oxygen,
                Materials.Fluorine,
                Materials.Chlorine,
            )
        for (materials in gas2plasma) {
            builder()
                .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
                .fluidInputs(materials.getGas(128 * INGOTS.toLong()))
                .fluidOutputs(materials.getPlasma(128 * INGOTS.toLong()))
                .eut(RECIPE_UEV)
                .durSec(12)
                .addTo(tc)
        }
        // endregion

        // region Dust to Plasma (UEV)
        val dust2plasma =
            arrayOf(
                Materials.Boron,
                Materials.Sodium,
                Materials.Magnesium,
                Materials.Aluminium,
                Materials.Silicon,
                Materials.Phosphorus,
                Materials.Sulfur,
                Materials.Strontium,
            )
        for (materials in dust2plasma) {
            builder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(12),
                    NyxItemList.LensAstriumInfinity.get(0),
                    materials.getDust(64),
                    materials.getDust(64),
                ).fluidOutputs(materials.getPlasma(128 * INGOTS.toLong()))
                .eut(RECIPE_UEV)
                .durSec(12)
                .addTo(tc)
        }
        // endregion

        // region Molten to Plasma (UEV)
        val molten2plasma =
            arrayOf<Materials>(
                Materials.Silver,
                Materials.Gold,
                Materials.Iron,
                Materials.Bismuth,
                Materials.Calcium,
                Materials.Neodymium,
                Materials.Niobium,
                Materials.Lead,
                Materials.Naquadah,
                Materials.Nickel,
                Materials.Rubidium,
                Materials.Thorium,
                Materials.Plutonium241,
                Materials.Americium,
                Materials.Tin,
                Materials.Titanium,
                Materials.Tungsten,
                Materials.Zinc,
                Materials.Platinum,
                Materials.Osmium,
            )
        for (materials in molten2plasma) {
            builder()
                .itemInputs(GTUtility.getIntegratedCircuit(12), NyxItemList.LensAstriumInfinity.get(0))
                .fluidInputs(materials.getMolten(128 * INGOTS.toLong()))
                .fluidOutputs(materials.getPlasma(128 * INGOTS.toLong()))
                .eut(RECIPE_UEV)
                .durSec(12)
                .addTo(tc)
        }
        // endregion
    }
}
