package rhynia.constellation.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraftforge.fluids.Fluid
import rhynia.constellation.common.tile.base.CelHatchFluidGenerator

class CelHatchLubricant : CelHatchFluidGenerator {

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
      aTextures: Array<Array<Array<ITexture>>>
  ) : super(aName, aTier, aDescription, aTextures)

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity {
    return CelHatchLubricant(this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)
  }

  override val customTooltip: Array<String>
    get() = arrayOf("油泉仓的进一步进化.", "每5秒填充至最大容量.", "所有的机械都不缺润滑油了!")

  override val fluidToGenerate: Fluid
    get() = FluidUtils.getFluidStack("lubricant", 1).getFluid()
}
