package vis.rhynia.nova.api.interfaces.block

interface GregTechTileInfo {
  val infoNoMobSpawn: Boolean
    get() = true

  val infoNotTileEntity: Boolean
    get() = true
}
