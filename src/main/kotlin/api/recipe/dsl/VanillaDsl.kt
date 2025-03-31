package rhynia.nyx.api.recipe.dsl

import gregtech.api.interfaces.IItemContainer
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack

fun shapedRecipe(
    out: ItemStack,
    bitmask: Long = 0,
    block: ShapedRecipeBuilder.() -> Unit,
) = ShapedRecipeBuilder().apply(block).build(out, bitmask)

class ShapedRecipeBuilder(
    private var row1: String = "",
    private var row2: String = "",
    private var row3: String = "",
    private val defs: MutableList<Pair<Char, Any>> = mutableListOf(),
) {
    fun shape(vararg shape: String) {
        when (shape.size) {
            1 -> row1 = shape[0]
            2 -> {
                row1 = shape[0]
                row2 = shape[1]
            }

            3 -> {
                row1 = shape[0]
                row2 = shape[1]
                row3 = shape[2]
            }

            else -> throw IllegalArgumentException("Shape must be 1, 2 or 3 rows")
        }
    }

    fun def(
        symbol: Char,
        stack: ItemStack,
    ) {
        defs.add(symbol to stack)
    }

    fun def(
        symbol: Char,
        stack: IItemContainer,
    ) {
        defs.add(symbol to stack)
    }

    private val args =
        buildList {
            if (row1.isNotEmpty()) add(row1)
            if (row2.isNotEmpty()) add(row2)
            if (row3.isNotEmpty()) add(row3)
            defs.forEach { (symbol, stack) ->
                add(symbol)
                add(stack)
            }
        }

    fun build(
        out: ItemStack,
        bitmask: Long,
    ) {
        GTModHandler.addCraftingRecipe(out, bitmask, args.toTypedArray())
    }
}
