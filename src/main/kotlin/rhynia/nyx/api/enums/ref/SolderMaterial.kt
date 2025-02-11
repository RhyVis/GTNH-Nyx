package rhynia.nyx.api.enums.ref

import gregtech.api.enums.Materials
import gtPlusPlus.core.material.MaterialMisc
import gtPlusPlus.core.material.MaterialsAlloy
import net.minecraftforge.fluids.FluidStack
import rhynia.nyx.api.interfaces.RefHelper
import rhynia.nyx.common.container.NyxItemList

enum class SolderMaterial : RefHelper {
  SolderingAlloy,
  IndaAlloy,
  MutatedLivingAlloy,
  ;

  override fun getItemStack(amount: Int) = NyxItemList.TestItem01.get(1)

  override fun getFluidStack(amount: Int): FluidStack =
      when (this) {
        SolderingAlloy -> Materials.SolderingAlloy.getMolten(amount.toLong())
        IndaAlloy -> MaterialsAlloy.INDALLOY_140.getFluidStack(amount)
        MutatedLivingAlloy -> MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(amount)
      }
}
