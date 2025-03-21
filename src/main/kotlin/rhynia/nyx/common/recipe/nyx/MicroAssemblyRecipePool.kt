package rhynia.nyx.common.recipe.nyx

import bartworks.system.material.WerkstoffLoader
import goodgenerator.items.GGMaterial
import goodgenerator.util.ItemRefer
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods.AppliedEnergistics2
import gregtech.api.enums.Mods.Computronics
import gregtech.api.enums.Mods.NewHorizonsCoreMod
import gregtech.api.enums.Mods.OpenComputers
import gregtech.api.enums.Mods.SuperSolarPanels
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTRecipeBuilder.BUCKETS
import gregtech.api.util.GTRecipeBuilder.INGOTS
import gregtech.api.util.GTUtility
import gtPlusPlus.core.material.MaterialsAlloy
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import rhynia.nyx.Config
import rhynia.nyx.api.enums.NyxMods
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_EV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_IV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_LuV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UEV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UHV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UMV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_UXV
import rhynia.nyx.api.enums.NyxValues.RecipeValues.RECIPE_ZPM
import rhynia.nyx.api.enums.ref.BartPart
import rhynia.nyx.api.enums.ref.BundleChip
import rhynia.nyx.api.enums.ref.SolderMaterial
import rhynia.nyx.api.enums.ref.SuperConductorPart
import rhynia.nyx.api.enums.ref.Tier
import rhynia.nyx.api.recipe.NyxRecipeMaps
import rhynia.nyx.api.util.FluidUtil
import rhynia.nyx.api.util.StackUtil.copyAmountUnsafe
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.NyxMaterials
import rhynia.nyx.common.recipe.RecipePool
import tectech.thing.CustomItemList

@Suppress("SpellCheckingInspection")
class MicroAssemblyRecipePool : RecipePool() {
    private val ma = NyxRecipeMaps.microAssemblyRecipes
    private val partOpticalMultiply = 4

    override fun loadRecipes() {
        loadMain()
        if (Config.loadTstRecipe && NyxMods.TwistSpaceTechnology.isModLoaded) loadTstRecipe()
        loadMachineAssembly()
    }

