package vis.rhynia.nova.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraftforge.fluids.Fluid
import vis.rhynia.nova.common.tile.base.NovaMTEHatchFluidGenerator

class NovaMTEHatchSteam : NovaMTEHatchFluidGenerator {

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
    return NovaMTEHatchSteam(this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)
  }

  override val customTooltip: Array<String>
    get() = arrayOf("内置强劲锅炉.", "每5秒填充至最大容量.", "蓄水仓里的水都沸腾了!")

  override val fluidToGenerate: Fluid
    get() = FluidUtils.getFluidStack("steam", 1).getFluid()
}
