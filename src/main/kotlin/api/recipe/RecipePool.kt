package rhynia.nyx.api.recipe

import bartworks.system.material.Werkstoff
import gregtech.api.enums.GTValues
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IItemContainer
import gregtech.api.recipe.RecipeMap
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTRecipeBuilder
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.Log
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.util.copyAmountUnsafe
import rhynia.nyx.api.util.getItem
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.material.generation.NyxMaterial

/**
 * Base class for all recipe loaders, impl [loadRecipes] method, and call it in complete-init stage.
 *
 * Provide various helper methods for recipe creation.
 */
@Suppress("UNUSED")
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
                NyxItemList::class.java
            }
        }

        /** Alias for RA.stdBuilder() in [GTValues] */
        val builder: GTRecipeBuilder get() = GTValues.RA.stdBuilder()

        /**
         * Use enum constants to get the item
         */
        fun getCoreItem(
            name: String,
            amount: Int = 1,
        ): ItemStack =
            try {
                coreItemList.enumConstants
                    .firstOrNull { it.toString() == name }
                    .takeIf { it is IItemContainer }
                    ?.let { (it as IItemContainer).get(amount.toLong()) }
                    ?: let {
                        Log.error("Failed to get item $name from CoreMod, with null result")
                        NyxItemList.Companion.Dummy
                    }
            } catch (e: Exception) {
                Log.error("Failed to get item $name from CoreMod, with error", e)
                NyxItemList.Companion.Dummy
            }

        /**
         * Use the ModHandler approach to get the item
         */
        fun getCoreItemAlt(
            name: String,
            amount: Int = 1,
        ): ItemStack = Mods.NewHorizonsCoreMod.getItem(name, amount, 0, NyxItemList.Companion.Dummy)
    }

    /**
     * Require mods to be loaded before executing the action
     *
     * @param requiredMods Mods to be checked
     */
    protected inline fun requireMods(
        vararg requiredMods: Mods,
        action: () -> Unit,
    ) {
        if (requiredMods.all { it.isModLoaded }) action()
    }

    protected fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder = this.duration(seconds * NyxValues.RecipeValues.SECOND)

    protected fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder = this.duration(minutes * NyxValues.RecipeValues.MINUTE)

    protected fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder = this.duration(hours * NyxValues.RecipeValues.HOUR)

    protected fun Materials.getBucketFluid(amount: Int): FluidStack = this.getFluid(amount * NyxValues.RecipeValues.BUCKET)

    protected fun Materials.getIngotFluid(amount: Int): FluidStack = this.getFluid(amount * NyxValues.RecipeValues.INGOT)

    protected fun Materials.getBucketMolten(amount: Int): FluidStack = this.getMolten(amount * NyxValues.RecipeValues.BUCKET)

    protected fun Materials.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * NyxValues.RecipeValues.INGOT)

    protected fun Werkstoff.getDust(amount: Int): ItemStack = this.get(OrePrefixes.dust, amount)

    protected fun Werkstoff.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * NyxValues.RecipeValues.INGOT.toInt())

    protected fun Werkstoff.getIngotMolten(amount: Long): FluidStack = this.getMolten((amount * NyxValues.RecipeValues.INGOT).toInt())

    protected fun Werkstoff.getBucketMolten(amount: Int): FluidStack = this.getMolten((amount * NyxValues.RecipeValues.BUCKET).toInt())

    protected fun NyxMaterial.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * NyxValues.RecipeValues.INGOT.toInt())

    protected fun NyxMaterial.getIngotMolten(amount: Long): FluidStack = this.getMolten((amount * NyxValues.RecipeValues.INGOT).toInt())

    protected fun NyxMaterial.getBucketMolten(amount: Int): FluidStack = this.getMolten((amount * NyxValues.RecipeValues.BUCKET).toInt())

    protected fun IItemContainer.getAmountUnsafe(amount: Int): ItemStack = this.get(1).copyAmountUnsafe(amount)

    internal class NyxRecipeBuilder {
        private var inputItems: Array<ItemStack> = arrayOf()
        private var outputItems: Array<ItemStack> = arrayOf()
        private var inputFluids: Array<FluidStack> = arrayOf()
        private var outputFluids: Array<FluidStack> = arrayOf()
        private var outputChance: IntArray = IntArray(0)
        private var eut = 0
        private var duration = 0
        private var specialValue = 0

        fun itemInputs(vararg inputItems: ItemStack): NyxRecipeBuilder {
            if (inputItems.isNotEmpty()) this.inputItems += inputItems
            return this
        }

        fun itemOutputs(vararg outputItems: ItemStack): NyxRecipeBuilder {
            if (outputItems.isNotEmpty()) this.outputItems += outputItems
            return this
        }

        fun fluidInputs(vararg inputFluids: FluidStack): NyxRecipeBuilder {
            if (inputFluids.isNotEmpty()) this.inputFluids += inputFluids
            return this
        }

        fun fluidOutputs(vararg outputFluids: FluidStack): NyxRecipeBuilder {
            if (outputFluids.isNotEmpty()) this.outputFluids += outputFluids
            return this
        }

        fun outputChances(vararg outputChance: Int): NyxRecipeBuilder = this.also { this.outputChance = outputChance }

        fun eut(eut: Int): NyxRecipeBuilder = this.also { this.eut = eut }

        fun eut(eut: Long): NyxRecipeBuilder = this.also { this.eut = eut.toInt() }

        fun duration(duration: Int): NyxRecipeBuilder = this.also { this.duration = duration }

        fun durSec(seconds: Int): NyxRecipeBuilder = this.also { this.duration = seconds * NyxValues.RecipeValues.SECOND }

        fun specialValue(specialValue: Int): NyxRecipeBuilder = this.also { this.specialValue = specialValue }

        /** Renamed inject method to avoid misuse in lambda calls */
        internal fun inject(recipeMap: RecipeMap<*>) {
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
                specialValue,
            ).apply {
                mInputs = inputItems.clone()
                mOutputs = outputItems.clone()
            }.let { recipeMap.add(it) }
        }
    }
}
