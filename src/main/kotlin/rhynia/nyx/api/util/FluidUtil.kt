package rhynia.nyx.api.util

import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

@Suppress("unused")
object FluidUtil {
  fun getFluidStack(registryName: String, amount: Int): FluidStack {
    return FluidRegistry.getFluid(registryName)?.let { FluidStack(it, amount) }
        ?: throw IllegalArgumentException("Null fluid found with $registryName")
  }

  fun Fluid.idEqual(other: Fluid): Boolean = this.id == other.id

  fun FluidStack.idEqual(other: FluidStack): Boolean = this.fluidID == other.fluidID

  fun FluidStack.idEqual(other: Fluid): Boolean = this.fluidID == other.id
}
