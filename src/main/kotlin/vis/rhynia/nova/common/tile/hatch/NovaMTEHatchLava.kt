package vis.rhynia.nova.common.tile.hatch

import gregtech.api.enums.Textures
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.metatileentity.MetaTileEntity
import gregtech.api.objects.GTRenderedTexture
import gtPlusPlus.core.util.minecraft.FluidUtils
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid
import vis.rhynia.nova.common.tile.base.NovaMTEHatchFluidGenerator

class NovaMTEHatchLava : NovaMTEHatchFluidGenerator {

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
    return NovaMTEHatchLava(this.mName, this.mTier.toInt(), this.mDescriptionArray, this.mTextures)
  }

  override fun getCustomTooltip(): Array<String?> {
    val aTooltip = arrayOfNulls<String>(4)
    aTooltip[0] = "熔岩从虚空涌现至这一方仓室中."
    aTooltip[1] = "每5秒填充至最大容量."
    aTooltip[2] = "炼狱之壶的工业化."
    return aTooltip
  }

  override fun getFluidToGenerate(): Fluid? {
    return FluidUtils.getFluidStack("lava", 1).getFluid()
  }

  override fun getAmountOfFluidToGenerate(): Int {
    return 2000000000
  }

  override fun getMaxTickTime(): Int {
    return 100
  }

  override fun getCapacity(): Int {
    return 2000000000
  }

  override fun doesHatchMeetConditionsToGenerate(): Boolean {
    return true
  }

  override fun generateParticles(aWorld: World?, name: String?) {}

  override fun getTexturesActive(aBaseTexture: ITexture?): Array<ITexture?> {
    return arrayOf<ITexture?>(aBaseTexture, GTRenderedTexture(Textures.BlockIcons.OVERLAY_FUSION1))
  }

  override fun getTexturesInactive(aBaseTexture: ITexture?): Array<ITexture?> {
    return arrayOf<ITexture?>(aBaseTexture, GTRenderedTexture(Textures.BlockIcons.OVERLAY_FUSION1))
  }
}
