package rhynia.nyx.api.interfaces.block

/**
 * Specifies that the block has common lines like
 *
 * `Mobs cannot Spawn on this Block`
 *
 * and
 *
 * `This is NOT a TileEntity!`
 *
 * in the tooltip. But actually this two functions is disabled by default.
 */
interface GregTechTileInfo {
    val infoNoMobSpawn: Boolean
        get() = true

    val infoNotTileEntity: Boolean
        get() = true
}
