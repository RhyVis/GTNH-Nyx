package rhynia.nyx.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraftforge.fluids.Fluid
import rhynia.nyx.common.tile.base.NyxHatchFluidGenerator

class NyxHatchOil : NyxHatchFluidGenerator {

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
    return NyxHatchOil(this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)
  }

  override val customTooltip: Array<String>
    get() = arrayOf("地下的油泉，就在你家门口!", "每5秒填充至最大容量.", "别让美国人知道了.")

  override val fluidToGenerate: Fluid
    get() = FluidUtils.getFluidStack("liquid_medium_oil", 1).getFluid()
}
