package rhynia.nyx.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.metatileentity.implementations.MTEHatchInput

class NyxMTEHatchHumongousCalibrationInput : MTEHatchInput {
    constructor(
        aID: Int,
        aName: String,
        aNameRegional: String,
    ) : super(aID, aName, aNameRegional, 13) {
        this.mDescriptionArray[1] = "容量: 1,000,000,000L"
    }

    constructor(
        aName: String?,
        aTier: Int,
        aDescription: Array<String>,
        aTextures: Array<Array<Array<ITexture>>>,
    ) : super(aName, aTier, aDescription, aTextures)

    override fun getCapacity(): Int = 1000000000

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity =
        NyxMTEHatchHumongousCalibrationInput(
            this.mName,
            this.mTier.toInt(),
            this.mDescriptionArray,
            this.mTextures,
        )
}
