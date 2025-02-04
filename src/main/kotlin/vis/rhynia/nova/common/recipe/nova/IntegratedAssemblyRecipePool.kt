package vis.rhynia.nova.common.recipe.nova

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList
import com.gtnewhorizons.gtnhintergalactic.item.IGItems
import goodgenerator.items.GGMaterial
import goodgenerator.util.ItemRefer
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods
import gregtech.api.enums.Mods.AE2FluidCraft
import gregtech.api.enums.Mods.AdvancedSolarPanel
import gregtech.api.enums.Mods.AppliedEnergistics2
import gregtech.api.enums.Mods.Avaritia
import gregtech.api.enums.Mods.AvaritiaAddons
import gregtech.api.enums.Mods.EternalSingularity
import gregtech.api.enums.Mods.GTPlusPlus
import gregtech.api.enums.Mods.GraviSuite
import gregtech.api.enums.Mods.IndustrialCraft2
import gregtech.api.enums.Mods.NewHorizonsCoreMod
import gregtech.api.enums.Mods.SuperSolarPanels
import gregtech.api.enums.Mods.TinkerConstruct
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsAlloy
import gtPlusPlus.core.material.MaterialsElements
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList
import tectech.loader.recipe.BaseRecipeLoader
import tectech.thing.CustomItemList
import vexatos.tgregworks.reference.PartTypes
import vexatos.tgregworks.util.TGregUtils
import vis.rhynia.nova.Config
import vis.rhynia.nova.api.enums.NovaMods
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_EV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_IV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LuV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UEV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UHV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UIV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UMV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UXV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_ZPM
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.SECOND
import vis.rhynia.nova.api.enums.ref.BartPart
import vis.rhynia.nova.api.enums.ref.BasicRef
import vis.rhynia.nova.api.enums.ref.BundleChip
import vis.rhynia.nova.api.enums.ref.SolderMaterial
import vis.rhynia.nova.api.enums.ref.SuperConductorPart
import vis.rhynia.nova.api.enums.ref.Tier
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.api.util.FluidUtil
import vis.rhynia.nova.api.util.ItemUtil
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.NovaMaterial

@Suppress("SpellCheckingInspection")
class IntegratedAssemblyRecipePool : RecipePool {
  private val ia = NovaRecipeMaps.integratedAssemblyRecipes

  override fun loadRecipes() {
    loadMain()

    if (Config.loadTstRecipe && NovaMods.TwistSpaceTechnology.isModLoaded) loadTstRecipe()
    if (Config.loadWirelessHatchRecipe) loadWirelessHatchRecipe()
  }

