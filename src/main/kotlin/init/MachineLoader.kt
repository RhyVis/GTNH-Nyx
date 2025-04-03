package rhynia.nyx.init

import gregtech.api.GregTechAPI
import rhynia.nyx.Config
import rhynia.nyx.DevEnv
import rhynia.nyx.MOD_NAME
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
