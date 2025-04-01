package rhynia.nyx.init

import gregtech.api.GregTechAPI
import rhynia.nyx.Config
import rhynia.nyx.DevEnv
import rhynia.nyx.MOD_ID
import rhynia.nyx.MOD_NAME
import rhynia.nyx.ModLogger
import rhynia.nyx.api.interfaces.Loader
import java.io.File

object MachineLoader : Loader {
    private val offset by lazy { Config.MTE_ID_OFFSET }

    override fun load() {
        if (Config.DEBUG_PRINT_MTE_IDS || DevEnv) printMteIds()
        checkOccupation()
        initialiseMachineClass()
    }

    private fun checkOccupation() {
        val range = (offset + 1)..(offset + 40)
        for (i in range) {
            if (GregTechAPI.METATILEENTITIES[i] != null) {
                val mte = GregTechAPI.METATILEENTITIES[i]

                ModLogger.run {
                    warn("=========================================")
                    warn("           ID CRASH DETECTED             ")
                    warn("Seems that there's a conflict with ID $i")
                    warn("$MOD_NAME will preserve $range for machine ids")
                    warn("Now ID $i is occupied by ${mte.localName} form ${mte.javaClass}")
                    warn("If this is an MTE from GT:NH, report this to $MOD_NAME's author")
                    warn("If from another self-installed mod, you can change the offset in config")
                    warn("It's named $MOD_ID.cfg in your config folder")
                }

                throw IllegalStateException("ID $i preserved by $MOD_NAME is occupied by ${mte.localName}")
            }
        }
    }

    private fun initialiseMachineClass() {
    }

    private fun printMteIds() {
        val ls = mutableListOf<Pair<Int, String>>()
        GregTechAPI.METATILEENTITIES.forEachIndexed { i, mte ->
            if (mte != null) ls.add(i to "${mte.localName}(${mte.javaClass.simpleName})")
        }
        ls.sortBy { it.first }
        ls.forEach {
            ModLogger.warn("MTE: ${it.first}: ${it.second}")
        }
        File("loaded_mte_ids.txt").writeText(
            ls.joinToString("\n") { "${it.first}: ${it.second}" },
        )
    }
}
