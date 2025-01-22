package vis.rhynia.nova.api.interfaces

import bartworks.system.material.Werkstoff
import gregtech.api.enums.Materials
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

interface RecipePool {
  /** Load recipes into the recipe pool at Complete Load Stage */
  fun loadRecipes()

  /** Alias for RA.stdBuilder() */
  fun builder(): GTRecipeBuilder = GTRecipeBuilder.builder().noOptimize()

  fun getCoreItem(name: String, amount: Int = 1): ItemStack =
      GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.$name", amount.toLong(), 0)

  fun ItemStack.withSize(size: Int): ItemStack = this.apply { this.stackSize = size }

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
}
