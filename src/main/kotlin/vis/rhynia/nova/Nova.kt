package vis.rhynia.nova

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
import vis.rhynia.nova.proxy.CommonProxy

@Suppress("SpellCheckingInspection")
@Mod(
    modid = Constant.MOD_ID,
    name = Constant.MOD_NAME,
    version = Tags.VERSION,
    dependencies = "required-after:forgelin;required-after:gregtech;required-after:dreamcraft;",
    acceptedMinecraftVersions = "[1.7.10]")
class Nova {
  companion object {
    val NovaModLogger: Logger by lazy { LogManager.getLogger(Constant.MOD_ID) }

    val DevEnv: Boolean by lazy { Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean }

    @JvmStatic
    @SidedProxy(
        clientSide = "vis.rhynia.nova.proxy.ClientProxy",
        serverSide = "vis.rhynia.nova.proxy.CommonProxy")
    lateinit var proxy: CommonProxy
  }

  @Mod.EventHandler fun preInit(event: FMLPreInitializationEvent) = proxy.preInit(event)

  @Mod.EventHandler fun init(event: FMLInitializationEvent) = proxy.init(event)

  @Mod.EventHandler fun postInit(event: FMLPostInitializationEvent) = proxy.postInit(event)

  @Mod.EventHandler fun completeInit(event: FMLLoadCompleteEvent) = proxy.completeInit(event)

  @Mod.EventHandler fun serverStarting(event: FMLServerStartingEvent) = proxy.serverStarting(event)
}

val Any.Log: Logger
  get() = Nova.NovaModLogger
