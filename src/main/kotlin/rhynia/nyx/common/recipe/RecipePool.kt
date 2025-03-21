package rhynia.nyx.common.recipe

import bartworks.system.material.Werkstoff
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.api.enums.OrePrefixes
import gregtech.api.interfaces.IItemContainer
import gregtech.api.recipe.RecipeMap
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTRecipeBuilder
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.Log
import rhynia.nyx.api.enums.NyxMods
import rhynia.nyx.api.enums.NyxValues
import rhynia.nyx.api.recipe.NyxRecipeMapBackend
import rhynia.nyx.api.util.StackUtil.copyAmountUnsafe
import rhynia.nyx.common.container.NyxItemList
import rhynia.nyx.common.material.generation.NyxMaterial

/**
 * Base class for all recipe loaders, impl [loadRecipes] method, and call it in complete-init stage.
 *
 * Provide various helper methods for recipe creation.
 */
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
                NyxItemList::class.java
            }
        }

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
                        NyxItemList.TestItem01.get(0)
                    }
            } catch (e: Exception) {
                Log.error("Failed to get item $name from CoreMod, with error", e)
                NyxItemList.TestItem01.get(666)
            }

        fun getCoreItemAlt(
            name: String,
            amount: Int = 1,
        ): ItemStack =
            GTModHandler.getModItem(
                Mods.NewHorizonsCoreMod.ID,
                name,
                amount.toLong(),
                0,
                NyxItemList.TestItem01.get(amount.toLong() * 6 + 1),
            )
    }

    /** Alias for RA.stdBuilder() */
    protected fun builder(): GTRecipeBuilder = GTRecipeBuilder.builder().noOptimize()

    /**
     * Create a new recipe builder with the given action and add it to the recipe map
     *
     * Use together with NyxRecipeMapBackend related recipe map to avoid 64+ stack problem with GT
     * Meta Generated Items
     *
     * @param backend RecipeMap Backend
     * @param action Recipe Builder Action
     */
    internal inline fun altBuilder(
        backend: RecipeMap<NyxRecipeMapBackend>,
        action: NyxRecipeBuilder.() -> NyxRecipeBuilder,
    ) = NyxRecipeBuilder().apply { this.action().inject(backend) }

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

    protected fun Mods.getItem(
        name: String,
        amount: Int = 1,
        meta: Int = 0,
    ): ItemStack =
        GTModHandler.getModItem(
            this.ID,
            name,
            amount.toLong(),
            meta,
            NyxItemList.TestItem01.get(amount.toLong() * 6 + 1),
        )

    protected fun NyxMods.getItem(
        name: String,
        amount: Int = 1,
        meta: Int = 0,
    ): ItemStack =
        GTModHandler.getModItem(
            this.modId,
            name,
            amount.toLong(),
            meta,
            NyxItemList.TestItem01.get(amount.toLong() * 6 + 1),
        )

    protected infix fun ItemStack.ofSize(size: Int): ItemStack = this.apply { this.stackSize = size }

    protected infix fun GTRecipeBuilder.durSec(seconds: Int): GTRecipeBuilder = this.duration(seconds * NyxValues.RecipeValues.SECOND)

    protected infix fun GTRecipeBuilder.durMin(minutes: Int): GTRecipeBuilder = this.duration(minutes * NyxValues.RecipeValues.MINUTE)

    protected infix fun GTRecipeBuilder.durHour(hours: Int): GTRecipeBuilder = this.duration(hours * NyxValues.RecipeValues.HOUR)

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
