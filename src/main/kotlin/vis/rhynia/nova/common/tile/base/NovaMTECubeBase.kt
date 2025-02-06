package vis.rhynia.nova.common.tile.base

import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.IStructureElement
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.util.GTUtility
import gregtech.api.util.HatchElementBuilder
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import org.jetbrains.annotations.ApiStatus.OverrideOnly

abstract class NovaMTECubeBase<T : NovaMTEBase<T>> : NovaMTEBase<T> {

  protected constructor(
      aId: Int,
      aName: String,
      aNameRegional: String
  ) : super(aId, aName, aNameRegional)
  protected constructor(aName: String) : super(aName)

  protected companion object {
    const val H_OFFSET = 1
    const val V_OFFSET = 1
    const val D_OFFSET = 0
  }

  /** The block and corresponding meta used for the casing of the machine. */
  protected abstract val sCasingBlock: Pair<Block, Int>

  /** The index of the casing texture, usually calculated from the block and meta. */
  protected open val sCasingIndex: Int
    @OverrideOnly get() = GTUtility.getCasingTextureIndex(sCasingBlock.first, sCasingBlock.second)

  /** The block used for the core of the machine, leave null if no core block is needed. */
  protected open val sCoreBlock: Pair<Block, Int>?
    @OverrideOnly get() = null

  protected val sCoreBlockRef: IStructureElement<T>
    get() = sCoreBlock?.let { ofBlock(it.first, it.second) } ?: isAir()

  final override val sControllerBlock: Pair<Block, Int>
    get() = sCasingBlock

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

  override fun genStructureDefinition(): IStructureDefinition<T> =
      StructureDefinition.builder<T>()
          .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(structureShape))
          .addElement('B', sCoreBlockRef)
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
                  .casingIndex(sCasingIndex)
                  .buildAndChain(sCasingBlock.first, sCasingBlock.second))
          .build()

  protected val structureShape: Array<Array<String>>
    get() =
        arrayOf(
            arrayOf("CCC", "CCC", "CCC"),
            arrayOf("C~C", "CBC", "CCC"),
            arrayOf("CCC", "CCC", "CCC"))
}
