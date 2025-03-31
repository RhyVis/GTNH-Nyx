package rhynia.nyx.api.enums.ref

import gregtech.api.enums.Materials.SolderingAlloy
import gtPlusPlus.core.material.MaterialMisc.MUTATED_LIVING_SOLDER
import gtPlusPlus.core.material.MaterialsAlloy.INDALLOY_140
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.api.interfaces.RefHelper

@Suppress("UNUSED", "SpellCheckingInspection")
enum class SolderMaterial : RefHelper {
    /** [SolderingAlloy] */
    T1,

    /** [INDALLOY_140] */
    T2,

    /** [MUTATED_LIVING_SOLDER] */
    T3,
    ;

    override fun getFluidStack(amount: Int): FluidStack =
        when (this) {
            T1 -> SolderingAlloy.getMolten(amount.toLong())
            T2 -> INDALLOY_140.getFluidStack(amount)
            T3 -> MUTATED_LIVING_SOLDER.getFluidStack(amount)
        }
}
