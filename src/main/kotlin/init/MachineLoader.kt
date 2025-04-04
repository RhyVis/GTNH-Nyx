package rhynia.nyx.init

import gregtech.api.GregTechAPI
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.util.GTUtility
import net.minecraft.util.StatCollector
import rhynia.nyx.DevEnv
import rhynia.nyx.MOD_NAME
import rhynia.nyx.api.interfaces.Loader
import rhynia.nyx.common.ItemList
import rhynia.nyx.common.NyxItemList
import rhynia.nyx.common.NyxWirelessDynamoList
import rhynia.nyx.common.NyxWirelessEnergyList
import rhynia.nyx.common.mte.base.NyxHatchWirelessDynamo
import rhynia.nyx.common.mte.base.NyxHatchWirelessEnergy
import rhynia.nyx.common.mte.prod.NyxCopier
import rhynia.nyx.config.ConfigDebug
import rhynia.nyx.config.ConfigMachine
import rhynia.nyx.config.ConfigRecipe
import java.io.File

object MachineLoader : Loader {
    private val offset by lazy { ConfigMachine.MTE_ID_OFFSET }
    private val offsetUpper get() = offset + 100

    private val offsetWirelessEnergy get() = offsetUpper - NyxWirelessEnergyList.entries.size
    private val offsetWirelessDynamo get() = offsetWirelessEnergy - NyxWirelessDynamoList.entries.size

    override fun load() {
        if (ConfigDebug.DEBUG_PRINT_MTE_IDS || DevEnv) printMteIds()
        checkOccupation()
        initialiseMachineClass()
        initExtraWirelessExtended()
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
        NyxItemList.ControllerCopier.register(NyxCopier(offset + 1, "nyx.machine.copier"), ConfigMachine.MTE_COPIER)
    }

    private fun initExtraWirelessExtended() {
        if (!ConfigRecipe.RECIPE_EASY_WIRELESS) return

        val zh = StatCollector.translateToLocal("nyx.common.amp") != "A"

        NyxWirelessEnergyList.entries.forEach { wireless ->
            wireless.register(
                NyxHatchWirelessEnergy(
                    aID = offsetWirelessEnergy + wireless.ordinal,
                    aName = "nyx.hatch.$wireless.${wireless.tier}",
                    aNameRegional =
                        if (zh) {
                            StatCollector.translateToLocalFormatted(
                                "nyx.wirelessExt.energy.name",
                                GTUtility.formatNumbers(wireless.amp.toLong()),
                                wireless.tierName,
                            )
                        } else {
                            StatCollector.translateToLocalFormatted(
                                "nyx.wirelessExt.energy.name",
                                wireless.tierName,
                                GTUtility.formatNumbers(wireless.amp.toLong()),
                            )
                        },
                    aTier = wireless.tier,
                    aAmp = wireless.amp,
                ),
            )
        }
        NyxWirelessDynamoList.entries.forEach { wireless ->
            wireless.register(
                NyxHatchWirelessDynamo(
                    aID = offsetWirelessDynamo + wireless.ordinal,
                    aName = "nyx.hatch.$wireless.${wireless.tier}",
                    aNameRegional =
                        if (zh) {
                            StatCollector.translateToLocalFormatted(
                                "nyx.wirelessExt.dynamo.name",
                                GTUtility.formatNumbers(wireless.amp.toLong()),
                                wireless.tierName,
                            )
                        } else {
                            StatCollector.translateToLocalFormatted(
                                "nyx.wirelessExt.dynamo.name",
                                wireless.tierName,
                                GTUtility.formatNumbers(wireless.amp.toLong()),
                            )
                        },
                    aTier = wireless.tier,
                    aAmp = wireless.amp,
                ),
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

    private fun ItemList.register(
        mte: IMetaTileEntity,
        condition: Boolean,
    ) {
        if (condition) {
            this.set(mte.getStackForm(1))
        }
    }

    private fun ItemList.register(mte: IMetaTileEntity) {
        this.set(mte.getStackForm(1))
    }
}
