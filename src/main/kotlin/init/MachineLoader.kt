package rhynia.nyx.init

import gregtech.api.GregTechAPI
import rhynia.nyx.Config
import rhynia.nyx.MOD_ID
import rhynia.nyx.MOD_NAME
import rhynia.nyx.ModLogger
import rhynia.nyx.api.interfaces.Loader

object MachineLoader : Loader {
    private val offset by lazy { Config.MTE_ID_OFFSET }

    override fun load() {
        checkOccupation()
        initialiseMachineClass()
    }

    private fun checkOccupation() {
        for (i in (offset + 1)..(offset + 40)) {
            if (GregTechAPI.METATILEENTITIES[i] != null) {
                val mte = GregTechAPI.METATILEENTITIES[i]

                ModLogger.run {
                    warn("=========================================")
                    warn("           ID CRASH DETECTED             ")
                    warn("Seems that there's a conflict with ID $i")
                    warn("$MOD_NAME will preserve ${(offset + 1)..(offset + 40)} for machine ids")
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
}
