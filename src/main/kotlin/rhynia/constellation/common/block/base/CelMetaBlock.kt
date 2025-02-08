package rhynia.constellation.common.block.base

import net.minecraft.entity.Entity
import net.minecraft.entity.EnumCreatureType
import net.minecraft.world.IBlockAccess
import rhynia.constellation.api.interfaces.block.GregTechTileInfo

/**
 * Normal block with meta as variant.
 *
 * @param rawName the raw name of the block
 */
open class CelMetaBlock(rawName: String) : AbstractMetaBlock(rawName), GregTechTileInfo {
  override fun canBeReplacedByLeaves(world: IBlockAccess?, x: Int, y: Int, z: Int): Boolean = false

  override fun canEntityDestroy(
      world: IBlockAccess?,
      x: Int,
      y: Int,
      z: Int,
      entity: Entity?
  ): Boolean = false

  override fun canCreatureSpawn(
      type: EnumCreatureType?,
      world: IBlockAccess?,
      x: Int,
      y: Int,
      z: Int
  ): Boolean = false
}
