package vis.rhynia.nova.common.tile.base

import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility
import com.gtnewhorizon.structurelib.structure.StructureUtility.isAir
import com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock
import gregtech.api.enums.HatchElement.Dynamo
import gregtech.api.enums.HatchElement.Energy
import gregtech.api.enums.HatchElement.ExoticEnergy
import gregtech.api.enums.HatchElement.InputBus
import gregtech.api.enums.HatchElement.InputHatch
import gregtech.api.enums.HatchElement.OutputBus
import gregtech.api.enums.HatchElement.OutputHatch
import gregtech.api.enums.Textures
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.render.TextureFactory
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import org.jetbrains.annotations.ApiStatus.OverrideOnly

abstract class NovaMTECubeBase<T : NovaMTEBase<T>> : NovaMTEBase<T> {
  protected constructor(
      aId: Int,
      aName: String,
      aNameRegional: String
  ) : super(aId, aName, aNameRegional)
  protected constructor(aName: String) : super(aName)

  companion object {
    private const val H_OFFSET = 1
    private const val V_OFFSET = 1
    private const val D_OFFSET = 1
  }

  @OverrideOnly protected abstract fun sCasingBlock(): Block

  @OverrideOnly protected abstract fun sCoreBlock(): Block?

  @OverrideOnly protected abstract fun sCasingIndex(): Int

  @OverrideOnly protected abstract fun sCasingBlockMeta(): Int

  @OverrideOnly protected fun sCoreBlockMeta(): Int = 0

  override fun checkMachine(
      aBaseMetaTileEntity: IGregTechTileEntity?,
      aStack: ItemStack?
  ): Boolean {
    removeMaintenance()
    return checkPiece(STRUCTURE_PIECE_MAIN, H_OFFSET, V_OFFSET, D_OFFSET)
  }

  override fun construct(stackSize: ItemStack?, hintsOnly: Boolean) {
    buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, H_OFFSET, V_OFFSET, D_OFFSET)
  }

  override fun survivalConstruct(
      stackSize: ItemStack,
      elementBudget: Int,
      env: ISurvivalBuildEnvironment
  ): Int {
    if (mMachine) return -1
    return survivialBuildPiece(
        STRUCTURE_PIECE_MAIN,
        stackSize,
        H_OFFSET,
        V_OFFSET,
        D_OFFSET,
        elementBudget,
        env,
        false,
        true)
  }

  override fun getStructureDefinition(): IStructureDefinition<T> {
    return StructureDefinition.builder<T>()
        .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
        .addElement(
            'B', if (sCoreBlock() == null) isAir() else ofBlock(sCoreBlock(), sCoreBlockMeta()))
        .addElement(
            'C',
            HatchElementBuilder.builder<T>()
                .atLeast(
                    InputBus,
                    InputHatch,
                    OutputBus,
                    OutputHatch,
                    Energy.or(ExoticEnergy),
                    Dynamo.or(ExoticDynamo))
                .adder { t, aTileEntity, aBaseCasingIndex ->
                  t.addToMachineList(aTileEntity, aBaseCasingIndex.toInt())
                }
                .dot(1)
                .casingIndex(sCasingIndex())
                .buildAndChain(sCasingBlock(), sCasingBlockMeta()))
        .build()
  }

  override fun getTexture(
      baseMetaTileEntity: IGregTechTileEntity,
      side: ForgeDirection,
      facing: ForgeDirection,
      colorIndex: Int,
      active: Boolean,
      redstoneLevel: Boolean
  ): Array<ITexture> {
    if (side != facing)
        return arrayOf(
            Textures.BlockIcons.getCasingTextureForId(
                GTUtility.getCasingTextureIndex(sCasingBlock(), sCasingBlockMeta())))
    if (active)
        return arrayOf(
            Textures.BlockIcons.getCasingTextureForId(
                GTUtility.getCasingTextureIndex(sCasingBlock(), sCasingBlockMeta())),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build())
    return arrayOf(
        Textures.BlockIcons.getCasingTextureForId(
            GTUtility.getCasingTextureIndex(sCasingBlock(), sCasingBlockMeta())),
        TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
            .extFacing()
            .glow()
            .build())
  }

  // spotless:off
  private val structureShape =
      arrayOf(
          arrayOf("CCC", "CCC", "CCC"),
          arrayOf("C~C", "CBC", "CCC"),
          arrayOf("CCC", "CCC", "CCC")
      )
  // spotless:on
}
