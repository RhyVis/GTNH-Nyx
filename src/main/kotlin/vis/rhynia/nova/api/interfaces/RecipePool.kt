package vis.rhynia.nova.api.interfaces

import bartworks.system.material.Werkstoff
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.Mods.NewHorizonsCoreMod
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.BUCKET
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.HOUR
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.INGOT
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.MINUTE
import vis.rhynia.nova.api.enums.NovaValues.RecipeValues.SECOND
import vis.rhynia.nova.common.loader.container.NovaItemList

@Suppress("unused")
interface RecipePool {
  /** Load recipes into the recipe pool at Complete Load Stage */
  fun loadRecipes()

  /** Alias for RA.stdBuilder() */
  fun builder(): GTRecipeBuilder = GTRecipeBuilder.builder().noOptimize()

  fun builder(`if`: Boolean) = if (`if`) builder() else null

  fun builder(requiredMod: Mods) = builder(requiredMod.isModLoaded)

  fun getCoreItem(name: String, amount: Int = 1): ItemStack =
      NewHorizonsCoreMod.getItem(name, amount)

  fun Mods.getItem(name: String, amount: Int = 1, meta: Int = 0): ItemStack =
      GTModHandler.getModItem(
          this.ID, name, amount.toLong(), meta, NovaItemList.TestItem01.get(amount.toLong()))

  fun ItemStack.setSize(size: Int): ItemStack = this.apply { this.stackSize = size }

  fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder = this.duration(seconds * SECOND)

  fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder = this.duration(minutes * MINUTE)

  fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder = this.duration(hours * HOUR)

  fun Materials.getBucketFluid(amount: Int): FluidStack = this.getFluid(amount * BUCKET)

  fun Materials.getIngotFluid(amount: Int): FluidStack = this.getFluid(amount * INGOT)

  fun Materials.getBucketMolten(amount: Int): FluidStack = this.getMolten(amount * BUCKET)

  fun Materials.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * INGOT)

  fun Werkstoff.getDust(amount: Int): ItemStack = this.get(OrePrefixes.dust, amount)

  fun Werkstoff.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * INGOT.toInt())

  fun Werkstoff.getIngotMolten(amount: Long): FluidStack = this.getMolten((amount * INGOT).toInt())

  fun Werkstoff.getBucketMolten(amount: Int): FluidStack = this.getMolten((amount * BUCKET).toInt())
}
