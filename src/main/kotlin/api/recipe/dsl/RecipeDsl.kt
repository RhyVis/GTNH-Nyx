package rhynia.nyx.api.recipe.dsl

import gregtech.api.enums.GTValues
import gregtech.api.interfaces.IRecipeMap
import gregtech.api.util.GTRecipeBuilder
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.api.enums.RecipeValues.HOUR
import rhynia.nyx.api.enums.RecipeValues.MINUTE
import rhynia.nyx.api.enums.RecipeValues.SECOND
import rhynia.nyx.api.enums.ref.Tier
import kotlin.apply

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class RecipeDsl

/**
 * Alias for standard recipe builder in [GTValues.RA].
 */
val GRecipeBuilder: GTRecipeBuilder get() = GTValues.RA.stdBuilder()

fun withRecipeMap(
    backend: IRecipeMap,
    block: @RecipeDsl RecipeMapOperation.() -> Unit,
) {
    RecipeMapOperation(backend).apply(block)
}

@RecipeDsl
@JvmInline
value class RecipeMapOperation(
    val backend: IRecipeMap,
) {
    inline fun newRecipe(block: @RecipeDsl RecipeBuilder.() -> Unit) {
        RecipeBuilder(backend)
            .apply(block)
            .build()
    }

    inline fun newRecipeIf(
        condition: Boolean,
        block: @RecipeDsl RecipeBuilder.() -> Unit,
    ) {
        if (condition) {
            RecipeBuilder(backend)
                .apply(block)
                .build()
        }
    }

    inline fun <T> newRecipeIter(
        iter: Iterable<T>,
        block: @RecipeDsl RecipeBuilder.(T) -> Unit,
    ) {
        for (item in iter) {
            RecipeBuilder(backend)
                .apply { block(item) }
                .build()
        }
    }
}

@RecipeDsl
class RecipeBuilder(
    private val backend: IRecipeMap,
) {
    val input = ElementCollector()
    val output = ElementCollector()
    private var eut = 0L
    private var dur = 0

    inline fun input(block: @RecipeDsl ElementCollector.() -> Unit) {
        input.apply(block)
    }

    inline fun output(block: @RecipeDsl ElementCollector.() -> Unit) {
        output.apply(block)
    }

    fun eut(tier: Tier) {
        eut = tier.voltageRecipe
    }

    fun eut(value: Long) {
        eut = value
    }

    fun dur(value: Int) {
        dur = value
    }

    fun durSec(value: Int) {
        dur = value * SECOND
    }

    fun durMin(value: Int) {
        dur = value * MINUTE
    }

    fun durHour(value: Int) {
        dur = value * HOUR
    }

    fun build() {
        GRecipeBuilder
            .apply {
                itemInputs(*input.item())
                fluidInputs(*input.fluid())
                itemOutputs(*output.itemOnly())
                fluidOutputs(*output.fluid())
                eut(eut)
                duration(dur)
            }.addTo(backend)
    }

    // Backward compatibility

    fun itemInputs(vararg items: Any) {
        items.forEach {
            input.apply {
                +it
            }
        }
    }

    fun fluidInputs(vararg fluids: FluidStack) {
        fluids.forEach {
            input.apply {
                +it
            }
        }
    }

    fun itemOutputs(vararg items: Any) {
        items.forEach {
            output.apply {
                +it
            }
        }
    }

    fun fluidOutputs(vararg fluids: FluidStack) {
        fluids.forEach {
            output.apply {
                +it
            }
        }
    }
}

@RecipeDsl
@JvmInline
value class ElementCollector(
    private val objects: MutableList<Any> = mutableListOf(),
) {
    operator fun Any.unaryPlus() {
        objects.add(this)
    }

    fun add(obj: Any) {
        objects.add(obj)
    }

    fun item(): Array<Any> = objects.filter { it !is FluidStack }.toTypedArray()

    fun itemOnly(): Array<ItemStack> = objects.filterIsInstance<ItemStack>().toTypedArray()

    fun fluid(): Array<FluidStack> = objects.filterIsInstance<FluidStack>().toTypedArray()
}