  private fun loadMain() {
    // region MISC
    // 光辉玻璃板
    builder()
        .itemInputs(Materials.Sunnarium.getPlates(16))
        .fluidInputs(
            Materials.Glass.getIngotMolten(72),
            Materials.Glowstone.getIngotMolten(16),
            Materials.Uranium.getIngotMolten(16),
        )
        .itemOutputs(GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 64, 5))
        .eut(RECIPE_EV)
        .durSec(20)
        .addTo(ia)
    // 无尽箱子
    builder()
        .itemInputs(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 10, 60),
            ItemList.Quantum_Chest_IV.get(18),
            GTModHandler.getModItem(Avaritia.ID, "Resource", 36, 0),
            BasicRef.getSingularity(1))
        .fluidInputs(
            Materials.InfinityCatalyst.getIngotMolten(64),
            Materials.Infinity.getIngotMolten(4 + 4 * 9),
            Materials.CosmicNeutronium.getIngotMolten(6 * 9),
        )
        .itemOutputs(GTModHandler.getModItem(AvaritiaAddons.ID, "InfinityChest", 1, 0))
        .eut(RECIPE_UHV)
        .durSec(4)
        .addTo(ia)
    // 中子压缩机
    if (Mods.TinkersGregworks.isModLoaded)
        builder()
            .itemInputs(
                ItemList.Hull_UV.get(1),
                Tier.UV.getComponent(Tier.Component.ElectricMotor, 4),
                Tier.UV.getComponent(Tier.Component.ElectricPiston, 8),
                Tier.UV.getComponent(Tier.Component.ConveyorModule, 8),
                Tier.UHV.getCircuit(4),
                GTModHandler.getModItem(Avaritia.ID, "Resource", 8, 1),
                GTModHandler.getModItem(TinkerConstruct.ID, "heavyPlate", 10, 500),
                TGregUtils.newItemStack(Materials.BlackPlutonium, PartTypes.LargePlate, 8),
                GTModHandler.getModItem(TinkerConstruct.ID, "heavyPlate", 6, 315))
            .fluidInputs(
                Materials.CosmicNeutronium.getIngotMolten(72),
                Materials.Neutronium.getIngotMolten(4),
            )
            .itemOutputs(GTModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 1, 0))
            .eut(RECIPE_UV)
            .durSec(4)
            .addTo(ia)
    // 传送机
    builder()
        .itemInputs(
            ItemList.Hull_HV.get(1),
            Tier.EV.getComponent(Tier.Component.ElectricMotor, 2),
            Tier.HV.getComponent(Tier.Component.FieldGenerator, 1),
            GTOreDictUnificator.get(OrePrefixes.lens, Materials.NetherStar, 2),
        )
        .fluidInputs(
            Materials.Titanium.getIngotMolten(5),
            Materials.Aluminium.getIngotMolten(4),
            NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(200))
        .itemOutputs(GTModHandler.getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0))
        .eut(RECIPE_EV)
        .durSec(4)
        .addTo(ia)
    // Gravitation Engine
    builder()
        .itemInputs(
            SuperConductorPart.LuV.getPrefix(OrePrefixes.wireGt16, 4),
            ItemList.IV_Coil.get(16),
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 64),
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 64))
        .fluidInputs(FluidUtil.getFluidStack("supercoolant", 32 * BUCKETS))
        .itemOutputs(GTModHandler.getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3))
        .eut(RECIPE_IV)
        .durSec(30)
        .addTo(ia)
    // PreTesseract v1
    builder()
        .itemInputs(NovaItemList.AstriumInfinityGem.get(1), BundleChip.ZPM.getItemStack(1))
        .fluidInputs(
            Materials.CosmicNeutronium.getIngotMolten(16 * 4),
            MaterialsAlloy.OCTIRON.getFluidStack(16 * 4 * INGOTS),
            GGMaterial.tairitsu.getIngotMolten(16 * 4),
            Materials.Sunnarium.getIngotMolten(16 * 4),
            MaterialsAlloy.ABYSSAL.getFluidStack(16 * 24 * INGOTS),
            MaterialsAlloy.BOTMIUM.getFluidStack(16 * 2 * INGOTS))
        .itemOutputs(NovaItemList.PreTesseract.get(64))
        .eut(RECIPE_UEV)
        .durSec(25)
        .addTo(ia)
    // PreTesseract v2
    builder()
        .itemInputs(
            ItemList.Tesseract.get(0),
            NovaItemList.AstriumInfinityGem.get(4),
            GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32105))
        .fluidInputs(MaterialsUEVplus.TranscendentMetal.getIngotMolten(256))
        .itemOutputs(GTUtility.copyAmountUnsafe(256, NovaItemList.PreTesseract.get(1)))
        .eut(RECIPE_UMV)
        .durSec(25)
        .addTo(ia)

    // endregion

    // region LENSES

    // Astrium MAGIC
    builder()
        .itemInputs(
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 32),
            NovaItemList.AstriumInfinityGem.get(16),
        )
        .fluidInputs(
            NovaMaterial.Astrium.getIngotMolten(32),
            Materials.Glass.getIngotMolten(128),
            Materials.Neutronium.getIngotMolten(64))
        .itemOutputs(NovaItemList.LensAstriumMagic.get(1))
        .eut(RECIPE_UEV)
        .durSec(120)
        .addTo(ia)
    // Astrium INF
    builder()
        .itemInputs(
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 32),
            NovaItemList.AstriumInfinityGem.get(32),
        )
        .fluidInputs(
            NovaMaterial.Astrium.getIngotMolten(128),
            Materials.Glass.getIngotMolten(1024),
            Materials.CosmicNeutronium.getIngotMolten(512))
        .itemOutputs(NovaItemList.LensAstriumInfinity.get(1))
        .eut(RECIPE_UEV)
        .durSec(120)
        .addTo(ia)
    // Or
    builder()
        .itemInputs(
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassLense", 64, 0),
            NovaMaterial.Originium.getDust(64),
        )
        .fluidInputs(
            NovaMaterial.Originium.getIngotMolten(128),
            Materials.Glass.getIngotMolten(64),
            GGMaterial.orundum.getIngotMolten(64))
        .itemOutputs(NovaItemList.LensOriginium.get(1))
        .eut(RECIPE_UHV)
        .durSec(120)
        .addTo(ia)
    // Pr
    builder()
        .itemInputs(
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassLense", 64, 0),
            NovaMaterial.Primoium.getDust(64),
        )
        .fluidInputs(
            NovaMaterial.Primoium.getIngotMolten(128),
            Materials.Glass.getIngotMolten(64),
            Materials.DraconiumAwakened.getIngotMolten(64))
        .itemOutputs(NovaItemList.LensPrimoium.get(1))
        .eut(RECIPE_UHV)
        .durSec(120)
        .addTo(ia)

    // endregion

    // region 电池配方
    // 兰波顿能量球 IV
    builder()
        .itemInputs(
            BartPart.DelicateBoard.getItemStack(1),
            BartPart.IC.getItemStack(4),
            BartPart.NanoCPU.getItemStack(2))
        .fluidInputs(
            SolderMaterial.SolderingAlloy.getFluidStack(16 * INGOTS),
            Materials.Platinum.getIngotMolten(160),
            NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(6400))
        .itemOutputs(ItemList.Energy_LapotronicOrb.get(16))
        .eut(RECIPE_EV)
        .duration(12 * (25 * SECOND + 12 * 20))
        .addTo(ia)
    // 兰波顿能量簇 LUV
    builder()
        .itemInputs(
            BartPart.DelicateBoard.getItemStack(1),
            GTUtility.copyAmountUnsafe(128, ItemList.Energy_LapotronicOrb.get(1)),
            BartPart.IC_H.getItemStack(4),
            BartPart.QBit.getItemStack(2))
        .fluidInputs(
            SolderMaterial.IndaAlloy.getFluidStack(16 * 5 * INGOTS),
            Materials.NiobiumTitanium.getIngotMolten(16 * 2),
            Materials.NaquadahAlloy.getIngotMolten(16 * 16))
        .itemOutputs(ItemList.Energy_LapotronicOrb2.get(16))
        .eut(RECIPE_IV)
        .duration(12 * (51 * SECOND + 4 * 20))
        .addTo(ia)
    builder()
        .itemInputs(
            BartPart.EliteBoard.getItemStack(1),
            BundleChip.LuV.getItemStack(4),
            BartPart.IC_H.getItemStack(64),
            BartPart.AdvancedDiode.getItemStack(8),
            BartPart.AdvancedCapacitor.getItemStack(8),
            BartPart.AdvancedResistor.getItemStack(8),
            BartPart.AdvancedTransistor.getItemStack(8))
        .fluidInputs(
            SolderMaterial.IndaAlloy.getFluidStack(16 * 5 * INGOTS),
            Materials.NaquadahAlloy.getIngotMolten(16 * 16),
            Materials.Platinum.getIngotMolten(16 * 8),
            NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(16 * 800))
        .itemOutputs(ItemList.Energy_LapotronicOrb2.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 50)
        .addTo(ia)
    // 能量模块 ZPM
    builder()
        .itemInputs(
            BundleChip.ZPM.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.Energy_LapotronicOrb2.get(1)),
            ItemList.Field_Generator_LuV.get(32),
            GTUtility.copyAmountUnsafe(6 * 64, BartPart.ASOC.getItemStack(1)),
            BartPart.AdvancedTransistor.getItemStack(8))
        .fluidInputs(
            SolderMaterial.IndaAlloy.getFluidStack(16 * 20 * INGOTS),
            Materials.Europium.getIngotMolten(16 * 16),
            Materials.Naquadah.getIngotMolten(16 * 16),
            FluidUtil.getFluidStack("ic2coolant", 16 * 16000))
        .itemOutputs(ItemList.Energy_Module.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 100)
        .addTo(ia)
    builder()
        .itemInputs(
            BartPart.WetwareBoard.getItemStack(1),
            BundleChip.ZPM.getItemStack(4),
            BartPart.IC_UH.getItemStack(64),
            BartPart.OpticalDiode.getItemStack(8),
            BartPart.OpticalCapacitor.getItemStack(8),
            BartPart.OpticalResistor.getItemStack(8),
            BartPart.OpticalTransistor.getItemStack(8))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 5 * INGOTS),
            Materials.Bedrockium.getIngotMolten(16 * 16),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * 6 * INGOTS),
            NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(16 * 1000))
        .itemOutputs(ItemList.Energy_Module.get(16))
        .eut(RECIPE_UV)
        .durSec(12 * 50)
        .addTo(ia)
    // 能量簇 UV
    builder()
        .itemInputs(
            BundleChip.UV.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.Energy_Module.get(1)),
            ItemList.Field_Generator_ZPM.get(32),
            BartPart.IC_H.getItemStack(256),
            BartPart.AdvancedDiode.getItemStack(16))
        .fluidInputs(
            SolderMaterial.IndaAlloy.getFluidStack(16 * 20 * INGOTS),
            Materials.Americium.getIngotMolten(16 * 16),
            Materials.NaquadahAlloy.getIngotMolten(16 * 16),
            FluidUtil.getFluidStack("ic2coolant", 16 * 16000))
        .itemOutputs(ItemList.Energy_Cluster.get(16))
        .eut(RECIPE_UV)
        .durSec(12 * 100)
        .addTo(ia)
    builder()
        .itemInputs(
            BartPart.BioBoard.getItemStack(1),
            BundleChip.UV.getItemStack(4),
            BartPart.IC_N.getItemStack(64),
            BartPart.OpticalDiode.getItemStack(32),
            BartPart.OpticalCapacitor.getItemStack(32),
            BartPart.OpticalResistor.getItemStack(32),
            BartPart.OpticalTransistor.getItemStack(32))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 10 * INGOTS),
            Materials.CosmicNeutronium.getIngotMolten(16 * 16),
            MaterialsUEVplus.SpaceTime.getIngotMolten(16 * 6),
            NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(16 * 2000))
        .itemOutputs(ItemList.Energy_Cluster.get(16))
        .eut(RECIPE_UHV)
        .durSec(12 * 50)
        .addTo(ia)
    // 终极电池 UV
    builder()
        .itemInputs(
            BundleChip.UHV.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.Energy_Cluster.get(1)),
            ItemList.Field_Generator_UV.get(32),
            BartPart.IC_H.getItemStack(256),
            BartPart.AdvancedDiode.getItemStack(32))
        .fluidInputs(
            SolderMaterial.IndaAlloy.getFluidStack(16 * 20 * INGOTS),
            Materials.Tritanium.getIngotMolten(16 * 64),
            NovaMaterial.SuperconductorFlux.getFluidOrGas(16 * 4 * INGOTS),
            FluidUtil.getFluidStack("ic2coolant", 16 * 16000))
        .itemOutputs(ItemList.ZPM2.get(16))
        .eut(RECIPE_UV)
        .durSec(12 * 150)
        .addTo(ia)
    builder()
        .itemInputs(
            BartPart.OpticalBoard.getItemStack(1),
            BundleChip.UHV.getItemStack(4),
            BartPart.IC_P.getItemStack(64),
            BartPart.OpticalDiode.getItemStack(64),
            BartPart.OpticalCapacitor.getItemStack(64),
            BartPart.OpticalResistor.getItemStack(64),
            BartPart.OpticalTransistor.getItemStack(64))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 20 * INGOTS),
            GGMaterial.shirabon.getIngotMolten(16 * 16),
            MaterialsUEVplus.WhiteDwarfMatter.getIngotMolten(16 * 4),
            MaterialsUEVplus.BlackDwarfMatter.getIngotMolten(16 * 4),
            MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getIngotMolten(8),
            NovaMaterial.LapotronEnhancedFluid.getFluidOrGas(16 * 5000))
        .itemOutputs(ItemList.ZPM2.get(16))
        .eut(RECIPE_UEV)
        .durSec(12 * 50)
        .addTo(ia)
    // 真·终极电池 UMV
    builder()
        .itemInputs(
            BundleChip.UEV.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.ZPM2.get(1)),
            ItemList.Field_Generator_UHV.get(64),
            BartPart.IC_UH.getItemStack(256),
            BartPart.ASOC.getItemStack(3 * 64),
            BartPart.AdvancedDiode.getItemStack(64))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 4608),
            Materials.Neutronium.getIngotMolten(16 * 128),
            Materials.Naquadria.getMolten(16 * 9216),
            NovaMaterial.SuperconductorFlux.getFluidOrGas(16 * 16 * INGOTS),
            FluidUtil.getFluidStack("ic2coolant", 16 * 32000))
        .itemOutputs(ItemList.ZPM3.get(16))
        .eut(RECIPE_UHV)
        .durSec(12 * 200)
        .addTo(ia)
    // 极·终极电池 UXV
    builder()
        .itemInputs(
            BundleChip.UIV.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.ZPM3.get(1)),
            ItemList.Field_Generator_UEV.get(64),
            BartPart.IC_P.getItemStack(256),
            BartPart.ASOC.getItemStack(6 * 64),
            BartPart.OpticalDiode.getItemStack(64))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 9216),
            Materials.InfinityCatalyst.getIngotMolten(16 * 128),
            Materials.Quantium.getMolten(16 * 18432),
            Materials.Naquadria.getMolten(16 * 18432),
            NovaMaterial.SuperconductorFlux.getFluidOrGas(16 * 64 * INGOTS),
            FluidUtil.getFluidStack("ic2coolant", 16 * 64000))
        .itemOutputs(ItemList.ZPM4.get(16))
        .eut(RECIPE_UEV)
        .durSec(12 * 250)
        .addTo(ia)
    // 狂·终极电池 MAX
    builder()
        .itemInputs(
            BundleChip.UMV.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.ZPM4.get(1)),
            ItemList.Field_Generator_UIV.get(64),
            BartPart.IC_Q.getItemStack(256),
            GTUtility.copyAmountUnsafe(
                16 * 64, GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.RawPicoWafer", 1)),
            BartPart.OpticalDiode.getItemStack(64),
            BartPart.OpticalInductor.getItemStack(32))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 18432),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * 128 * INGOTS),
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(18_432),
            Materials.Quantium.getMolten(16 * 18432),
            NovaMaterial.SuperconductorFlux.getFluidOrGas(16 * 128 * INGOTS),
            FluidUtil.getFluidStack("ic2coolant", 16 * 128000))
        .itemOutputs(ItemList.ZPM5.get(16))
        .eut(RECIPE_UIV)
        .durSec(12 * 300)
        .addTo(ia)
    // 太·终极电池 ER
    builder()
        .itemInputs(
            BundleChip.UXV.getItemStack(4),
            GTUtility.copyAmountUnsafe(128, ItemList.ZPM5.get(1)),
            ItemList.Field_Generator_UMV.get(64),
            BartPart.IC_Q.getItemStack(256),
            GTUtility.copyAmountUnsafe(
                16 * 64, GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.RawPicoWafer", 1)),
            BartPart.OpticalDiode.getItemStack(64),
            BartPart.OpticalInductor.getItemStack(64))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 36864),
            MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(16 * 128 * INGOTS),
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(16 * 36864),
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(16 * 36864),
            NovaMaterial.SuperconductorFlux.getFluidOrGas(16 * 256 * INGOTS),
            FluidUtil.getFluidStack("ic2coolant", 16 * 256000))
        .itemOutputs(ItemList.ZPM6.get(16))
        .eut(RECIPE_UMV)
        .durSec(12 * 350)
        .addTo(ia)
    // endregion

    // region 防辐射板
    // 防辐射板 64x
    builder()
        .itemInputs(
            ItemUtil.setStackSize(Materials.NaquadahAlloy.getPlates(64), 8 * 64),
            ItemUtil.setStackSize(Materials.Lanthanum.getPlates(64), 4 * 64))
        .fluidInputs(Materials.Lead.getIngotMolten(64 * 8))
        .itemOutputs(ItemRefer.Radiation_Protection_Plate.get(64))
        .eut(RECIPE_EV)
        .durSec(12 * 80)
        .addTo(ia)
    // 进阶防辐射板 64x
    builder()
        .itemInputs(
            ItemUtil.setStackSize(Materials.ElectrumFlux.getPlates(64), 256),
            ItemUtil.setStackSize(Materials.Trinium.getPlates(64), 256),
            ItemUtil.setStackSize(Materials.Osmiridium.getPlates(64), 256),
            ItemUtil.setStackSize(Materials.VibrantAlloy.getPlates(64), 256))
        .fluidInputs(
            SolderMaterial.IndaAlloy.getFluidStack(16 * 32 * INGOTS),
            Materials.Lead.getIngotMolten(32 * 32),
            Materials.NaquadahAlloy.getIngotMolten(1280),
            Materials.Lanthanum.getIngotMolten(512))
        .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(64))
        .eut(RECIPE_ZPM)
        .durSec(24 * 100)
        .addTo(ia)
    // endregion

    // region 戴森球
    if (Mods.GTNHIntergalactic.isModLoaded) {
      // 耐高温网 1024
      builder()
          .itemInputs(
              ItemUtil.setStackSize(
                  GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Carbon, 1), 128),
              ItemUtil.setStackSize(
                  GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 1), 512),
              ItemUtil.setStackSize(
                  GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.TungstenCarbide, 1), 512),
              ItemUtil.setStackSize(
                  GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tantalum, 1), 512))
          .fluidInputs(
              SolderMaterial.IndaAlloy.getFluidStack(128 * INGOTS),
              MaterialsAlloy.SILICON_CARBIDE.getFluidStack(48 * INGOTS))
          .itemOutputs(
              ItemUtil.setStackSize(ItemUtil.getItemStack(IGItems.DysonSwarmItems, 1, 3), 1024))
          .eut(RECIPE_LuV)
          .durSec(200)
          .addTo(ia)
      // 戴森球模块 512 8x
      builder()
          .itemInputs(
              ItemList.Cover_SolarPanel_UV.get(1),
              ItemUtil.getItemStack(IGItems.DysonSwarmItems, 32, 3),
              ItemRefer.Radiation_Protection_Plate.get(4),
              BartPart.IC_Q.getItemStack(1),
              BundleChip.UHV.getItemStack(1),
              ItemList.Emitter_UEV.get(4),
              ItemList.Sensor_UEV.get(4))
          .fluidInputs(SolderMaterial.MutatedLivingAlloy.getFluidStack(256 * INGOTS))
          .itemOutputs(
              GTUtility.copyAmountUnsafe(
                  8 * 64, ItemUtil.getItemStack(IGItems.DysonSwarmItems, 1, 0)))
          .eut(RECIPE_UEV)
          .durSec(5)
          .addTo(ia)
    }
    // endregion

    // region Optical
    // Optical RAM
    builder()
        .itemInputs(
            BundleChip.LuV.getItemStack(4),
            BartPart.EliteBoard.getItemStack(4),
            BartPart.OpticalCard.getItemStack(4),
            GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 32, 5),
            BartPart.RAM.getItemStack(64),
            BartPart.SOC.getItemStack(64),
            BartPart.NAND.getItemStack(64),
            GTUtility.copyAmountUnsafe(
                128, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 15470)))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 48 * INGOTS),
            Materials.VanadiumGallium.getIngotMolten(16 * 16),
            Materials.Infinity.getIngotMolten(16 * 32))
        .itemOutputs(GTUtility.copyAmountUnsafe(2048, ItemList.Optically_Compatible_Memory.get(1)))
        .eut(RECIPE_UEV)
        .durSec(12 * 80)
        .addTo(ia)
    // Optical CPU
    builder()
        .itemInputs(
            BartPart.OpticalCard.getItemStack(8),
            BartPart.OpticalCPUCasing.getItemStack(8),
            BartPart.OpticalCard.getItemStack(4),
            GTUtility.copyAmountUnsafe(
                128, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 15470)),
            GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 32),
            GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Draconium, 32))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(48 * INGOTS),
            Materials.CosmicNeutronium.getIngotMolten(64))
        .itemOutputs(GTUtility.copyAmountUnsafe(128, ItemList.Optically_Perfected_CPU.get(1)))
        .eut(RECIPE_UEV)
        .durSec(120)
        .addTo(ia)
    // endregion

    // region Coil Misc
    // SC Coil
    builder()
        .itemInputs(
            *Array(8) { GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Trinium, 64) },
            NovaItemList.DenseMicaInsulatorFoil.get(64),
        )
        .fluidInputs(NovaMaterial.SuperconductorFlux.getFluidOrGas(64 * INGOTS))
        .itemOutputs(ItemList.Casing_Coil_Superconductor.get(64))
        .eut(RECIPE_LuV)
        .durSec(12 * 40)
        .addTo(ia)
    // Adv SC Coil (GG)
    builder()
        .itemInputs(ItemList.Casing_Coil_Superconductor.get(48), ItemRefer.HiC_T2.get(16))
        .fluidInputs(
            GGMaterial.marM200.getMolten(16 * 8 * INGOTS),
            GGMaterial.zircaloy4.getMolten(26 * 2 * INGOTS),
            Materials.Aluminium.getIngotMolten(24))
        .itemOutputs(ItemRefer.Compact_Fusion_Coil_T0.get(16))
        .eut(RECIPE_LuV)
        .durSec(12 * 60)
        .addTo(ia)
    // endregion

    // region Coil UHV+
    // Infinity
    var micaStack = BaseRecipeLoader.getItemContainer("MicaInsulatorFoil").get(64)
    var micaStackHalf = getCoreItem("MicaInsulatorFoil", 32)
    builder()
        .itemInputs(
            GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UHV), 1),
            GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Infinity, 8),
            GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 8),
            micaStack,
            micaStackHalf,
        )
        .fluidInputs(Materials.DraconiumAwakened.getIngotMolten(4))
        .itemOutputs(ItemList.Casing_Coil_Infinity.get(1))
        .eut(8000000)
        .durSec(60)
        .addTo(ia)
    builder()
        .itemInputs(
            BundleChip.UHV.getItemStack(1),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(32))
        .fluidInputs(
            Materials.DraconiumAwakened.getIngotMolten(16 * 4),
            Materials.Infinity.getIngotMolten(16 * 9))
        .itemOutputs(ItemList.Casing_Coil_Infinity.get(16))
        .eut(8000000)
        .durSec(12 * 60)
        .addTo(ia)
    // Hypogen
    builder()
        .itemInputs(
            GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UEV), 1),
            MaterialsElements.STANDALONE.HYPOGEN.getWire02(8),
            MaterialsElements.STANDALONE.HYPOGEN.getScrew(8),
            micaStack,
            micaStack,
            micaStack)
        .fluidInputs(Materials.Infinity.getIngotMolten(4))
        .itemOutputs(ItemList.Casing_Coil_Hypogen.get(1))
        .eut(8000000 * 4)
        .durSec(60)
        .addTo(ia)
    builder()
        .itemInputs(
            BundleChip.UEV.getItemStack(1),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64))
        .fluidInputs(
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * 9 * INGOTS),
            Materials.Infinity.getIngotMolten(16 * 4))
        .itemOutputs(ItemList.Casing_Coil_Hypogen.get(16))
        .eut(8000000 * 4)
        .durSec(12 * 60)
        .addTo(ia)
    // Eternal
    builder()
        .itemInputs(
            GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UIV), 1),
            GTOreDictUnificator.get(OrePrefixes.wireGt02, MaterialsUEVplus.SpaceTime, 8),
            GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 8),
            *Array(6) { micaStack })
        .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(4 * INGOTS))
        .itemOutputs(ItemList.Casing_Coil_Eternal.get(1))
        .eut(8000000 * 16)
        .durSec(60)
        .addTo(ia)
    builder()
        .itemInputs(
            BundleChip.UIV.getItemStack(1),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64),
            NovaItemList.DenseMicaInsulatorFoil.get(64))
        .fluidInputs(
            MaterialsUEVplus.SpaceTime.getIngotMolten(16 * 9),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * 4 * INGOTS))
        .itemOutputs(ItemList.Casing_Coil_Eternal.get(16))
        .eut(8000000 * 16)
        .durSec(12 * 60)
        .addTo(ia)
    // endregion

    // region Coil Fusion
    // Casing Fusion Coil
    builder()
        .itemInputs(
            ItemList.Casing_Coil_Superconductor.get(16),
            BundleChip.LuV.getItemStack(4),
            ItemList.Field_Generator_MV.get(32))
        .fluidInputs(
            Materials.Iridium.getIngotMolten(12 * 8),
            Materials.Tin.getIngotMolten(16 * 64),
            Materials.TungstenCarbide.getIngotMolten(8 * 72),
            Materials.Beryllium.getIngotMolten(8 * 72))
        .itemOutputs(ItemList.Casing_Fusion_Coil.get(16))
        .eut(RECIPE_LuV)
        .durSec(400)
        .addTo(ia)
    // Advanced Casing Fusion Coil
    builder()
        .itemInputs(
            ItemList.Casing_Fusion_Coil.get(16),
            GTUtility.copyAmountUnsafe(16 * 16, ItemList.Energy_LapotronicOrb2.get(1)),
            BundleChip.LuV.getItemStack(16),
            BundleChip.UV.getItemStack(8),
            ItemList.Emitter_UHV.get(16),
            ItemList.Sensor_UHV.get(16))
        .fluidInputs(
            MaterialsAlloy.CINOBITE.getFluidStack(16 * 16 * INGOTS),
            MaterialsAlloy.OCTIRON.getFluidStack(16 * 16 * INGOTS),
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(16 * 16 * INGOTS),
            Materials.UUMatter.getBucketFluid(16 * 8),
            Materials.Neutronium.getIngotMolten(16 * 8))
        .itemOutputs(GregtechItemList.Casing_Fusion_Internal.get(16))
        .eut(RECIPE_UHV)
        .durSec(12 * 16)
        .addTo(ia)
    // Advanced Casing Fusion Coil II
    builder()
        .itemInputs(
            ItemRefer.Compact_Fusion_Coil_T2.get(16),
            ItemList.Casing_Fusion_Coil.get(16),
            GTUtility.copyAmountUnsafe(16 * 16, ItemList.Energy_Module.get(1)),
            BundleChip.ZPM.getItemStack(16),
            BundleChip.UHV.getItemStack(8),
            ItemList.Emitter_UHV.get(16),
            ItemList.Sensor_UHV.get(16))
        .fluidInputs(
            MaterialsElements.getInstance().NEPTUNIUM.getFluidStack(16 * 16 * INGOTS),
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(16 * 16 * INGOTS),
            MaterialsAlloy.ABYSSAL.getFluidStack(16 * 16 * INGOTS),
            MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(16 * 16 * INGOTS),
            MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(16 * 8 * INGOTS))
        .itemOutputs(GregtechItemList.Casing_Fusion_Internal2.get(16))
        .eut(RECIPE_UEV)
        .durSec(12 * 60)
        .addTo(ia)

    // Compact Casing Fusion Coil
    builder()
        .itemInputs(
            ItemList.Casing_Fusion_Coil.get(48),
            ItemRefer.HiC_T3.get(16),
            BartPart.ILC.getItemStack(8),
            BartPart.IC.getItemStack(1),
            ItemList.IV_Coil.get(1))
        .fluidInputs(
            GGMaterial.artheriumSn.getMolten(16 * 4 * INGOTS),
            GGMaterial.titaniumBetaC.getMolten(16 * INGOTS),
            Materials.EnergeticAlloy.getIngotMolten(16),
            Materials.Aluminium.getIngotMolten(16),
            Materials.Silver.getIngotMolten(8))
        .itemOutputs(ItemRefer.Compact_Fusion_Coil_T1.get(16))
        .eut(RECIPE_LuV)
        .durSec(12 * 40)
        .addTo(ia)
    // Compact Casing Fusion Coil Adv
    builder()
        .itemInputs(
            ItemList.Casing_Fusion_Coil.get(48),
            ItemRefer.HiC_T5.get(16),
            ItemRefer.Advanced_Radiation_Protection_Plate.get(32),
            ItemList.NuclearStar.get(4))
        .fluidInputs(
            GGMaterial.dalisenite.getMolten(16 * 4 * INGOTS),
            GGMaterial.hikarium.getMolten(16 * INGOTS))
        .itemOutputs(ItemRefer.Compact_Fusion_Coil_T2.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 40)
        .addTo(ia)
    // Compact Casing Fusion Coil II Prototype
    builder()
        .itemInputs(
            GregtechItemList.Casing_Fusion_Internal.get(48),
            ItemRefer.HiC_T5.get(16),
            GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore4", 16))
        .fluidInputs(
            MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(16 * 8 * INGOTS),
            MaterialsAlloy.LAURENIUM.getFluidStack(16 * INGOTS))
        .itemOutputs(ItemRefer.Compact_Fusion_Coil_T3.get(16))
        .eut(RECIPE_UV)
        .durSec(12 * 100)
        .addTo(ia)
    // Compact Casing Fusion Coil II
    builder()
        .itemInputs(
            GregtechItemList.Casing_Fusion_Internal2.get(48),
            ItemRefer.HiC_T5.get(64),
            BundleChip.UEV.getItemStack(1),
            GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore5", 16))
        .fluidInputs(
            MaterialsAlloy.BLACK_TITANIUM.getFluidStack(16 * 8 * INGOTS),
            GGMaterial.metastableOganesson.getMolten(16 * 4 * INGOTS))
        .itemOutputs(ItemRefer.Compact_Fusion_Coil_T4.get(16))
        .eut(RECIPE_UHV)
        .durSec(12 * 100)
        .addTo(ia)
    // endregion

    // region Casing Fusion
    // Compact Casing Fusion Coil III
    builder()
        .itemInputs(
            ItemList.Casing_Fusion2.get(16),
            BundleChip.EV.getItemStack(16),
            BundleChip.IV.getItemStack(8),
            ItemList.Electric_Motor_UHV.get(32),
            ItemList.Electric_Piston_UHV.get(16))
        .fluidInputs(
            Materials.TungstenCarbide.getIngotMolten(16 * 8 * 9),
            Materials.Neutronium.getIngotMolten(16 * 8),
            Materials.UUMatter.getBucketFluid(16),
            MaterialsAlloy.CINOBITE.getFluidStack(16 * 4 * INGOTS),
            MaterialsAlloy.OCTIRON.getFluidStack(16 * 4 * INGOTS),
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(16 * 4 * INGOTS))
        .itemOutputs(GregtechItemList.Casing_Fusion_External.get(16))
        .eut(RECIPE_UHV)
        .durSec(12 * 15)
        .addTo(ia)
    // Compact Casing Fusion Coil IV
    builder()
        .itemInputs(
            GregtechItemList.Casing_Fusion_External.get(16),
            BundleChip.IV.getItemStack(16),
            BundleChip.LuV.getItemStack(8),
            ItemList.Electric_Motor_UEV.get(32),
            ItemList.Electric_Piston_UEV.get(16))
        .fluidInputs(
            Materials.NaquadahAlloy.getIngotMolten(16 * 8 * 9),
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(16 * 16 * INGOTS),
            MaterialsElements.getInstance().FERMIUM.getFluidStack(16 * 8 * INGOTS),
            MaterialsAlloy.ABYSSAL.getFluidStack(16 * 8 * INGOTS),
            MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(16 * 8 * INGOTS))
        .itemOutputs(GregtechItemList.Casing_Fusion_External2.get(16))
        .eut(RECIPE_UEV)
        .durSec(12 * 15)
        .addTo(ia)
    // endregion

    // region Casing Fusion
    // FRF Coil I
    builder()
        .itemInputs(
            Tier.UHV.getCircuitWrap(1),
            Tier.UV.getComponent(Tier.Component.FieldGenerator, 32),
            ItemUtil.setStackSize(Tier.UV.getComponent(Tier.Component.ElectricPump, 1), 128),
            ItemUtil.setStackSize(ItemList.Circuit_Wafer_PPIC.get(1), 32 * 16))
        .fluidInputs(
            SuperConductorPart.ZPM.getSxEqualFluid(128 * 16),
            Materials.Americium.getIngotMolten(8 * 9 * 16),
            Materials.BlackPlutonium.getIngotMolten(16 * 3 * 16),
            Materials.Osmium.getIngotMolten(2 * 16),
            Materials.ElectrumFlux.getIngotMolten(64 * 16),
            FluidUtil.getFluidStack("lubricant", 128000 * 16))
        .itemOutputs(ItemRefer.Field_Restriction_Coil_T1.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 900)
        .addTo(ia)
    // FRF Coil II
    builder()
        .itemInputs(
            Tier.UEV.getCircuitWrap(1),
            Tier.UHV.getComponent(Tier.Component.FieldGenerator, 32),
            ItemUtil.setStackSize(Tier.UHV.getComponent(Tier.Component.ElectricPump, 1), 128),
            ItemUtil.setStackSize(ItemList.Circuit_Wafer_PPIC.get(1), 48 * 16))
        .fluidInputs(
            SuperConductorPart.UV.getSxEqualFluid(2 * 128 * 16),
            Materials.Infinity.getIngotMolten(8 * 9 * 16),
            Materials.Neutronium.getIngotMolten(16 * 3 * 16),
            Materials.CosmicNeutronium.getIngotMolten(2 * 16),
            Materials.DraconiumAwakened.getIngotMolten(64 * 16),
            FluidUtil.getFluidStack("lubricant", 128000 * 16))
        .itemOutputs(ItemRefer.Field_Restriction_Coil_T2.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 1800)
        .addTo(ia)
    // FRF Coil III
    builder()
        .itemInputs(
            Tier.UIV.getCircuitWrap(1),
            Tier.UEV.getComponent(Tier.Component.FieldGenerator, 32),
            ItemUtil.setStackSize(Tier.UEV.getComponent(Tier.Component.ElectricPump, 1), 128),
            ItemUtil.setStackSize(ItemList.Circuit_Wafer_PPIC.get(1), 64 * 16))
        .fluidInputs(
            SuperConductorPart.UHV.getSxEqualFluid(4 * 128 * 16),
            MaterialsUEVplus.TranscendentMetal.getIngotMolten(8 * 9 * 16),
            Materials.Infinity.getIngotMolten((16 * 3 + 2) * 16),
            Materials.Neutronium.getIngotMolten(64 * 16),
            FluidUtil.getFluidStack("lubricant", 128000 * 16))
        .itemOutputs(ItemRefer.Field_Restriction_Coil_T3.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 3600)
        .addTo(ia)
    // FRF Coil IV
    builder()
        .itemInputs(
            Tier.UMV.getCircuitWrap(1),
            Tier.UIV.getComponent(Tier.Component.FieldGenerator, 32),
            ItemUtil.setStackSize(Tier.UIV.getComponent(Tier.Component.ElectricPump, 1), 128),
            ItemUtil.setStackSize(ItemList.Circuit_Wafer_PPIC.get(1), 64 * 16))
        .fluidInputs(
            SuperConductorPart.UEV.getSxEqualFluid(4 * 128 * 16),
            MaterialsUEVplus.SpaceTime.getIngotMolten((8 * 9 + 16 * 3 + 2) * 16),
            MaterialsUEVplus.TranscendentMetal.getIngotMolten(64 * 16),
            FluidUtil.getFluidStack("lubricant", 128000 * 16))
        .itemOutputs(ItemRefer.Field_Restriction_Coil_T4.get(16))
        .eut(RECIPE_ZPM)
        .durSec(12 * 7200)
        .addTo(ia)
    // endregion

    // region QFT
    // 脉冲T1
    builder()
        .itemInputs(
            GregtechItemList.ForceFieldGlass.get(16),
            ItemUtil.setStackSize(Materials.Carbon.getNanite(1), 4 * 16),
            ItemUtil.setStackSize(Tier.UV.getComponent(Tier.Component.Emitter, 1), 4 * 16),
            GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32105),
            ItemRefer.Advanced_Radiation_Protection_Plate.get(2 * 16))
        .fluidInputs(
            Materials.Thulium.getIngotMolten(10 * 16),
            SuperConductorPart.UHV.getMolten(8 * 8 * INGOTS))
        .itemOutputs(GregtechItemList.NeutronPulseManipulator.get(16))
        .eut(RECIPE_UEV)
        .durSec(12 * 60)
        .addTo(ia)
    // 脉冲T2
    builder()
        .itemInputs(
            GregtechItemList.ForceFieldGlass.get(2 * 16),
            ItemUtil.setStackSize(Materials.Carbon.getNanite(1), 8 * 16),
            ItemUtil.setStackSize(Tier.UEV.getComponent(Tier.Component.Emitter, 1), 4 * 16),
            GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32105),
            ItemRefer.Advanced_Radiation_Protection_Plate.get(4 * 16))
        .fluidInputs(
            Materials.Thulium.getIngotMolten(12 * 16),
            SuperConductorPart.UEV.getMolten(8 * 8 * INGOTS))
        .itemOutputs(GregtechItemList.CosmicFabricManipulator.get(16))
        .eut(RECIPE_UIV)
        .durSec(12 * 75)
        .addTo(ia)
    // 脉冲T3
    builder()
        .itemInputs(
            GregtechItemList.ForceFieldGlass.get(4 * 16),
            ItemUtil.setStackSize(Materials.Carbon.getNanite(1), 16 * 16),
            ItemUtil.setStackSize(Tier.UIV.getComponent(Tier.Component.Emitter, 1), 4 * 16),
            GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32105),
            ItemUtil.setStackSize(ItemRefer.Advanced_Radiation_Protection_Plate.get(1), 8 * 16))
        .fluidInputs(
            Materials.Thulium.getIngotMolten(15 * 16),
            SuperConductorPart.UIV.getMolten(8 * 8 * INGOTS))
        .itemOutputs(GregtechItemList.InfinityInfusedManipulator.get(16))
        .eut(RECIPE_UMV)
        .durSec(12 * 90)
        .addTo(ia)
    // 脉冲T4
    builder()
        .itemInputs(
            ItemUtil.setStackSize(GregtechItemList.ForceFieldGlass.get(1), 8 * 16),
            ItemUtil.setStackSize(Materials.Carbon.getNanite(1), 32 * 16),
            ItemUtil.setStackSize(Tier.UMV.getComponent(Tier.Component.Emitter, 1), 4 * 16),
            GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32105),
            ItemUtil.setStackSize(ItemRefer.Advanced_Radiation_Protection_Plate.get(1), 16 * 16))
        .fluidInputs(
            Materials.Thulium.getIngotMolten(20 * 16),
            SuperConductorPart.UMV.getMolten(8 * 8 * INGOTS))
        .itemOutputs(GregtechItemList.SpaceTimeContinuumRipper.get(16))
        .eut(RECIPE_UXV)
        .durSec(12 * 60)
        .addTo(ia)
    // 核心T1
    builder()
        .itemInputs(
            MaterialsAlloy.QUANTUM.getFrameBox(16),
            ItemUtil.setStackSize(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 1), 16 * 16),
            Tier.UV.getComponent(Tier.Component.FieldGenerator, 16))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 10 * INGOTS),
            GGMaterial.preciousMetalAlloy.getIngotMolten(16 * 4 * 9),
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(16 * 2 * INGOTS))
        .itemOutputs(GregtechItemList.NeutronShieldingCore.get(16))
        .eut(RECIPE_UEV)
        .durSec(12 * 60)
        .addTo(ia)
    // 核心T2
    builder()
        .itemInputs(
            MaterialsAlloy.QUANTUM.getFrameBox(16 * 2),
            ItemUtil.setStackSize(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1), 16 * 16),
            Tier.UEV.getComponent(Tier.Component.FieldGenerator, 16))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 20 * INGOTS),
            GGMaterial.enrichedNaquadahAlloy.getIngotMolten(16 * 4 * 9),
            FluidUtil.getFluidStack("molten.radoxpoly", 16 * 2 * INGOTS))
        .itemOutputs(GregtechItemList.CosmicFabricShieldingCore.get(16))
        .eut(RECIPE_UIV)
        .durSec(12 * 75)
        .addTo(ia)
    // 核心T3
    builder()
        .itemInputs(
            MaterialsAlloy.QUANTUM.getFrameBox(16 * 4),
            ItemUtil.setStackSize(
                GTOreDictUnificator.get(
                    OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 1),
                16 * 16),
            Tier.UIV.getComponent(Tier.Component.FieldGenerator, 16))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 40 * INGOTS),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(16 * 4 * 9 * INGOTS),
            GGMaterial.metastableOganesson.getMolten(16 * 2 * INGOTS))
        .itemOutputs(GregtechItemList.InfinityInfusedShieldingCore.get(16))
        .eut(RECIPE_UMV)
        .durSec(12 * 90)
        .addTo(ia)
    // 核心T4
    builder()
        .itemInputs(
            ItemUtil.setStackSize(MaterialsAlloy.QUANTUM.getFrameBox(1), 16 * 8),
            ItemUtil.setStackSize(
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 1),
                16 * 16),
            Tier.UMV.getComponent(Tier.Component.FieldGenerator, 16))
        .fluidInputs(
            SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 40 * INGOTS),
            GGMaterial.shirabon.getMolten(16 * 4 * 9 * INGOTS),
            // Materials.Dilithium.getIngotMolten(16 * 2),
            Materials.Lithium.getIngotMolten(32 * 2))
        .itemOutputs(GregtechItemList.SpaceTimeBendingCore.get(16))
        .eut(RECIPE_UXV)
        .durSec(12 * 120)
        .addTo(ia)
    // endregion

    // region AE2
    // 奇点存储元件
    builder()
        .itemInputs(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 12, 60),
            ItemList.Quantum_Chest_IV.get(8),
            GTModHandler.getModItem(Avaritia.ID, "Resource", 4, 5),
            GTModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 1))
        .fluidInputs(
            Materials.CosmicNeutronium.getIngotMolten(12 * 9),
            Materials.Infinity.getIngotMolten(4 * 9))
        .itemOutputs(
            GTModHandler.getModItem(
                AppliedEnergistics2.ID, "item.ItemExtremeStorageCell.Singularity", 1))
        .eut(RECIPE_UEV)
        .durSec(4)
        .addTo(ia)
    // 奇点流体元件
    builder()
        .itemInputs(
            GTModHandler.getModItem(AE2FluidCraft.ID, "fluid_part", 8, 7),
            ItemList.Quantum_Tank_IV.get(8),
            BasicRef.getYOTCell(7, 4),
            Avaritia.getItem("Resource", 4, 5),
            BasicRef.getSingularity(1))
        .fluidInputs(
            Materials.CosmicNeutronium.getIngotMolten(12 * 9),
            Materials.Infinity.getIngotMolten(4 * 9))
        .itemOutputs(GTModHandler.getModItem(AE2FluidCraft.ID, "fluid_storage.singularity", 1))
        .eut(RECIPE_UEV)
        .durSec(4)
        .addTo(ia)
    // 量子存储元件
    builder()
        .itemInputs(
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 61),
            GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8, 60),
            Tier.UHV.getCircuit(4))
        .fluidInputs(
            Materials.CosmicNeutronium.getIngotMolten(4 * 9),
            Materials.Neutronium.getIngotMolten(8 * 9))
        .itemOutputs(
            GTModHandler.getModItem(
                AppliedEnergistics2.ID, "item.ItemExtremeStorageCell.Quantum", 1))
        .eut(RECIPE_UV)
        .durSec(4)
        .addTo(ia)
    // 量子流体元件
    builder()
        .itemInputs(
            GTModHandler.getModItem(AE2FluidCraft.ID, "fluid_storage_housing", 1, 1),
            GTModHandler.getModItem(AE2FluidCraft.ID, "fluid_part", 8, 7),
            Tier.UHV.getCircuit(4))
        .fluidInputs(
            Materials.CosmicNeutronium.getIngotMolten(4 * 9),
            Materials.Neutronium.getIngotMolten(8 * 9))
        .itemOutputs(GTModHandler.getModItem(AE2FluidCraft.ID, "fluid_storage.quantum", 1))
        .eut(RECIPE_UV)
        .durSec(4)
        .addTo(ia)
    // endregion

    // region Solar Panel
    // UV
    builder()
        .itemInputs(
            ItemUtil.setStackSize(getCoreItem("PicoWafer", 1), 4 * 64),
            ItemUtil.setStackSize(getCoreItem("RawPicoWafer", 1), 2 * 64),
            BundleChip.UEV.getItemStack(8),
            BundleChip.UHV.getItemStack(8),
            ItemUtil.setStackSize(Materials.Carbon.getPlates(1), 1024),
            BartPart.IC_P.getItemStack(16),
            GTModHandler.getModItem(SuperSolarPanels.ID, "solarsplitter", 8, 0),
            NovaItemList.CrystalMatrix.get(2))
        .fluidInputs(
            Materials.SiliconSG.getIngotMolten(64 * 12 * 9),
            SuperConductorPart.UHV.getMolten(12 * 64 * INGOTS),
            Materials.ReinforceGlass.getIngotMolten(4 * 64),
            Materials.Neutronium.getIngotMolten(256))
        .itemOutputs(ItemList.Cover_SolarPanel_UV.get(64))
        .eut(RECIPE_UIV)
        .durSec(32 * 8)
        .addTo(ia)
    // endregion
  }

  private fun loadTstRecipe() {
    // Particle Trap SpaceTime Shield
    builder()
        .itemInputs(
            BartPart.OpticalCPUCasing.getItemStack(2),
            NovaItemList.PreTesseract.get(8),
            NovaItemList.AstriumInfinityGem.get(1))
        .fluidInputs(
            NovaMaterial.AstriumMagic.getMolten(8 * INGOTS),
            MaterialsUEVplus.SpaceTime.getIngotMolten(4))
        .itemOutputs(GTCMItemList.ParticleTrapTimeSpaceShield.get(64))
        .noOptimize()
        .eut(RECIPE_UIV)
        .durSec(60)
        .addTo(ia)
  }

  private fun loadWirelessHatchRecipe() {
    val recipeDuration = 20 * SECOND
    val recipeEU = 128_000_000

    val energyHatches =
        arrayOf(
            ItemList.Hatch_Energy_ULV.get(1),
            ItemList.Hatch_Energy_LV.get(1),
            ItemList.Hatch_Energy_MV.get(1),
            ItemList.Hatch_Energy_HV.get(1),
            ItemList.Hatch_Energy_EV.get(1),
            ItemList.Hatch_Energy_IV.get(1),
            ItemList.Hatch_Energy_LuV.get(1),
            ItemList.Hatch_Energy_ZPM.get(1),
            ItemList.Hatch_Energy_UV.get(1),
            ItemList.Hatch_Energy_UHV.get(1),
            ItemList.Hatch_Energy_UEV.get(1),
            ItemList.Hatch_Energy_UIV.get(1),
            ItemList.Hatch_Energy_UMV.get(1),
            ItemList.Hatch_Energy_UXV.get(1))

    val energyHatches4A =
        arrayOf(
            CustomItemList.eM_energyMulti4_EV.get(1),
            CustomItemList.eM_energyMulti4_IV.get(1),
            CustomItemList.eM_energyMulti4_LuV.get(1),
            CustomItemList.eM_energyMulti4_ZPM.get(1),
            CustomItemList.eM_energyMulti4_UV.get(1),
            CustomItemList.eM_energyMulti4_UHV.get(1),
            CustomItemList.eM_energyMulti4_UEV.get(1),
            CustomItemList.eM_energyMulti4_UIV.get(1),
            CustomItemList.eM_energyMulti4_UMV.get(1),
            CustomItemList.eM_energyMulti4_UXV.get(1))

    val energyHatches16A =
        arrayOf(
            CustomItemList.eM_energyMulti16_EV.get(1),
            CustomItemList.eM_energyMulti16_IV.get(1),
            CustomItemList.eM_energyMulti16_LuV.get(1),
            CustomItemList.eM_energyMulti16_ZPM.get(1),
            CustomItemList.eM_energyMulti16_UV.get(1),
            CustomItemList.eM_energyMulti16_UHV.get(1),
            CustomItemList.eM_energyMulti16_UEV.get(1),
            CustomItemList.eM_energyMulti16_UIV.get(1),
            CustomItemList.eM_energyMulti16_UMV.get(1),
            CustomItemList.eM_energyMulti16_UXV.get(1))

    val energyHatches64A =
        arrayOf(
            CustomItemList.eM_energyMulti64_EV.get(1),
            CustomItemList.eM_energyMulti64_IV.get(1),
            CustomItemList.eM_energyMulti64_LuV.get(1),
            CustomItemList.eM_energyMulti64_ZPM.get(1),
            CustomItemList.eM_energyMulti64_UV.get(1),
            CustomItemList.eM_energyMulti64_UHV.get(1),
            CustomItemList.eM_energyMulti64_UEV.get(1),
            CustomItemList.eM_energyMulti64_UIV.get(1),
            CustomItemList.eM_energyMulti64_UMV.get(1),
            CustomItemList.eM_energyMulti64_UXV.get(1))

    val laserTargetsUXV =
        arrayOf(
            CustomItemList.eM_energyTunnel1_UXV.get(1),
            CustomItemList.eM_energyTunnel2_UXV.get(1),
            CustomItemList.eM_energyTunnel3_UXV.get(1),
            CustomItemList.eM_energyTunnel4_UXV.get(1),
            CustomItemList.eM_energyTunnel5_UXV.get(1),
            CustomItemList.eM_energyTunnel6_UXV.get(1),
            CustomItemList.eM_energyTunnel7_UXV.get(1))

    val dynamoHatches =
        arrayOf(
            ItemList.Hatch_Dynamo_ULV.get(1),
            ItemList.Hatch_Dynamo_LV.get(1),
            ItemList.Hatch_Dynamo_MV.get(1),
            ItemList.Hatch_Dynamo_HV.get(1),
            ItemList.Hatch_Dynamo_EV.get(1),
            ItemList.Hatch_Dynamo_IV.get(1),
            ItemList.Hatch_Dynamo_LuV.get(1),
            ItemList.Hatch_Dynamo_ZPM.get(1),
            ItemList.Hatch_Dynamo_UV.get(1),
            ItemList.Hatch_Dynamo_UHV.get(1),
            ItemList.Hatch_Dynamo_UEV.get(1),
            ItemList.Hatch_Dynamo_UIV.get(1),
            ItemList.Hatch_Dynamo_UMV.get(1),
            ItemList.Hatch_Dynamo_UXV.get(1),
        )

    val circuitsTierPlusTwo =
        arrayOf(
            arrayOf(OrePrefixes.circuit.get(Materials.MV), 1L), // MV
            arrayOf(OrePrefixes.circuit.get(Materials.HV), 1L), // HV
            arrayOf(OrePrefixes.circuit.get(Materials.EV), 1L), // EV
            arrayOf(OrePrefixes.circuit.get(Materials.IV), 1L), // IV
            arrayOf(OrePrefixes.circuit.get(Materials.LuV), 1L), // LuV
            arrayOf(OrePrefixes.circuit.get(Materials.ZPM), 1L), // ZPM
            arrayOf(OrePrefixes.circuit.get(Materials.UV), 1L), // UV
            arrayOf(OrePrefixes.circuit.get(Materials.UHV), 1L), // UHV
            arrayOf(OrePrefixes.circuit.get(Materials.UEV), 1L), // UEV
            arrayOf(OrePrefixes.circuit.get(Materials.UIV), 1L), // UIV
            arrayOf(OrePrefixes.circuit.get(Materials.UMV), 1L), // UMV
            arrayOf(OrePrefixes.circuit.get(Materials.UXV), 1L), // UXV
            arrayOf(OrePrefixes.circuit.get(Materials.UXV), 4L), // MAX
            arrayOf(OrePrefixes.circuit.get(Materials.UXV), 16L) // MAX
            )

    val wirelessHatches =
        arrayOf(
            ItemList.Wireless_Hatch_Energy_ULV.get(1),
            ItemList.Wireless_Hatch_Energy_LV.get(1),
            ItemList.Wireless_Hatch_Energy_MV.get(1),
            ItemList.Wireless_Hatch_Energy_HV.get(1),
            ItemList.Wireless_Hatch_Energy_EV.get(1),
            ItemList.Wireless_Hatch_Energy_IV.get(1),
            ItemList.Wireless_Hatch_Energy_LuV.get(1),
            ItemList.Wireless_Hatch_Energy_ZPM.get(1),
            ItemList.Wireless_Hatch_Energy_UV.get(1),
            ItemList.Wireless_Hatch_Energy_UHV.get(1),
            ItemList.Wireless_Hatch_Energy_UEV.get(1),
            ItemList.Wireless_Hatch_Energy_UIV.get(1),
            ItemList.Wireless_Hatch_Energy_UMV.get(1),
            ItemList.Wireless_Hatch_Energy_UXV.get(1))

    val wirelessHatches4A =
        arrayOf(
            CustomItemList.eM_energyWirelessMulti4_EV.get(1),
            CustomItemList.eM_energyWirelessMulti4_IV.get(1),
            CustomItemList.eM_energyWirelessMulti4_LuV.get(1),
            CustomItemList.eM_energyWirelessMulti4_ZPM.get(1),
            CustomItemList.eM_energyWirelessMulti4_UV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UHV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UEV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UIV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UMV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UXV.get(1))

    val wirelessHatches16A =
        arrayOf(
            CustomItemList.eM_energyWirelessMulti16_EV.get(1),
            CustomItemList.eM_energyWirelessMulti16_IV.get(1),
            CustomItemList.eM_energyWirelessMulti16_LuV.get(1),
            CustomItemList.eM_energyWirelessMulti16_ZPM.get(1),
            CustomItemList.eM_energyWirelessMulti16_UV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UHV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UEV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UIV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UMV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UXV.get(1))

    val wirelessHatches64A =
        arrayOf(
            CustomItemList.eM_energyWirelessMulti64_EV.get(1),
            CustomItemList.eM_energyWirelessMulti64_IV.get(1),
            CustomItemList.eM_energyWirelessMulti64_LuV.get(1),
            CustomItemList.eM_energyWirelessMulti64_ZPM.get(1),
            CustomItemList.eM_energyWirelessMulti64_UV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UHV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UEV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UIV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UMV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UXV.get(1))

    val wirelessLasers =
        arrayOf(
            CustomItemList.eM_energyWirelessTunnel1_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel2_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel3_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel4_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel5_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel6_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel7_UXV.get(1))

    val wirelessDynamos =
        arrayOf(
            ItemList.Wireless_Dynamo_Energy_ULV.get(1),
            ItemList.Wireless_Dynamo_Energy_LV.get(1),
            ItemList.Wireless_Dynamo_Energy_MV.get(1),
            ItemList.Wireless_Dynamo_Energy_HV.get(1),
            ItemList.Wireless_Dynamo_Energy_EV.get(1),
            ItemList.Wireless_Dynamo_Energy_IV.get(1),
            ItemList.Wireless_Dynamo_Energy_LuV.get(1),
            ItemList.Wireless_Dynamo_Energy_ZPM.get(1),
            ItemList.Wireless_Dynamo_Energy_UV.get(1),
            ItemList.Wireless_Dynamo_Energy_UHV.get(1),
            ItemList.Wireless_Dynamo_Energy_UEV.get(1),
            ItemList.Wireless_Dynamo_Energy_UIV.get(1),
            ItemList.Wireless_Dynamo_Energy_UMV.get(1),
            ItemList.Wireless_Dynamo_Energy_UXV.get(1))

    // Wireless EU hatches

    for (i in wirelessHatches.indices) {
      builder()
          .itemInputs(
              energyHatches[i],
              ItemRefer.Compact_Fusion_Coil_T0.get(1),
              ItemList.Casing_Coil_Superconductor.get(1),
              CustomItemList.Machine_Multi_Transformer.get(1),
              CustomItemList.eM_Power.get(2),
              circuitsTierPlusTwo[i],
              ItemList.EnergisedTesseract.get(1))
          .fluidInputs(
              SolderMaterial.MutatedLivingAlloy.getFluidStack(9 * INGOTS),
              Materials.Infinity.getIngotMolten(8),
              MaterialsUEVplus.SpaceTime.getMolten(72),
              MaterialsUEVplus.ExcitedDTEC.getFluid(500))
          .itemOutputs(wirelessHatches[i])
          .eut(recipeEU)
          .duration(recipeDuration)
          .addTo(ia)
    }

    // 4A Wireless EU hatches

    for (i in wirelessHatches4A.indices) {
      builder()
          .itemInputs(
              energyHatches4A[i],
              ItemRefer.Compact_Fusion_Coil_T1.get(1),
              ItemList.Casing_Coil_Superconductor.get(1),
              CustomItemList.Machine_Multi_Transformer.get(1),
              CustomItemList.eM_Power.get(4),
              circuitsTierPlusTwo[i + 4],
              ItemList.EnergisedTesseract.get(1))
          .fluidInputs(
              SolderMaterial.MutatedLivingAlloy.getFluidStack(4 * 9 * INGOTS),
              Materials.Flerovium.getIngotMolten(32),
              GGMaterial.shirabon.getMolten(12 * INGOTS),
              MaterialsUEVplus.SpaceTime.getIngotMolten(2),
              MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 500))
          .itemOutputs(wirelessHatches4A[i])
          .eut(recipeEU)
          .duration(recipeDuration)
          .addTo(ia)

      // 16A Wireless EU hatches

      for (i in wirelessHatches16A.indices) {
        builder()
            .itemInputs(
                energyHatches16A[i],
                ItemRefer.Compact_Fusion_Coil_T2.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Power.get(16),
                circuitsTierPlusTwo[i + 4],
                ItemList.EnergisedTesseract.get(1))
            .fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(16 * 9 * INGOTS),
                GGMaterial.shirabon.getMolten(48 * INGOTS),
                MaterialsUEVplus.TranscendentMetal.getIngotMolten(32),
                MaterialsUEVplus.SpaceTime.getIngotMolten(8),
                MaterialsUEVplus.ExcitedDTEC.getFluid(16 * 500))
            .itemOutputs(wirelessHatches16A[i])
            .eut(recipeEU)
            .duration(recipeDuration)
            .addTo(ia)
      }

      // 64A Wireless EU hatches

      for (i in wirelessHatches64A.indices) {
        builder()
            .itemInputs(
                energyHatches64A[i],
                ItemRefer.Compact_Fusion_Coil_T3.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Power.get(64),
                circuitsTierPlusTwo[i + 4],
                ItemList.EnergisedTesseract.get(1))
            .fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(64 * 9 * INGOTS),
                GGMaterial.shirabon.getMolten(192 * INGOTS),
                GGMaterial.metastableOganesson.getMolten(32 * INGOTS),
                MaterialsUEVplus.SpaceTime.getIngotMolten(32),
                MaterialsUEVplus.ExcitedDTEC.getFluid(64 * 500))
            .itemOutputs(wirelessHatches64A[i])
            .eut(recipeEU)
            .duration(recipeDuration)
            .addTo(ia)
      }

      // Wireless UXV Lasers

      for (i in wirelessLasers.indices) {
        builder()
            .itemInputs(
                laserTargetsUXV[i],
                ItemRefer.Compact_Fusion_Coil_T4.get(1),
                CustomItemList.LASERpipeBlock.get(64),
                CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Power.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16L),
                ItemList.EnergisedTesseract.get(1))
            .fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(256 * 9 * INGOTS),
                MaterialsUEVplus.Eternity.getIngotMolten(32 * 8),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getIngotMolten(
                    16 * 8),
                MaterialsUEVplus.SpaceTime.getIngotMolten(512),
                MaterialsUEVplus.ExcitedDTSC.getFluid(64 * 500))
            .itemOutputs(wirelessLasers[i])
            .eut(recipeEU)
            .duration(recipeDuration)
            .addTo(ia)
      }

      // Wireless EU dynamos
      for (i in wirelessHatches.indices) {
        builder()
            .itemInputs(
                dynamoHatches[i],
                ItemRefer.Compact_Fusion_Coil_T0.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Power.get(2),
                circuitsTierPlusTwo[i],
                ItemList.Tesseract.get(1))
            .fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(9 * INGOTS),
                Materials.Infinity.getIngotMolten(8),
                MaterialsUEVplus.SpaceTime.getMolten(72),
                MaterialsUEVplus.ExcitedDTEC.getFluid(500))
            .itemOutputs(wirelessDynamos[i])
            .eut(recipeEU)
            .duration(recipeDuration)
            .addTo(ia)
      }
    }
  }
}
