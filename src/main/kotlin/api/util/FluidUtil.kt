package rhynia.nyx.api.util

import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack

infix fun Fluid.idEqual(other: Fluid): Boolean = this.id == other.id

infix fun Fluid.idEqual(other: FluidStack): Boolean = this.id == other.fluidID

infix fun FluidStack.idEqual(other: Fluid): Boolean = this.fluidID == other.id

infix fun FluidStack.idEqual(other: FluidStack): Boolean = this.fluidID == other.fluidID

infix fun FluidStack.size(size: Int): FluidStack = apply { amount = size }