    private fun loadMain() {
        // region 光学元件
        // 二极管
        altBuilder(ma) {
            itemInputs(
                GTOreDictUnificator
                    .get(OrePrefixes.foil, Materials.Tritanium, 1)
                    .ofSize(partOpticalMultiply * 32 * 4),
                MaterialsAlloy.LAFIUM.getFoil(1).ofSize(partOpticalMultiply * 32 * 2),
                GTOreDictUnificator
                    .get(OrePrefixes.foil, SuperConductorPart.ZPM.getMaterial(true), 1)
                    .ofSize(partOpticalMultiply * 32),
            ).fluidInputs(
                FluidUtil.getFluidStack("xenoxene", partOpticalMultiply * 32 * INGOTS),
                SuperConductorPart.LuV.getMolten(partOpticalMultiply * 16 * INGOTS),
            ).eut(RECIPE_ZPM)
                .durSec(partOpticalMultiply * 32 * 5)
        }
        // 电阻
        altBuilder(ma) {
            itemInputs(
                GTOreDictUnificator
                    .get(OrePrefixes.foil, Materials.Naquadria, 1)
                    .ofSize(partOpticalMultiply * 32 * 4),
                MaterialsAlloy.PIKYONIUM.getFoil(1).ofSize(partOpticalMultiply * 32 * 2),
                GTOreDictUnificator
                    .get(OrePrefixes.foil, SuperConductorPart.ZPM.getMaterial(true), 1)
                    .ofSize(partOpticalMultiply * 32),
            ).fluidInputs(
                FluidUtil.getFluidStack("xenoxene", partOpticalMultiply * 32 * INGOTS),
                SuperConductorPart.LuV.getMolten(partOpticalMultiply * 16 * INGOTS),
            ).itemOutputs(
                ItemList.Circuit_Parts_ResistorXSMD.get(1).ofSize(partOpticalMultiply * 32 * 32),
            ).eut(RECIPE_ZPM)
                .durSec(partOpticalMultiply * 32 * 5)
        }
        // 晶体管
        altBuilder(ma) {
            itemInputs(
                GTOreDictUnificator
                    .get(OrePrefixes.foil, Materials.BlackPlutonium, 1)
                    .ofSize(partOpticalMultiply * 32 * 4),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL
                    .getFoil(1)
                    .ofSize(partOpticalMultiply * 32 * 2),
                GTOreDictUnificator
                    .get(OrePrefixes.foil, SuperConductorPart.ZPM.getMaterial(true), 1)
                    .ofSize(partOpticalMultiply * 32),
            ).fluidInputs(
                FluidUtil.getFluidStack("xenoxene", partOpticalMultiply * 32 * INGOTS),
                SuperConductorPart.LuV.getMolten(partOpticalMultiply * 16 * INGOTS),
            ).itemOutputs(
                ItemList.Circuit_Parts_TransistorXSMD.get(1).ofSize(partOpticalMultiply * 32 * 32),
            ).eut(RECIPE_ZPM)
                .durSec(partOpticalMultiply * 32 * 5)
        }
        // 电容
        altBuilder(ma) {
            itemInputs(
                GTOreDictUnificator
                    .get(OrePrefixes.foil, Materials.Draconium, 1)
                    .ofSize(partOpticalMultiply * 32 * 4),
                MaterialsAlloy.CINOBITE.getFoil(1).ofSize(partOpticalMultiply * 32 * 2),
                GTOreDictUnificator
                    .get(OrePrefixes.foil, SuperConductorPart.ZPM.getMaterial(true), 1)
                    .ofSize(partOpticalMultiply * 32),
            ).fluidInputs(
                FluidUtil.getFluidStack("xenoxene", partOpticalMultiply * 32 * INGOTS),
                SuperConductorPart.LuV.getMolten(partOpticalMultiply * 16 * INGOTS),
            ).itemOutputs(
                ItemList.Circuit_Parts_CapacitorXSMD.get(1).ofSize(partOpticalMultiply * 32 * 32),
            ).eut(RECIPE_ZPM)
                .durSec(partOpticalMultiply * 32 * 5)
        }
        // 电感
        altBuilder(ma) {
            itemInputs(
                GGMaterial.hikarium.get(OrePrefixes.foil, 1).ofSize(partOpticalMultiply * 32 * 4),
                GGMaterial.artheriumSn.get(OrePrefixes.foil, 1).ofSize(partOpticalMultiply * 32),
            ).fluidInputs(
                FluidUtil.getFluidStack("xenoxene", partOpticalMultiply * 32 * INGOTS),
                SuperConductorPart.LuV.getMolten(partOpticalMultiply * 16 * INGOTS),
            ).itemOutputs(
                ItemList.Circuit_Parts_InductorXSMD.get(1).ofSize(partOpticalMultiply * 32 * 32),
            ).eut(RECIPE_ZPM)
                .durSec(partOpticalMultiply * 32 * 5)
        }
        // endregion

        // region MISC
        builder()
            .itemInputs(
                Materials.Plastic.getPlates(32),
                Materials.Obsidian.getPlates(64),
                BartPart.AdvancedBoard.getItemStack(4),
                Tier.HV.getCircuit(4),
                GTModHandler.getModItem(SuperSolarPanels.ID, "redcomponent", 4),
                GTModHandler.getModItem(SuperSolarPanels.ID, "greencomponent", 4),
                GTModHandler.getModItem(SuperSolarPanels.ID, "bluecomponent", 4),
            ).itemOutputs(
                GTUtility.copyAmountUnsafe(
                    256,
                    GTModHandler.getModItem(OpenComputers.ID, "hologram2", 1),
                ),
            ).eut(RECIPE_IV)
            .durSec(16)
            .addTo(ma)
        // endregion

        val multiple = 1L
        // region 生物系
        // 生物超级电脑 UHV
        builder()
            .itemInputs(
                ItemList.Circuit_Board_Bio_Ultra.get(2 * multiple),
                ItemList.Circuit_Biowarecomputer.get(2 * multiple),
                ItemList.Circuit_Parts_TransistorXSMD.get(4 * multiple),
                ItemList.Circuit_Parts_ResistorXSMD.get(4 * multiple),
                ItemList.Circuit_Parts_CapacitorXSMD.get(4 * multiple),
                ItemList.Circuit_Parts_DiodeXSMD.get(4 * multiple),
                BartPart.NOR.getItemStack(2 * multiple),
                BartPart.RAM.getItemStack(4 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(10 * INGOTS * multiple.toInt()),
                Materials.BioMediumSterilized.getFluid(10 * INGOTS * multiple),
                Materials.SuperCoolant.getFluid(10 * BUCKETS * multiple),
                Materials.NiobiumTitanium.getMolten(4 * INGOTS * multiple),
                Materials.Silicone.getMolten(16 * INGOTS * multiple),
            ).itemOutputs(ItemList.Circuit_Biowaresupercomputer.get(multiple))
            .eut(RECIPE_UV)
            .durSec(100 * multiple.toInt())
            .addTo(ma)
        // 生物主机 UEV
        builder()
            .itemInputs(
                ItemList.Circuit_Biowaresupercomputer.get(2 * multiple),
                ItemList.Circuit_Parts_InductorXSMD.get(6 * multiple),
                ItemList.Circuit_Parts_TransistorXSMD.get(6 * multiple),
                ItemList.Circuit_Parts_ResistorXSMD.get(6 * multiple),
                ItemList.Circuit_Parts_CapacitorXSMD.get(6 * multiple),
                ItemList.Circuit_Parts_DiodeXSMD.get(6 * multiple),
                BartPart.RAM.getItemStack(4 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(20 * INGOTS * multiple),
                Materials.BioMediumSterilized.getFluid(20 * INGOTS * multiple),
                Materials.SuperCoolant.getFluid(20 * BUCKETS * multiple),
                Materials.Tritanium.getMolten(8 * INGOTS * multiple),
                Materials.Silicone.getMolten(16 * INGOTS * multiple),
                Materials.Polybenzimidazole.getMolten(16 * INGOTS * multiple),
                NyxMaterials.SuperconductorFlux.getLiquid(4 * INGOTS * multiple.toInt()),
            ).itemOutputs(ItemList.Circuit_Biomainframe.get(multiple))
            .eut(RECIPE_UHV)
            .durSec(150 * multiple.toInt())
            .addTo(ma)
        // endregion

        // region 光学系
        // 光学集群 UHV
        builder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(multiple),
                ItemList.Circuit_OpticalProcessor.get(2 * multiple),
                ItemList.Circuit_Parts_InductorXSMD.get(16 * multiple),
                ItemList.Circuit_Parts_CapacitorXSMD.get(20 * multiple),
                ItemList.Circuit_Parts_ResistorXSMD.get(20 * multiple),
                BartPart.NOR.getItemStack(2 * multiple),
                BartPart.RAM.getItemStack(4 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(10 * INGOTS * multiple),
                Materials.Radon.getPlasma(10 * INGOTS * multiple),
                Materials.SuperCoolant.getFluid(10 * BUCKETS * multiple),
                WerkstoffLoader.Oganesson.getFluidOrGas(500 * multiple.toInt()),
                GGMaterial.lumiium.getIngotMolten(3 * multiple),
                Materials.Silicone.getMolten(16 * INGOTS * multiple),
            ).itemOutputs(ItemList.Circuit_OpticalAssembly.get(multiple))
            .eut(RECIPE_UHV)
            .durSec(10 * multiple.toInt())
            .addTo(ma)
        // 光学超级电脑 UEV
        builder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(2 * multiple),
                ItemList.Circuit_OpticalAssembly.get(2 * multiple),
                ItemList.Circuit_Parts_TransistorXSMD.get(24 * multiple),
                ItemList.Circuit_Parts_ResistorXSMD.get(24 * multiple),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24 * multiple),
                ItemList.Circuit_Parts_DiodeXSMD.get(24 * multiple),
                BartPart.NOR.getItemStack(4 * multiple),
                BartPart.ASOC.getItemStack(2 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(20 * INGOTS * multiple),
                Materials.Radon.getPlasma(20 * INGOTS * multiple),
                Materials.SuperCoolant.getFluid(20 * BUCKETS * multiple),
                WerkstoffLoader.Oganesson.getFluidOrGas(BUCKETS * multiple.toInt()),
                GGMaterial.lumiium.getMolten(4 * INGOTS * multiple.toInt()),
                Materials.Silicone.getMolten(16 * INGOTS * multiple),
                Materials.Polybenzimidazole.getMolten(16 * INGOTS * multiple),
            ).itemOutputs(ItemList.Circuit_OpticalComputer.get(multiple))
            .eut(RECIPE_UHV)
            .durSec(100 * multiple.toInt())
            .addTo(ma)
        // 光学主机 UIV
        builder()
            .itemInputs(
                ItemList.Circuit_OpticalComputer.get(2 * multiple),
                BartPart.OpticalInductor.getItemStack(2 * multiple),
                BartPart.OpticalTransistor.getItemStack(2 * multiple),
                BartPart.OpticalResistor.getItemStack(2 * multiple),
                BartPart.OpticalCapacitor.getItemStack(2 * multiple),
                BartPart.OpticalDiode.getItemStack(2 * multiple),
                BartPart.ASOC.getItemStack(2 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(26 * INGOTS * multiple),
                Materials.Radon.getPlasma(40 * INGOTS * multiple),
                Materials.SuperCoolant.getBucketFluid(40),
                WerkstoffLoader.Oganesson.getFluidOrGas(2 * BUCKETS * multiple.toInt()),
                Materials.Tritanium.getMolten(16 * INGOTS * multiple),
                Materials.Silicone.getMolten(32 * INGOTS * multiple),
                Materials.Polybenzimidazole.getMolten(32 * INGOTS * multiple),
                NyxMaterials.SuperconductorFlux.getLiquid(16 * INGOTS * multiple.toInt()),
            ).itemOutputs(ItemList.Circuit_OpticalMainframe.get(multiple))
            .eut(RECIPE_UEV)
            .durSec(150 * multiple.toInt())
            .addTo(ma)
        // endregion

        // region 量子-Piko电路 UXV-UMV
        // Quantum UXV
        builder()
            .itemInputs(
                getCoreItem("PikoCircuit", 2 * multiple.toInt()),
                BartPart.OpticalCapacitor.getItemStack(4 * multiple),
                BartPart.OpticalDiode.getItemStack(4 * multiple),
                BartPart.OpticalTransistor.getItemStack(4 * multiple),
                BartPart.OpticalResistor.getItemStack(4 * multiple),
                BartPart.IC_Q.getItemStack(4 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(26 * INGOTS * multiple),
                Materials.UUMatter.getFluid(24 * BUCKETS * multiple),
                Materials.Osmium.getMolten(16 * INGOTS * multiple),
                Materials.Neutronium.getMolten(8 * INGOTS * multiple),
                GGMaterial.shirabon.getIngotMolten(8 * multiple),
                Materials.Indium.getMolten(8 * INGOTS * multiple),
                MaterialsUEVplus.SpaceTime.getMolten(4 * INGOTS * multiple),
                Materials.Lanthanum.getMolten(2 * INGOTS * multiple),
            ).itemOutputs(getCoreItem("QuantumCircuit", multiple.toInt()))
            .eut(RECIPE_UMV)
            .durSec(800 * multiple.toInt())
            .addTo(ma)
        // Pico UMV
        val picoC = getCoreItem("PikoCircuit", multiple.toInt())
        val picoW = getCoreItem("PicoWafer", 4 * multiple.toInt())
        builder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(multiple),
                picoW,
                ItemList.Circuit_OpticalMainframe.get(2 * multiple),
                BartPart.OpticalTransistor.getItemStack(3 * multiple),
                BartPart.OpticalResistor.getItemStack(3 * multiple),
                BartPart.OpticalCapacitor.getItemStack(3 * multiple),
                BartPart.OpticalDiode.getItemStack(3 * multiple),
                BartPart.IC_P.getItemStack(4 * multiple),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(26 * INGOTS * multiple),
                Materials.UUMatter.getFluid(8 * BUCKETS * multiple),
                Materials.Osmium.getMolten(8 * INGOTS * multiple),
                Materials.RadoxPolymer.getMolten(4 * INGOTS * multiple),
                MaterialsUEVplus.TranscendentMetal.getMolten(4 * INGOTS * multiple),
                Materials.Neutronium.getMolten(2 * INGOTS * multiple),
                Materials.Lanthanum.getMolten(8 * INGOTS * multiple),
            ).itemOutputs(picoC)
            .eut(RECIPE_UMV)
            .durSec(400 * multiple.toInt())
            .addTo(ma)
        // endregion

        // region High Energy Flow Circuit
        builder()
            .itemInputs(
                BartPart.EliteBoard.getItemStack(12),
                BundleChip.ZPM.getItemStack(24),
                BartPart.IC_Q.getItemStack(48),
            ).fluidInputs(
                SolderMaterial.IndaAlloy.getFluidStack(24 * INGOTS),
                NyxMaterials.SuperconductorFlux.getLiquid(6 * INGOTS),
                Materials.Infinity.getIngotMolten(4),
            ).itemOutputs(GTUtility.copyAmountUnsafe(256, getCoreItem("HighEnergyFlowCircuit")))
            .eut(RECIPE_LuV)
            .durSec(1600)
            .addTo(ma)
        // endregion

        // region 高算力工作站
        // T1
        builder()
            .itemInputs(
                BartPart.EliteBoard.getItemStack(1),
                // getCoreItem("EngravedGoldChip").copyAmountUnsafe(16 * 16),
                Materials.Gold.getPlates(1).ofSize(16 * 20),
                BartPart.ASOC.getItemStack(8),
                BartPart.NOR.getItemStack(32),
            ).fluidInputs(
                SolderMaterial.SolderingAlloy.getFluidStack(16 * 2 * INGOTS),
                GGMaterial.signalium.getMolten(16 * 4 * INGOTS),
                Materials.Aluminium.getIngotMolten(16 * 4),
                Materials.TinAlloy.getIngotMolten(16 * 4),
            ).itemOutputs(ItemRefer.HiC_T1.get(16))
            .eut(RECIPE_IV)
            .durSec(12 * 60)
            .addTo(ma)
        // T2
        builder()
            .itemInputs(
                ItemRefer.HiC_T1.get(32),
                getCoreItem("EngravedDiamondCrystalChip").copyAmountUnsafe(16 * 8),
                BartPart.NAND.getItemStack(16),
            ).fluidInputs(
                Materials.Plastic.getIngotMolten(16 * 2),
                GGMaterial.signalium.getMolten(16 * INGOTS),
                GGMaterial.lumiium.getMolten(16 * 72),
                Materials.Enderium.getMolten(16 * 72),
                Materials.Aluminium.getIngotMolten(16 * 8),
            ).itemOutputs(ItemRefer.HiC_T2.get(16))
            .eut(RECIPE_LuV)
            .durSec(12 * 5)
            .addTo(ma)
        // T3
        builder()
            .itemInputs(
                ItemRefer.HiC_T2.get(32),
                BartPart.Lapotron.getItemStack(8),
                BartPart.AdvancedCrystalRaw.getItemStack(1),
            ).fluidInputs(
                GGMaterial.adamantiumAlloy.getMolten(16 * 4 * INGOTS),
                GGMaterial.signalium.getMolten(16 * 2 * INGOTS),
                GGMaterial.lumiium.getMolten(16 * INGOTS),
                Materials.TungstenCarbide.getMolten(16 * 72),
                Materials.StainlessSteel.getIngotMolten(16 * 8),
            ).itemOutputs(ItemRefer.HiC_T3.get(16))
            .eut(RECIPE_ZPM)
            .durSec(12 * 5)
            .addTo(ma)
        // T4
        builder()
            .itemInputs(
                ItemRefer.HiC_T3.get(8),
                getCoreItem("EngravedEnergyChip", 32),
                BartPart.QBit.getItemStack(4),
            ).fluidInputs(
                GGMaterial.marM200.getMolten(4 * 8 * INGOTS),
                GGMaterial.signalium.getMolten(4 * 4 * INGOTS),
                GGMaterial.lumiium.getMolten(4 * 2 * INGOTS),
                GGMaterial.artheriumSn.getMolten(4 * INGOTS),
                Materials.EnergeticAlloy.getIngotMolten(4 * 8),
            ).itemOutputs(ItemRefer.HiC_T4.get(4))
            .eut(RECIPE_UV)
            .durSec(3 * 5)
            .addTo(ma)
        // T5
        builder()
            .itemInputs(
                ItemRefer.HiC_T4.get(8),
                // getCoreItem("EngravedManyullynCrystalChip", 64),
                getCoreItem("ManyullynCrystal").ofSize(64 + 32),
                ItemList.Circuit_Chip_BioCPU.get(1),
            ).fluidInputs(
                GGMaterial.titaniumBetaC.getMolten(4 * 1728),
                GGMaterial.signalium.getMolten(4 * 1152),
                GGMaterial.lumiium.getMolten(4 * 576),
                GGMaterial.dalisenite.getMolten(4 * 288),
                Materials.TungstenCarbide.getIngotMolten(4 * 8),
            ).itemOutputs(ItemRefer.HiC_T5.get(4))
            .eut(RECIPE_UHV)
            .durSec(3 * 5)
            .addTo(ma)
        // endregion

        // region OC
        // Magic RAM
        builder()
            .itemInputs(
                ItemList.Optically_Compatible_Memory.get(6),
                Tier.UEV.getCircuit(6),
                GTModHandler.getModItem(OpenComputers.ID, "item", 2, 103),
                ItemList.Circuit_Chip_PPIC.get(8),
                Materials.Neutronium.getPlates(24),
            ).fluidInputs(
                Materials.ElectrumFlux.getIngotMolten(12),
                Materials.Infinity.getIngotMolten(2),
            ).itemOutputs(GTModHandler.getModItem(Computronics.ID, "computronics.ocSpecialParts", 1, 0))
            .eut(RECIPE_UEV)
            .durSec(4)
            .addTo(ma)
        // APU T3
        builder()
            .itemInputs(
                GTModHandler.getModItem(OpenComputers.ID, "item", 1, 102),
                GTModHandler.getModItem(OpenComputers.ID, "item", 1, 43),
                GTModHandler.getModItem(OpenComputers.ID, "item", 1, 10),
                Tier.UV.getCircuit(2),
                Tier.LuV.getCircuit(4),
                Tier.IV.getCircuit(16),
            ).itemOutputs(GTModHandler.getModItem(OpenComputers.ID, "item", 1, 103))
            .eut(RECIPE_UV)
            .durSec(4)
            .addTo(ma)
        // Super Server
        builder()
            .itemInputs(
                GTModHandler.getModItem(OpenComputers.ID, "case3", 1, 0),
                GTModHandler.getModItem(OpenComputers.ID, "item", 2, 103),
                Tier.UV.getCircuit(2),
                Tier.LuV.getCircuit(16),
                Tier.IV.getCircuit(4),
            ).itemOutputs(GTModHandler.getModItem(OpenComputers.ID, "item", 1, 69))
            .eut(RECIPE_UV)
            .durSec(4)
            .addTo(ma)
        // endregion

        // region AE
        // Spacial 1
        builder()
            .itemInputs(
                GTModHandler.getModItem(
                    NewHorizonsCoreMod.ID,
                    "item.EngineeringProcessorSpatialPulsatingCore",
                    16,
                ),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ChargedCertusQuartzPlate", 64),
                Materials.Redstone.getPlates(64),
                Materials.NetherQuartz.getPlates(64),
            ).fluidInputs(
                SolderMaterial.SolderingAlloy.getFluidStack(4 * INGOTS),
                Materials.Glowstone.getIngotMolten(64),
            ).itemOutputs(
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 32),
            ).eut(RECIPE_EV)
            .durSec(4)
            .addTo(ma)
        // Spacial 2
        altBuilder(ma) {
            itemInputs(
                GTModHandler.getModItem(
                    NewHorizonsCoreMod.ID,
                    "item.EngineeringProcessorSpatialPulsatingCore",
                    64,
                ),
                GTModHandler
                    .getModItem(NewHorizonsCoreMod.ID, "item.ChargedCertusQuartzPlate", 64)
                    .ofSize(4 * 64),
                Materials.Redstone.getPlates(64).ofSize(4 * 64),
                Materials.NetherQuartz.getPlates(64).ofSize(4 * 64),
                ItemStack(Items.ender_pearl, 64),
            ).fluidInputs(
                SolderMaterial.IndaAlloy.getFluidStack(4 * INGOTS),
            ).itemOutputs(
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 33),
            ).eut(RECIPE_LuV)
                .durSec(4)
        }
        // Spacial 3
        altBuilder(ma) {
            itemInputs(
                NewHorizonsCoreMod
                    .getItem("item.EngineeringProcessorSpatialPulsatingCore", 1)
                    .ofSize(256),
                NewHorizonsCoreMod.getItem("item.ChargedCertusQuartzPlate", 1).ofSize(16 * 64),
                Materials.Redstone.getPlates(1).ofSize(16 * 64),
                Materials.NetherQuartz.getPlates(1).ofSize(16 * 64),
                ItemStack(Items.ender_eye, 64),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(4 * INGOTS),
            ).itemOutputs(AppliedEnergistics2.getItem("item.ItemMultiMaterial", 1, 34))
                .eut(RECIPE_UV)
                .durSec(4)
        }
        // endregion

        // region EOHC
        // EOHC
        for (i in 1 until 10) {
            builder()
                .itemInputs(
                    Tier.UXV.getCircuitWrap(4),
                    GTUtility.copyAmountUnsafe(
                        138,
                        CustomItemList.valueOf("SpacetimeCompressionFieldGeneratorTier${i - 1}").get(1),
                    ),
                    GTUtility.copyAmountUnsafe(
                        168,
                        CustomItemList.valueOf("TimeAccelerationFieldGeneratorTier${i - 1}").get(1),
                    ),
                    GTUtility.copyAmountUnsafe(
                        48,
                        CustomItemList.valueOf("StabilisationFieldGeneratorTier${i - 1}").get(1),
                    ),
                ).fluidInputs(
                    SolderMaterial.MutatedLivingAlloy.getFluidStack(128 * i * INGOTS),
                    MaterialsUEVplus.SpaceTime.getMolten(96L * i * INGOTS),
                ).itemOutputs(NyxItemList.valueOf("EOHCoreT$i").get(1))
                .eut(RECIPE_UXV)
                .durSec(16 * i)
                .addTo(ma)
        }
        // endregion
    }

