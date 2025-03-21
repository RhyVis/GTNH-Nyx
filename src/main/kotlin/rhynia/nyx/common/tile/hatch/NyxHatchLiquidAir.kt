package rhynia.nyx.common.tile.hatch

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraftforge.fluids.Fluid
import rhynia.nyx.common.tile.base.NyxHatchFluidGenerator

class NyxHatchLiquidAir : NyxHatchFluidGenerator {
    constructor(
        aID: Int,
        aName: String,
        aNameRegional: String,
        aTier: Int,
    ) : super(aID, aName, aNameRegional, aTier)

    constructor(
        aName: String,
        aTier: Int,
        aDescription: Array<String>,
        aTextures: Array<Array<Array<ITexture>>>?,
    ) : super(aName, aTier, aDescription, aTextures)

    override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): MetaTileEntity =
        NyxHatchLiquidAir(this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)

    override val customTooltip: Array<String>
        get() = arrayOf("直接将吸入的气体压缩至液态.", "每5秒填充至最大容量.", "你是在里面塞了一个奇点吗?")

    override val fluidToGenerate: Fluid
        get() = FluidUtils.getFluidStack("liquidair", 1).getFluid()
}
