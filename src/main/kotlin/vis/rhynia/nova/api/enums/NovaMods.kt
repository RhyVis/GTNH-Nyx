package vis.rhynia.nova.api.enums

import cpw.mods.fml.common.Loader

@Suppress("SpellCheckingInspection")
enum class NovaMods(val modId: String) {
  TwistSpaceTechnology(Names.TWIST_SPACE_TECHNOLOGY),
  BoxPlusPlus(Names.BOX_PLUS_PLUS),
  ;

  private object Names {
    const val TWIST_SPACE_TECHNOLOGY = "TwistSpaceTechnology"
    const val BOX_PLUS_PLUS = "boxplusplus"
  }

  private var modLoaded: Boolean? = null
  val resourceDomain: String
    get() = modId.lowercase()

  val isModLoaded: Boolean
    get() =
        modLoaded
            ?: run {
              modLoaded = Loader.isModLoaded(modId)
              modLoaded!!
            }
}
