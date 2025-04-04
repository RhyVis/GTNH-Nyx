@file:Suppress("UNUSED")

package rhynia.nyx.api.enums

import com.gtnewhorizons.modularui.api.drawable.UITexture
import gregtech.api.enums.GTValues
import net.minecraft.util.StatCollector

object RecipeValues {
    const val TICK = 1
    const val SECOND = 20
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE

    const val STACK = 64

    const val BUCKET = 1_000L
    const val INGOT = 144L

    val RECIPE_ULV = GTValues.VP[0]
    val RECIPE_LV = GTValues.VP[1]
    val RECIPE_MV = GTValues.VP[2]
    val RECIPE_HV = GTValues.VP[3]
    val RECIPE_EV = GTValues.VP[4]
    val RECIPE_IV = GTValues.VP[5]
    val RECIPE_LuV = GTValues.VP[6]
    val RECIPE_ZPM = GTValues.VP[7]
    val RECIPE_UV = GTValues.VP[8]
    val RECIPE_UHV = GTValues.VP[9]
    val RECIPE_UEV = GTValues.VP[10]
    val RECIPE_UIV = GTValues.VP[11]
    val RECIPE_UMV = GTValues.VP[12]
    val RECIPE_UXV = GTValues.VP[13]
    val RECIPE_MAX = GTValues.VP[14]
}

object Texture {
    // TODO: Re-draw this icon
    val Logo32: UITexture = UITexture.fullImage("nyx", "gui/picture/va_logo_32_t")
}

object CommonString {
    val NyxMagical: String by lazy { StatCollector.translateToLocal("nyx.common.NyxMagical") }
    val NyxNuclear: String by lazy { StatCollector.translateToLocal("nyx.common.NyxNuclear") }
    val NyxGigaFac: String by lazy { StatCollector.translateToLocal("nyx.common.NyxGigaFac") }
    val AddByNyx: String by lazy { StatCollector.translateToLocal("nyx.common.AddByNyx") }
    val BluePrintInfo: String by lazy { StatCollector.translateToLocal("nyx.common.BluePrintInfo") }
    val BluePrintTip: String by lazy { StatCollector.translateToLocal("nyx.common.BluePrintTip") }
    val StructureTooComplex: String by lazy { StatCollector.translateToLocal("nyx.common.StructureTooComplex") }
    val ChangeModeByScrewdriver: String by lazy { StatCollector.translateToLocal("nyx.common.ChangeModeByScrewdriver") }
}
