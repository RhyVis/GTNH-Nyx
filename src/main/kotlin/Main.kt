package rhynia.nyx

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import rhynia.nyx.proxy.CommonProxy
import java.io.File

internal const val MOD_ID = "nyx"
internal const val MOD_NAME = "Nyx"

internal val DevEnv: Boolean by lazy { Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean }
internal val ModLogger: Logger by lazy { LogManager.getLogger(MOD_NAME) }

@Suppress("SpellCheckingInspection")
@Mod(
    modid = MOD_ID,
    name = MOD_NAME,
    version = Tags.VERSION,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    dependencies =
        "required-after:forgelin;" +
            "required-after:gregtech;" +
            "required-after:bartworks;" +
            "required-after:dreamcraft;" +
            "required-after:GalaxySpace;" +
            "required-after:galacticgreg;" +
            "required-after:gtnhintergalactic;" +
            "after:*;",
    acceptedMinecraftVersions = "[1.7.10]",
)
object Nyx {
    @JvmStatic
    @SidedProxy(
        clientSide = "rhynia.nyx.proxy.ClientProxy",
        serverSide = "rhynia.nyx.proxy.CommonProxy",
    )
    lateinit var proxy: CommonProxy

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) = proxy.preInit(event)

    @EventHandler
    fun init(event: FMLInitializationEvent) = proxy.init(event)

    @EventHandler
    fun postInit(event: FMLPostInitializationEvent) = proxy.postInit(event)

    @EventHandler
    fun completeInit(event: FMLLoadCompleteEvent) = proxy.completeInit(event)

    @EventHandler
    fun serverStarting(event: FMLServerStartingEvent) = proxy.serverStarting(event)
}

@Suppress("ktlint:standard:property-naming")
object Config {
    var MTE_ID_OFFSET: Int = 17850
        private set

    var DEBUG_PRINT_MOD_LIST = false
        private set
    var DEBUG_PRINT_MTE_IDS = false
        private set

    private const val CATEGORY_MACHINE = "machine"
    private const val CATEGORY_DEBUG = "debug"

    fun syncConfig(configFile: File) {
        Configuration(configFile).run {
            MTE_ID_OFFSET =
                getInt(
                    "MTE_ID_OFFSET",
                    CATEGORY_MACHINE,
                    17850,
                    0,
                    Short.MAX_VALUE.toInt(),
                    "MTE ID Offset, using to solve conflicts with other custom mods " +
                        "!!!DANGER TO CHANGE IN EXISTING SAVE!!!",
                )

            DEBUG_PRINT_MOD_LIST =
                getBoolean(
                    "DEBUG_PRINT_MOD_LIST",
                    CATEGORY_DEBUG,
                    false,
                    "Print all loaded mods to the console.",
                )
            DEBUG_PRINT_MTE_IDS =
                getBoolean(
                    "DEBUG_PRINT_MTE_IDS",
                    CATEGORY_DEBUG,
                    false,
                    "Print all MTE IDs to the console.",
                )
            if (hasChanged()) save()
        }
    }
}
