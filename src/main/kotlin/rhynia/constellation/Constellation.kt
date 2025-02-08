package rhynia.constellation

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import net.minecraft.launchwrapper.Launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import rhynia.constellation.proxy.CommonProxy

@Suppress("SpellCheckingInspection")
@Mod(
    modid = CEL_MOD_ID,
    name = CEL_MOD_NAME,
    version = Tags.VERSION,
    dependencies = "required-after:forgelin;required-after:gregtech;required-after:dreamcraft;",
    acceptedMinecraftVersions = "[1.7.10]")
class Constellation {
  companion object {
    val ModLogger: Logger by lazy { LogManager.getLogger(CEL_MOD_ID) }

    val DevEnv: Boolean by lazy { Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean }

    @JvmStatic
    @SidedProxy(
        clientSide = "rhynia.constellation.proxy.ClientProxy",
        serverSide = "rhynia.constellation.proxy.CommonProxy")
    lateinit var proxy: CommonProxy
  }

  @Mod.EventHandler fun preInit(event: FMLPreInitializationEvent) = proxy.preInit(event)

  @Mod.EventHandler fun init(event: FMLInitializationEvent) = proxy.init(event)

  @Mod.EventHandler fun postInit(event: FMLPostInitializationEvent) = proxy.postInit(event)

  @Mod.EventHandler fun completeInit(event: FMLLoadCompleteEvent) = proxy.completeInit(event)

  @Mod.EventHandler fun serverStarting(event: FMLServerStartingEvent) = proxy.serverStarting(event)
}

internal val Any.Log: Logger
  get() = Constellation.ModLogger

internal const val CEL_MOD_ID = "constellation"
internal const val CEL_MOD_NAME = "Constellation"
