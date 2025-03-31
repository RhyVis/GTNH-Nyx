package rhynia.nyx.api.enums

import com.gtnewhorizons.modularui.api.drawable.UITexture
import gregtech.api.enums.GTValues
import net.minecraft.util.EnumChatFormatting.AQUA
import net.minecraft.util.EnumChatFormatting.BLUE
import net.minecraft.util.EnumChatFormatting.DARK_BLUE
import net.minecraft.util.EnumChatFormatting.GRAY
import net.minecraft.util.EnumChatFormatting.GREEN
import net.minecraft.util.EnumChatFormatting.RED

@Suppress("unused")
object NyxValues {
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

    object TextureSets {
        // TODO: Re-draw this icon
        val Logo32: UITexture = UITexture.fullImage("nyx", "gui/picture/va_logo_32_t")
    }

    object CommonStrings {
        val NyxMagical = "${AQUA}Nyx${RED}Creation$GRAY - ${GREEN}Magical Reveal"
        val NyxNuclear = "${AQUA}Nyx${RED}Creation$GRAY - ${GREEN}Thermonuclear Reaction"
        val NyxGigaFac = "${AQUA}Nyx${RED}Creation$GRAY - ${GREEN}Giga Factory"
        val AddByNyx = "由 ${GREEN}Nyx$GRAY 添加"
        val BluePrintInfo = "如${BLUE}蓝${AQUA}图${GRAY}所示相对位置"
        val BluePrintTip = "请参考${BLUE}Structure${DARK_BLUE}Lib${GRAY}全息投影，构建主体结构"
        const val StructureTooComplex = "结构太复杂了!"
        const val ChangeModeByScrewdriver = "使用螺丝刀切换模式."
    }
}
