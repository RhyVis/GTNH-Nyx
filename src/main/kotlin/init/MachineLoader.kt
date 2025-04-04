package rhynia.nyx.init

import gregtech.api.GregTechAPI
import gregtech.api.interfaces.IItemContainer
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.util.GTUtility
import net.minecraft.util.StatCollector
import rhynia.nyx.DevEnv
import rhynia.nyx.MOD_NAME
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.NyxWirelessHatchList
import rhynia.nyx.common.mte.base.NyxHatchWirelessMultiExtended
import rhynia.nyx.common.mte.prod.NyxCopier
import rhynia.nyx.config.ConfigDebug
import rhynia.nyx.config.ConfigMachine
import rhynia.nyx.config.ConfigRecipe
import java.io.File

object MachineLoader : Loader {
    private val offset by lazy { ConfigMachine.MTE_ID_OFFSET }

    override fun load() {
        if (ConfigDebug.DEBUG_PRINT_MTE_IDS || DevEnv) printMteIds()
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
        NyxItemList.ControllerCopier.register(NyxCopier(offset + 1, "nyx.machine.copier"))
    }

    private fun initExtraWirelessLaser() {
        if (!ConfigRecipe.RECIPE_EASY_WIRELESS) return
        val initialOffset = offset + 100 - NyxWirelessHatchList.entries.size // Keep last one id free

        val zh = StatCollector.translateToLocal("nyx.common.amp") != "A"

        NyxWirelessHatchList.entries.forEach { wireless ->
            wireless.set(
                NyxHatchWirelessMultiExtended(
                    aID = initialOffset + wireless.ordinal,
                    aName = "nyx.hatch.$wireless.${wireless.tier}",
                    aNameRegional =
                        if (zh) {
                            StatCollector.translateToLocalFormatted(
                                "nyx.wirelessExt.name",
                                GTUtility.formatNumbers(wireless.amp.toLong()),
                                wireless.tierName,
                            )
                        } else {
                            StatCollector.translateToLocalFormatted(
                                "nyx.wirelessExt.name",
                                wireless.tierName,
                                GTUtility.formatNumbers(wireless.amp.toLong()),
                            )
                        },
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

    private fun IItemContainer.register(mte: IMetaTileEntity) {
        this.set(mte.getStackForm(1))
    }
}
