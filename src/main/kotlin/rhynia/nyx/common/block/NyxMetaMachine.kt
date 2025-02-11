package rhynia.nyx.common.block

import gregtech.api.GregTechAPI
import net.minecraft.block.Block
import net.minecraft.world.World
import org.jetbrains.annotations.ApiStatus

/**
 * A block that is a machine block such as casing. Causes machine updates when added or broken.
 *
 * @param rawName The raw name of the block.
 */
open class NyxMetaMachine(rawName: String) : NyxMetaBlock(rawName) {
  init {
    GregTechAPI.registerMachineBlock(this, -1)
    setHarvestLevel("wrench", wrenchLevel)
    setHardness(9.0f)
    setResistance(5.0f)
  }

  protected val wrenchLevel: Int
    @ApiStatus.OverrideOnly get() = 1

  override fun onBlockAdded(aWorld: World, aX: Int, aY: Int, aZ: Int) {
    updateMachine(aWorld, aX, aY, aZ)
  }

  override fun breakBlock(
      aWorld: World,
      aX: Int,
      aY: Int,
      aZ: Int,
      aBlock: Block?,
      aMetaData: Int
  ) {
    updateMachine(aWorld, aX, aY, aZ)
  }

  private fun updateMachine(aWorld: World, aX: Int, aY: Int, aZ: Int) {
    if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
      GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ)
    }
  }
}
