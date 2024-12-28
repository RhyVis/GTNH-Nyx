package vis.rhynia.nova.api.interfaces

import net.minecraftforge.fluids.Fluid

interface ProcessExtension {
  fun consumeFluid(fluid: Fluid, amount: Int): Boolean
}
