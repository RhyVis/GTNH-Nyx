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
import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.ModLogger
import rhynia.nyx.api.enums.RecipeValues.BUCKET
import rhynia.nyx.api.enums.RecipeValues.HOUR
import rhynia.nyx.api.enums.RecipeValues.INGOT
import rhynia.nyx.api.enums.RecipeValues.MINUTE
import rhynia.nyx.api.enums.RecipeValues.SECOND
import rhynia.nyx.api.enums.ref.Tier
import rhynia.nyx.api.util.copyAmountUnsafe
import rhynia.nyx.api.util.debugItem
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
                ModLogger.error("Failed to load CoreMod RecipeLoader", e)
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
                        ModLogger.error("Failed to get item $name from CoreMod, with null result")
                        debugItem("Failed to get item $name from CoreMod, with null result")
                    }
            } catch (e: Exception) {
                ModLogger.error("Failed to get item $name from CoreMod, with error", e)
                debugItem("Failed to get item $name from CoreMod, with error")
            }

        /**
         * Use the ModHandler approach to get the item
         */
        fun getCoreItemAlt(
            name: String,
            amount: Int = 1,
        ): ItemStack = Mods.NewHorizonsCoreMod.getItem(name, amount, 0) { debugItem("CoreMod item null: $name, $amount") }
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

    protected fun ic(id: Int): ItemStack = GTUtility.getIntegratedCircuit(id)

    protected fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder = this.duration(seconds * SECOND)

    protected fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder = this.duration(minutes * MINUTE)

    protected fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder = this.duration(hours * HOUR)

    protected fun GTRecipeBuilder.eut(tier: Tier): GTRecipeBuilder = this.eut(tier.voltageRecipe)

    protected fun Materials.getBucketFluid(amount: Int): FluidStack = this.getFluid(amount * BUCKET)

    protected fun Materials.getIngotFluid(amount: Int): FluidStack = this.getFluid(amount * INGOT)

    protected fun Materials.getBucketMolten(amount: Int): FluidStack = this.getMolten(amount * BUCKET)

    protected fun Materials.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * INGOT)

    protected fun Werkstoff.getDust(amount: Int): ItemStack = this.get(OrePrefixes.dust, amount)

    protected fun Werkstoff.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * INGOT.toInt())

    protected fun Werkstoff.getIngotMolten(amount: Long): FluidStack = this.getMolten((amount * INGOT).toInt())

    protected fun Werkstoff.getBucketMolten(amount: Int): FluidStack = this.getMolten((amount * BUCKET).toInt())

    protected fun NyxMaterial.getIngotMolten(amount: Int): FluidStack = this.getMolten(amount * INGOT.toInt())

    protected fun NyxMaterial.getIngotMolten(amount: Long): FluidStack = this.getMolten((amount * INGOT).toInt())

    protected fun NyxMaterial.getBucketMolten(amount: Int): FluidStack = this.getMolten((amount * BUCKET).toInt())

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

        fun durSec(seconds: Int): NyxRecipeBuilder = this.also { this.duration = seconds * SECOND }

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
