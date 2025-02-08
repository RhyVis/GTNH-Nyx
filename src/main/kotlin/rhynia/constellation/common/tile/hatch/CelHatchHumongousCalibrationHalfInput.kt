package rhynia.constellation.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.metatileentity.implementations.MTEHatchInput

class CelHatchHumongousCalibrationHalfInput : MTEHatchInput {

  constructor(
      aID: Int,
      aName: String,
      aNameRegional: String
  ) : super(aID, aName, aNameRegional, 13) {
    this.mDescriptionArray[1] = "容量: 500,000,000L"
  }

  constructor(
      aName: String?,
      aTier: Int,
      aDescription: Array<String>,
      aTextures: Array<Array<Array<ITexture>>>
  ) : super(aName, aTier, aDescription, aTextures)

  override fun getCapacity(): Int {
    return 500000000
  }

  override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity {
    return CelHatchHumongousCalibrationHalfInput(
        this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)
  }
}
