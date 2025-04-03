package rhynia.nyx.common.material

import gregtech.api.interfaces.IColorModulationContainer

@Suppress("UNUSED", "SpellCheckingInspection")
enum class MaterialColors(
    val rgba: ShortArray,
) : IColorModulationContainer {
    // Basic colors
    BLACK(shortArrayOf(0, 0, 0, 255)),
    WHITE(shortArrayOf(255, 255, 255, 255)),
    RED(shortArrayOf(255, 0, 0, 255)),
    GREEN(shortArrayOf(0, 255, 0, 255)),
    BLUE(shortArrayOf(0, 0, 255, 255)),
    YELLOW(shortArrayOf(255, 255, 0, 255)),
    CYAN(shortArrayOf(0, 255, 255, 255)),
    MAGENTA(shortArrayOf(255, 0, 255, 255)),

    // Gray colors
    LIGHT_GRAY(shortArrayOf(192, 192, 192, 255)),
    GRAY(shortArrayOf(128, 128, 128, 255)),
    DARK_GRAY(shortArrayOf(64, 64, 64, 255)),

    // Metal colors
    GOLD(shortArrayOf(255, 215, 0, 255)),
    SILVER(shortArrayOf(192, 192, 192, 255)),
    COPPER(shortArrayOf(184, 115, 51, 255)),
    BRONZE(shortArrayOf(205, 127, 50, 255)),
    IRON(shortArrayOf(112, 128, 144, 255)),
    STEEL(shortArrayOf(176, 196, 222, 255)),

    // Gem colors
    RUBY(shortArrayOf(224, 17, 95, 255)),
    SAPPHIRE(shortArrayOf(15, 82, 186, 255)),
    EMERALD(shortArrayOf(0, 138, 0, 255)),
    AMETHYST(shortArrayOf(153, 102, 204, 255)),
    DIAMOND(shortArrayOf(185, 242, 255, 255)),

    // Misc colors
    PURPLE(shortArrayOf(128, 0, 128, 255)),
    ORANGE(shortArrayOf(255, 165, 0, 255)),
    BROWN(shortArrayOf(165, 42, 42, 255)),
    LIME(shortArrayOf(50, 205, 50, 255)),
    PINK(shortArrayOf(255, 192, 203, 255)),
    NAVY(shortArrayOf(0, 0, 128, 255)),
    TEAL(shortArrayOf(0, 128, 128, 255)),
    OLIVE(shortArrayOf(128, 128, 0, 255)),
    MAROON(shortArrayOf(128, 0, 0, 255)),
    AQUA(shortArrayOf(0, 255, 255, 255)),

    // Radioactive colors
    URANIUM(shortArrayOf(50, 240, 50, 255)),
    PLUTONIUM(shortArrayOf(240, 50, 50, 255)),
    NAQUADAH(shortArrayOf(30, 30, 30, 255)),
    NEUTRONIUM(shortArrayOf(250, 250, 250, 255)),
    ;

    override fun getRGBA(): ShortArray = rgba

    companion object {
        fun fromRGB(
            r: Int,
            g: Int,
            b: Int,
        ): ShortArray = shortArrayOf(r.toShort(), g.toShort(), b.toShort(), 255)
    }
}
