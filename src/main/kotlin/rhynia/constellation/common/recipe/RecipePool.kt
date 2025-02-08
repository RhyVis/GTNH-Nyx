package rhynia.constellation.common.recipe

import bartworks.system.material.Werkstoff
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IItemContainer
import gregtech.api.recipe.RecipeMap
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTRecipeBuilder
import kotlin.jvm.java
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.constellation.Log
import rhynia.constellation.api.enums.CelMods
import rhynia.constellation.api.enums.CelValues
import rhynia.constellation.api.recipe.CelRecipeMapBackend
import rhynia.constellation.common.container.CelItemList
import rhynia.constellation.common.material.generation.CelMaterial

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
        CelItemList::class.java
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
                CelItemList.TestItem01.get(0)
              }
        } catch (e: Exception) {
          Log.error("Failed to get item $name from CoreMod, with error", e)
          CelItemList.TestItem01.get(666)
        }

    fun getCoreItemAlt(name: String, amount: Int = 1): ItemStack =
        GTModHandler.getModItem(
            Mods.NewHorizonsCoreMod.ID,
            name,
            amount.toLong(),
            0,
            CelItemList.TestItem01.get(amount.toLong() * 6 + 1))
  }

  /** Alias for RA.stdBuilder() */
  protected fun builder(): GTRecipeBuilder = GTRecipeBuilder.builder().noOptimize()

  protected fun builder(`if`: Boolean) = if (`if`) builder() else null

  protected fun builder(requiredMod: Mods) = builder(requiredMod.isModLoaded)

  protected fun builder(vararg requiredMods: Mods) = builder(requiredMods.all { it.isModLoaded })

  /**
   * Create a new recipe builder with the given action and add it to the recipe map
   *
   * Use together with CelRecipeMapBackend related recipe map to avoid 64+ stack problem with GT
   * Meta Generated Items
   *
   * @param backend RecipeMap Backend
   * @param action Recipe Builder Action
   */
  internal inline fun altBuilder(
      backend: RecipeMap<CelRecipeMapBackend>,
      action: CelRecipeBuilder.() -> CelRecipeBuilder
  ) = CelRecipeBuilder().apply { this.action().inject(backend) }

  protected fun Mods.getItem(name: String, amount: Int = 1, meta: Int = 0): ItemStack =
      GTModHandler.getModItem(
          this.ID, name, amount.toLong(), meta, CelItemList.TestItem01.get(amount.toLong() * 6 + 1))

  protected fun CelMods.getItem(name: String, amount: Int = 1, meta: Int = 0): ItemStack =
      GTModHandler.getModItem(
          this.modId,
          name,
          amount.toLong(),
          meta,
          CelItemList.TestItem01.get(amount.toLong() * 6 + 1))

  protected fun ItemStack.ofSize(size: Int): ItemStack = this.apply { this.stackSize = size }

  protected fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder =
      this.duration(seconds * CelValues.RecipeValues.SECOND)

  protected fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder =
      this.duration(minutes * CelValues.RecipeValues.MINUTE)

  protected fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder =
      this.duration(hours * CelValues.RecipeValues.HOUR)

  protected fun Materials.getBucketFluid(amount: Int): FluidStack =
      this.getFluid(amount * CelValues.RecipeValues.BUCKET)

  protected fun Materials.getIngotFluid(amount: Int): FluidStack =
      this.getFluid(amount * CelValues.RecipeValues.INGOT)

  protected fun Materials.getBucketMolten(amount: Int): FluidStack =
      this.getMolten(amount * CelValues.RecipeValues.BUCKET)

  protected fun Materials.getIngotMolten(amount: Int): FluidStack =
      this.getMolten(amount * CelValues.RecipeValues.INGOT)

  protected fun Werkstoff.getDust(amount: Int): ItemStack = this.get(OrePrefixes.dust, amount)

  protected fun Werkstoff.getIngotMolten(amount: Int): FluidStack =
      this.getMolten(amount * CelValues.RecipeValues.INGOT.toInt())

  protected fun Werkstoff.getIngotMolten(amount: Long): FluidStack =
      this.getMolten((amount * CelValues.RecipeValues.INGOT).toInt())

  protected fun Werkstoff.getBucketMolten(amount: Int): FluidStack =
      this.getMolten((amount * CelValues.RecipeValues.BUCKET).toInt())

  protected fun CelMaterial.getIngotMolten(amount: Int): FluidStack =
      this.getMolten(amount * CelValues.RecipeValues.INGOT.toInt())

  protected fun CelMaterial.getIngotMolten(amount: Long): FluidStack =
      this.getMolten((amount * CelValues.RecipeValues.INGOT).toInt())

  protected fun CelMaterial.getBucketMolten(amount: Int): FluidStack =
      this.getMolten((amount * CelValues.RecipeValues.BUCKET).toInt())

  internal class CelRecipeBuilder {
    private var inputItems: Array<ItemStack> = arrayOf()
    private var outputItems: Array<ItemStack> = arrayOf()
    private var inputFluids: Array<FluidStack> = arrayOf()
    private var outputFluids: Array<FluidStack> = arrayOf()
    private var outputChance: IntArray = IntArray(0)
    private var eut = 0
    private var duration = 0
    private var specialValue = 0

    fun itemInputs(vararg inputItems: ItemStack): CelRecipeBuilder {
      if (inputItems.isNotEmpty()) this.inputItems += inputItems
      return this
    }

    fun itemOutputs(vararg outputItems: ItemStack): CelRecipeBuilder {
      if (outputItems.isNotEmpty()) this.outputItems += outputItems
      return this
    }

    fun fluidInputs(vararg inputFluids: FluidStack): CelRecipeBuilder {
      if (inputFluids.isNotEmpty()) this.inputFluids += inputFluids
      return this
    }

    fun fluidOutputs(vararg outputFluids: FluidStack): CelRecipeBuilder {
      if (outputFluids.isNotEmpty()) this.outputFluids += outputFluids
      return this
    }

    fun outputChances(vararg outputChance: Int): CelRecipeBuilder =
        this.also { this.outputChance = outputChance }

    fun eut(eut: Int): CelRecipeBuilder = this.also { this.eut = eut }

    fun eut(eut: Long): CelRecipeBuilder = this.also { this.eut = eut.toInt() }

    fun duration(duration: Int): CelRecipeBuilder = this.also { this.duration = duration }

    fun durSec(seconds: Int): CelRecipeBuilder =
        this.also { this.duration = seconds * CelValues.RecipeValues.SECOND }

    fun specialValue(specialValue: Int): CelRecipeBuilder =
        this.also { this.specialValue = specialValue }

    /** Renamed inject method to avoid misuse in lambda calls */
    internal fun inject(recipeMap: RecipeMap<*>): CelRecipeBuilder {
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
