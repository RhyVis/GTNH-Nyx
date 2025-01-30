package vis.rhynia.nova.api.util

import kotlin.collections.indices
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

object FluidUtil {
  fun getFluidStackByName(fluidName: String, amount: Int): FluidStack {
    val fluid: Fluid? = FluidRegistry.getFluid(fluidName)
    return fluid?.let { FluidStack(it, amount) }
        ?: throw IllegalArgumentException("Null fluid found with $fluidName")
  }

  fun fluidStackEqualAbsolutely(fsa1: Array<FluidStack>, fsa2: Array<FluidStack>): Boolean {
    if (fsa1.size != fsa2.size) return false
    for (i in fsa1.indices) {
      if (!fluidEqual(fsa1[i], fsa2[i])) return false
      if (fsa1[i].amount != fsa2[i].amount) return false
    }
    return true
  }

  fun fluidStackEqualFuzzy(fsa1: Array<FluidStack>, fsa2: Array<FluidStack>): Boolean {
    if (fsa1.size != fsa2.size) return false
    for (fluidStack1 in fsa1) {
      var flag = false
      for (fluidStack2 in fsa2) {
        if (fluidEqual(fluidStack1, fluidStack2)) {
          flag = true
          break
        }
      }
      if (!flag) return false
    }
    return true
  }

  fun fluidEqual(a: FluidStack, b: FluidStack): Boolean = a.fluidID == b.fluidID

  fun Fluid.idEqual(other: Fluid): Boolean = this.id == other.id

  fun FluidStack.idEqual(other: FluidStack): Boolean = this.fluidID == other.fluidID

  fun FluidStack.idEqual(other: Fluid): Boolean = this.fluidID == other.id
}
