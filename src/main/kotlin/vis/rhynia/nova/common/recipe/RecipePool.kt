package vis.rhynia.nova.common.recipe

import bartworks.system.material.Werkstoff
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IItemContainer
import gregtech.api.recipe.RecipeMap
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTRecipeBuilder
import kubatech.loaders.MobHandlerLoader.recipeMap
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.Log
import vis.rhynia.nova.api.enums.NovaMods
import vis.rhynia.nova.api.enums.NovaValues
import vis.rhynia.nova.api.recipe.NovaRecipeMapBackend
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

  /**
   * Create a new recipe builder with the given action and add it to the recipe map
   *
   * Use together with NovaRecipeMapBackend related recipe map to avoid 64+ stack problem with GT
   * Meta Generated Items
   *
   * @param backend RecipeMap Backend
   * @param action Recipe Builder Action
   */
  protected inline fun altBuilder(
      backend: RecipeMap<NovaRecipeMapBackend>,
      action: (builder: NovaRecipeBuilder) -> NovaRecipeBuilder
  ) = NovaRecipeBuilder().apply { action(this).inject(backend) }

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

  protected fun ItemStack.ofSize(size: Int): ItemStack = this.apply { this.stackSize = size }

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

  class NovaRecipeBuilder {
    private var inputItems: Array<ItemStack> = arrayOf()
    private var outputItems: Array<ItemStack> = arrayOf()
    private var inputFluids: Array<FluidStack> = arrayOf()
    private var outputFluids: Array<FluidStack> = arrayOf()
    private var outputChance: IntArray = IntArray(0)
    private var eut = 0
    private var duration = 0
    private var specialValue = 0

    fun itemInputs(vararg inputItems: ItemStack): NovaRecipeBuilder {
      if (inputItems.isNotEmpty()) this.inputItems += inputItems
      return this
    }

    fun itemOutputs(vararg outputItems: ItemStack): NovaRecipeBuilder {
      if (outputItems.isNotEmpty()) this.outputItems += outputItems
      return this
    }

    fun fluidInputs(vararg inputFluids: FluidStack): NovaRecipeBuilder {
      if (inputFluids.isNotEmpty()) this.inputFluids += inputFluids
      return this
    }

    fun fluidOutputs(vararg outputFluids: FluidStack): NovaRecipeBuilder {
      if (outputFluids.isNotEmpty()) this.outputFluids += outputFluids
      return this
    }

    fun outputChances(vararg outputChance: Int): NovaRecipeBuilder =
        this.also { this.outputChance = outputChance }

    fun eut(eut: Int): NovaRecipeBuilder = this.also { this.eut = eut }

    fun eut(eut: Long): NovaRecipeBuilder = this.also { this.eut = eut.toInt() }

    fun duration(duration: Int): NovaRecipeBuilder = this.also { this.duration = duration }

    fun durSec(seconds: Int): NovaRecipeBuilder =
        this.also { this.duration = seconds * NovaValues.RecipeValues.SECOND }

    fun specialValue(specialValue: Int): NovaRecipeBuilder =
        this.also { this.specialValue = specialValue }

    /** Renamed inject method to avoid misuse in lambda calls */
    fun inject(recipeMap: RecipeMap<*>): NovaRecipeBuilder {
      GTRecipe(
              false,
              inputItems,
              outputItems,
              null,
              outputChance,
              inputFluids,
              outputFluids,
              duration,
              eut,
              specialValue)
          .apply {
            mInputs = inputItems.clone()
            mOutputs = outputItems.clone()
          }
          .let { recipeMap.add(it) }

      return this
    }
  }
}
