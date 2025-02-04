package vis.rhynia.nova.common.block.base

import gregtech.api.GregTechAPI
import net.minecraft.block.Block
import net.minecraft.world.World

/**
 * A block that is a machine block. Causes machine updates when added or broken.
 *
 * @param rawName The raw name of the block.
 */
open class NovaMetaMachine(rawName: String) : NovaMetaBlock(rawName) {
  init {
    GregTechAPI.registerMachineBlock(this, -1)
  }

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
