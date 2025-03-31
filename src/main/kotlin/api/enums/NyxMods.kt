package rhynia.nyx.api.enums

import cpw.mods.fml.common.Loader

@Suppress("UNUSED", "SpellCheckingInspection", "PropertyName")
enum class NyxMods(
    val ID: String,
) {
    TwistSpaceTechnology("TwistSpaceTechnology"),
    BoxPlusPlus("boxplusplus"),
    ;

    val isModLoaded: Boolean by lazy { Loader.isModLoaded(ID) }

    val resourceDomain: String get() = ID.lowercase()
}
