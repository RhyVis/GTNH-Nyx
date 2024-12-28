package vis.rhynia.nova.api.enums.ref

import gregtech.api.enums.Materials
import gtPlusPlus.core.material.MaterialMisc
import gtPlusPlus.core.material.MaterialsAlloy
import net.minecraftforge.fluids.FluidStack
import vis.rhynia.nova.api.interfaces.RefHelper
import vis.rhynia.nova.common.NovaItemList

enum class SolderMaterial : RefHelper {
  SolderingAlloy,
  IndaAlloy,
  MutatedLivingAlloy,
  ;

  override fun getItemStack(amount: Int) = NovaItemList.TestItem01.get(1)

  override fun getFluidStack(amount: Int): FluidStack =
      when (this) {
        SolderingAlloy -> Materials.SolderingAlloy.getMolten(amount.toLong())
        IndaAlloy -> MaterialsAlloy.INDALLOY_140.getFluidStack(amount)
        MutatedLivingAlloy -> MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(amount)
      }
}
