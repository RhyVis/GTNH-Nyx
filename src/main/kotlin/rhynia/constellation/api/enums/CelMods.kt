package rhynia.constellation.api.enums

import cpw.mods.fml.common.Loader

@Suppress("SpellCheckingInspection", "unused")
enum class CelMods(val modId: String) {
  TwistSpaceTechnology("TwistSpaceTechnology"),
  BoxPlusPlus("boxplusplus"),
  ;

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
