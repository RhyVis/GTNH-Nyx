package rhynia.nyx.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraftforge.fluids.Fluid
import rhynia.nyx.common.tile.base.NyxHatchFluidGenerator

class NyxHatchDistilledWater : NyxHatchFluidGenerator {

  constructor(
      aID: Int,
      aName: String,
      aNameRegional: String,
      aTier: Int
  ) : super(aID, aName, aNameRegional, aTier)

  constructor(
      aName: String,
      aTier: Int,
      aDescription: Array<String>,
      aTextures: Array<Array<Array<ITexture>>>?
  ) : super(aName, aTier, aDescription, aTextures)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity {
    return NyxHatchDistilledWater(
        this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)
  }

  override val customTooltip: Array<String>
    get() = arrayOf("水蓄积其中时即被彻底净化.", "每5秒填充至最大容量.", "感觉不如太空电梯.")

  @Suppress("SpellCheckingInspection")
  override val fluidToGenerate: Fluid
    get() = FluidUtils.getFluidStack("ic2distilledwater", 1).getFluid()
}