    private fun loadTstRecipe() {
        // Optical SOC
        builder()
            .itemInputs(
                BartPart.OpticalCPU.getItemStack(16),
                BartPart.OpticalRam.getItemStack(32),
                BartPart.OpticalCapacitor.getItemStack(64),
                BartPart.OpticalDiode.getItemStack(64),
                GGMaterial.orundum.getDust(64),
                GGMaterial.orundum.getDust(64),
            ).fluidInputs(
                SolderMaterial.MutatedLivingAlloy.getFluidStack(48 * INGOTS),
                Materials.Sunnarium.getIngotMolten(32),
                NyxMaterials.Astrium.getIngotMolten(32),
            ).itemOutputs(
                // GTCMItemList.OpticalSOC.get(64)
                NyxMods.TwistSpaceTechnology.getItem("MetaItem01", 64, 2),
            ).noOptimize()
            .eut(RECIPE_UMV)
            .durSec(512)
            .addTo(ma)
    }

    // region Machine Assembly
    private fun loadMachineAssembly() {
        // Calibration
        GTModHandler.addShapelessCraftingRecipe(
            NyxItemList.Calibration.get(1),
            arrayOf(Tier.UV.getCircuit(1), Tier.UHV.getCircuit(1), Tier.UEV.getCircuit(1)),
        )

        // DTPF
        builder()
            .itemInputs(
                ItemList.Machine_Multi_PlasmaForge.get(1),
                GTUtility.copyAmountUnsafe(2121, ItemList.Casing_Dim_Trans.get(1)),
                GTUtility.copyAmountUnsafe(1273, ItemList.Casing_Dim_Injector.get(1)),
                GTUtility.copyAmountUnsafe(120, ItemList.Casing_Dim_Bridge.get(1)),
                GTUtility.copyAmountUnsafe(2112, ItemList.Casing_Coil_Eternal.get(1)),
            ).fluidInputs(SolderMaterial.MutatedLivingAlloy.getFluidStack(64 * INGOTS))
            .itemOutputs(NyxItemList.AssemblyDTPF.get(1))
            .noOptimize()
            .eut(RECIPE_UMV)
            .durSec(16)
            .addTo(ma)
    }
    // endregion
}
