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
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import rhynia.nyx.proxy.CommonProxy

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
