@file:Suppress("UNUSED", "NOTHING_TO_INLINE")

package rhynia.nyx.api.util

import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack

inline infix fun Fluid.idEqual(other: Fluid): Boolean = this.id == other.id

inline infix fun Fluid.idEqual(other: FluidStack): Boolean = this.id == other.fluidID

inline infix fun FluidStack.idEqual(other: Fluid): Boolean = this.fluidID == other.id

inline infix fun FluidStack.idEqual(other: FluidStack): Boolean = this.fluidID == other.fluidID

inline infix fun FluidStack.size(size: Int): FluidStack = apply { amount = size }
