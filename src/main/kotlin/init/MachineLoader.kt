package rhynia.nyx.init

import gregtech.api.GregTechAPI
import gregtech.api.util.GTUtility
import net.minecraft.util.StatCollector
import rhynia.nyx.Config
import rhynia.nyx.DevEnv
import rhynia.nyx.MOD_NAME
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.NyxWirelessHatchList
import rhynia.nyx.common.tile.base.NyxHatchWirelessMultiExtended
import java.io.File

object MachineLoader : Loader {
    private val offset by lazy { Config.MTE_ID_OFFSET }

    override fun load() {
        if (Config.DEBUG_PRINT_MTE_IDS || DevEnv) printMteIds()
        checkOccupation()
        initialiseMachineClass()
        initExtraWirelessLaser()
    }

    private fun checkOccupation() {
        val range = (offset + 1)..(offset + 100)
        val checked = mutableListOf<Pair<Int, String>>()
        for (i in range) {
            if (GregTechAPI.METATILEENTITIES[i] != null) {
                val mte = GregTechAPI.METATILEENTITIES[i]
                if (mte != null) checked.add(i to "${mte.localName}(${mte.javaClass.name})")
            }
        }
        if (checked.isNotEmpty()) {
            throw IllegalStateException(
                "ID $range preserved by $MOD_NAME is occupied by\n ${checked.joinToString(", \n")
                    { "${it.first}: ${it.second}" }}",
            )
        }
    }

    private fun initialiseMachineClass() {
    }

    private fun initExtraWirelessLaser() {
        if (!Config.RECIPE_EASY_WIRELESS) return
        val initialOffset = Config.MTE_ID_OFFSET + 99 - NyxWirelessHatchList.entries.size
        NyxWirelessHatchList.entries.forEach { wireless ->
            wireless.set(
                NyxHatchWirelessMultiExtended(
                    aID = initialOffset + wireless.ordinal,
                    aName = "nyx.hatch.$wireless.${wireless.tier}",
                    aNameRegional =
                        StatCollector.translateToLocalFormatted(
                            "nyx.wirelessExt.name",
                            GTUtility.formatNumbers(wireless.amp.toLong()),
                            wireless.tierName,
                        ),
                    aTier = wireless.tier,
                    aAmp = wireless.amp,
                ).getStackForm(1),
            )
        }
    }

    private fun printMteIds() {
        buildList<Pair<Int, String>> {
            GregTechAPI.METATILEENTITIES.forEachIndexed { i, mte ->
                if (mte != null) add(i to "${mte.localName}(${mte.javaClass.simpleName})")
            }
            sortBy { it.first }
        }.let { list ->
            File("loaded_mte_ids.txt").writeText(
                list.joinToString("\n") { "${it.first}: ${it.second}" },
            )
        }
    }
}
