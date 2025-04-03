package rhynia.nyx.common.material

import gregtech.api.enums.FluidState.GAS
import gregtech.api.enums.FluidState.LIQUID
import gregtech.api.enums.FluidState.MOLTEN
import gregtech.api.enums.FluidState.PLASMA
import gregtech.api.enums.TextureSet
import rhynia.nyx.ModLogger
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.material.generation.NyxMaterial

@Suppress("UNUSED", "SpellCheckingInspection")
object NyxMaterials : Loader {
    override fun load() {
        ModLogger.info("Adding materials by Nyx...")
    }

    /** Null material, used for some special cases. */
    val Null =
        NyxMaterial(0, "null", shortArrayOf(250, 250, 250, 255)) {
            skipRecipeGeneration = true
            textureSet = TextureSet.SET_NONE
            protons = 999
            mass = 999
            addElementalTooltip("N")
            enableDusts()
            enableFluids(LIQUID to 1111, MOLTEN to 3333, GAS to 6666, PLASMA to 9999)
            enableGems()
            enableIngots()
            enablePlates()
            enableMisc()
        }

    val Restone =
        NyxMaterial(1, "restone", MaterialColors.RED) {
            textureSet = TextureSet.SET_POWDER
            protons = 43
            mass = 43
            addElementalTooltip("R")
            enableDusts()
            enableGems()
            enableFluids(MOLTEN to 1241)
        }
}
