package vis.rhynia.nova.common.recipe.nova

import bartworks.system.material.WerkstoffLoader
import goodgenerator.items.GGMaterial
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.MaterialsUEVplus
import gregtech.api.enums.Mods.NewHorizonsCoreMod
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTUtility.getIntegratedCircuit
import gtPlusPlus.core.item.ModItems
import gtPlusPlus.core.util.minecraft.ItemUtils
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_EV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_HV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_IV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_LuV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UEV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UV
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.RECIPE_UXV
import vis.rhynia.nova.api.interfaces.RecipePool
import vis.rhynia.nova.api.recipe.NovaRecipeMaps
import vis.rhynia.nova.common.material.NovaMaterial

class AstralForgeRecipePool : RecipePool {
  private val af = NovaRecipeMaps.astralForgeRecipes

  private val sand = ItemStack(Blocks.sand, 64)
  private val soulSand = ItemStack(Blocks.soul_sand, 64)
  private val blazePowder = ItemStack(Items.blaze_powder, 64)
  private val enderPearl = ItemStack(Items.ender_pearl, 64)

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
            // LensAstriumInfinity
            // AstriumInfinityGem
            )
        .fluidInputs(
            MaterialsUEVplus.DimensionallyTranscendentResidue.getBucketFluid(4),
            MaterialsUEVplus.SpaceTime.getIngotFluid(4),
        )
        .itemOutputs(GGMaterial.shirabon.getDust(64), ItemList.Timepiece.get(4))
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
            // VA_ItemList.LensAstriumMagic.get(0),
            NovaMaterial.Astrium.getDust(16),
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
            // VA_ItemList.LensAstriumMagic.get(0),
            Materials.Iron.getDust(64),
            Materials.Iron.getDust(64),
            Materials.Diamond.getDust(64),
            Materials.Diamond.getDust(64),
        )
        .itemOutputs(Materials.Unstable.getDust(64), Materials.Unstable.getDust(64))
        .eut(RECIPE_IV)
        .durSec(20)
        .addTo(af)
    // endregion

    // region 11
    // 灵魂沙
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            // VA_ItemList.LensAstriumInfinity.get(0),
            ItemStack(Items.blaze_powder, 4, 0),
            sand,
            sand,
            sand,
            sand,
            sand,
            sand,
            sand,
            sand,
        )
        .fluidInputs(Materials.Water.getBucketFluid(1), NovaMaterial.Astrium.getMolten(16))
        .itemOutputs(soulSand)
        .eut(RECIPE_HV)
        .durSec(18)
        .addTo(af)
    // 烈焰粉
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            // VA_ItemList.LensAstriumInfinity.get(0),
            Materials.Carbon.getDust(64),
            Materials.Sulfur.getDust(64),
        )
        .fluidInputs(Materials.Lava.getBucketFluid(1), NovaMaterial.Astrium.getMolten(16))
        .itemOutputs(blazePowder, blazePowder, blazePowder, blazePowder)
        .eut(RECIPE_EV)
        .durSec(18)
        .addTo(af)
    // 末影珍珠
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            // VA_ItemList.LensAstriumMagic.get(0),
            GTModHandler.getModItem("HardcoreEnderExpansion", "end_powder", 16),
            NovaMaterial.Astrium.getDust(64),
        )
        .fluidInputs(NovaMaterial.Astrium.getMolten(16))
        .itemOutputs(enderPearl, enderPearl, enderPearl, enderPearl)
        .eut(RECIPE_IV)
        .durSec(18)
        .addTo(af)
    // 胶质云母
    val micaBasedPulp = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MicaBasedPulp", 64)
    builder()
        .itemInputs(
            getIntegratedCircuit(11),
            // VA_ItemList.LensAstriumMagic.get(0),
            NovaMaterial.Astrium.getDust(64),
            NovaMaterial.Astrium.getDust(64),
            Materials.Mica.getDust(64),
            Materials.Mica.getDust(64),
            Materials.Mica.getDust(64),
            Materials.Mica.getDust(64),
        )
        .fluidInputs(NovaMaterial.Astrium.getMolten(1000))
        .itemOutputs(
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
            micaBasedPulp,
        )
        .eut(RECIPE_LuV)
        .durSec(20)
        .addTo(af)
    // endregion

    // region 9号电路 分离
    // 净化水
    val mysteriousCrystalLens =
        GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0)
    val chromaticLens = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 0)
    builder()
        .itemInputs(
            getIntegratedCircuit(9),
            // VA_ItemList.LensAstriumInfinity.get(0),
            // VA_ItemList.LensAstriumMagic.get(0),
            WerkstoffLoader.Tiberium.get(OrePrefixes.lens, 0),
            mysteriousCrystalLens,
            chromaticLens,
            // VA_ItemList.LensPrimoium.get(0),
            // VA_ItemList.LensOriginium.get(0),
            NovaMaterial.Astrium.getDust(64),
            NovaMaterial.Astrium.getDust(64),
            NovaMaterial.Astrium.getDust(64),
            NovaMaterial.Astrium.getDust(64),
        )
        .fluidInputs(Materials.Water.getBucketFluid(512), Materials.UUMatter.getBucketFluid(16))
        .itemOutputs(
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 0, 1),
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 7, 1),
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 18, 1),
            ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 24, 1),
            NovaMaterial.AstriumMagic.getDust(32),
            NovaMaterial.AstriumInfinity.getDust(32),
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
  }
}
