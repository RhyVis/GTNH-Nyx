package vis.rhynia.nova.common.block.base

import net.minecraft.entity.Entity
import net.minecraft.entity.EnumCreatureType
import net.minecraft.world.IBlockAccess
import vis.rhynia.nova.api.interfaces.block.GregTechTileInfo

open class NovaMetaBlock(rawName: String) : AbstractMetaBlock(rawName), GregTechTileInfo {
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
