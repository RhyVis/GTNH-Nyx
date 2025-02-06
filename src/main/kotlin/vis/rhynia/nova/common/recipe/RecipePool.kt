package vis.rhynia.nova.common.recipe

import bartworks.system.material.Werkstoff
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipeBuilder
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.Log
import vis.rhynia.nova.api.enums.NovaMods
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.common.loader.container.NovaItemList
import vis.rhynia.nova.common.material.generation.SimpleMaterial

@Suppress("unused")
abstract class RecipePool {
  /** Load recipes into the recipe pool at Complete Load Stage */
  abstract fun loadRecipes()

  protected companion object {
    val coreItemList: Class<*> by lazy {
      try {
        @Suppress("SpellCheckingInspection")
        Class.forName("com.dreammaster.gthandler.CustomItemList")
      } catch (e: ClassNotFoundException) {
        Log.error("Failed to load CoreMod RecipeLoader", e)
        NovaItemList::class.java
      }
    }

    fun getCoreItem(name: String, amount: Int = 1): ItemStack =
        try {
          coreItemList.enumConstants
              .firstOrNull { it.toString() == name }
              .takeIf { it is IItemContainer }
              ?.let { (it as IItemContainer).get(amount.toLong()) }
              ?: let {
                Log.error("Failed to get item $name from CoreMod, with null result")
                NovaItemList.TestItem01.get(0)
              }
        } catch (e: Exception) {
          Log.error("Failed to get item $name from CoreMod, with error", e)
          NovaItemList.TestItem01.get(666)
        }

    fun getCoreItemAlt(name: String, amount: Int = 1): ItemStack =
        GTModHandler.getModItem(
            Mods.NewHorizonsCoreMod.ID,
            name,
            amount.toLong(),
            0,
            NovaItemList.TestItem01.get(amount.toLong() * 6 + 1))
  }

  /** Alias for RA.stdBuilder() */
  protected fun builder(): GTRecipeBuilder = GTRecipeBuilder.builder().noOptimize()

  protected fun builder(`if`: Boolean) = if (`if`) builder() else null

  protected fun builder(requiredMod: Mods) = builder(requiredMod.isModLoaded)

  protected fun builder(vararg requiredMods: Mods) = builder(requiredMods.all { it.isModLoaded })

  protected fun Mods.getItem(name: String, amount: Int = 1, meta: Int = 0): ItemStack =
      GTModHandler.getModItem(
          this.ID,
          name,
          amount.toLong(),
          meta,
          NovaItemList.TestItem01.get(amount.toLong() * 6 + 1))

  protected fun NovaMods.getItem(name: String, amount: Int = 1, meta: Int = 0): ItemStack =
      GTModHandler.getModItem(
          this.modId,
          name,
          amount.toLong(),
          meta,
          NovaItemList.TestItem01.get(amount.toLong() * 6 + 1))

  protected fun ItemStack.setSize(size: Int): ItemStack = this.apply { this.stackSize = size }

  protected fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder =
      this.duration(seconds * NovaValues.RecipeValues.SECOND)

  protected fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder =
      this.duration(minutes * NovaValues.RecipeValues.MINUTE)

  protected fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder =
      this.duration(hours * NovaValues.RecipeValues.HOUR)

  protected fun Materials.getBucketFluid(amount: Int): FluidStack =
      this.getFluid(amount * NovaValues.RecipeValues.BUCKET)

  protected fun Materials.getIngotFluid(amount: Int): FluidStack =
      this.getFluid(amount * NovaValues.RecipeValues.INGOT)

  protected fun Materials.getBucketMolten(amount: Int): FluidStack =
      this.getMolten(amount * NovaValues.RecipeValues.BUCKET)

  protected fun Materials.getIngotMolten(amount: Int): FluidStack =
      this.getMolten(amount * NovaValues.RecipeValues.INGOT)

  protected fun Werkstoff.getDust(amount: Int): ItemStack = this.get(OrePrefixes.dust, amount)

  protected fun Werkstoff.getIngotMolten(amount: Int): FluidStack =
      this.getMolten(amount * NovaValues.RecipeValues.INGOT.toInt())

  protected fun Werkstoff.getIngotMolten(amount: Long): FluidStack =
      this.getMolten((amount * NovaValues.RecipeValues.INGOT).toInt())

  protected fun Werkstoff.getBucketMolten(amount: Int): FluidStack =
      this.getMolten((amount * NovaValues.RecipeValues.BUCKET).toInt())

  protected fun SimpleMaterial.getIngotMolten(amount: Int): FluidStack =
      this.getMolten(amount * NovaValues.RecipeValues.INGOT.toInt())

  protected fun SimpleMaterial.getIngotMolten(amount: Long): FluidStack =
      this.getMolten((amount * NovaValues.RecipeValues.INGOT).toInt())

  protected fun SimpleMaterial.getBucketMolten(amount: Int): FluidStack =
      this.getMolten((amount * NovaValues.RecipeValues.BUCKET).toInt())
}
