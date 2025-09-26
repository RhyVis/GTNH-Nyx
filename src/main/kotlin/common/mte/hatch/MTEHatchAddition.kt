package rhynia.nyx.common.mte.hatch

import gregtech.api.enums.Textures
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.modularui.IAddUIWidgets
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.implementations.MTEHatch
import gregtech.api.render.TextureFactory
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import rhynia.nyx.api.util.localized

class MTEHatchAddition :
    MTEHatch,
    IAddUIWidgets {
    constructor(id: Int, name: String) : super(id, name, "$name.name".localized(), 6, 16, arrayOf(""))

    constructor(
        aName: String?,
        aTier: Int,
        aDescription: Array<String?>?,
        aTextures: Array<Array<Array<ITexture?>?>?>?,
    ) : super(aName, aTier, 16, aDescription, aTextures)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity =
        MTEHatchAddition(mName, mTier.toInt(), mDescriptionArray, mTextures)

    override fun allowPullStack(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aIndex: Int,
        side: ForgeDirection?,
        aStack: ItemStack?,
    ): Boolean = false

    override fun allowPutStack(
        aBaseMetaTileEntity: IGregTechTileEntity?,
        aIndex: Int,
        side: ForgeDirection?,
        aStack: ItemStack?,
    ): Boolean = true

    override fun getTexturesActive(aBaseTexture: ITexture?) =
        arrayOf(aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_DATA_ACCESS))

    override fun getTexturesInactive(aBaseTexture: ITexture?) =
        arrayOf(aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_DATA_ACCESS))
}
