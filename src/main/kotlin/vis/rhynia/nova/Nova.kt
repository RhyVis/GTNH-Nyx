package vis.rhynia.nova

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import vis.rhynia.nova.proxy.CommonProxy

@Mod(
    modid = Constant.MOD_ID,
    name = Constant.MOD_NAME,
    version = Tags.VERSION,
    acceptedMinecraftVersions = "[1.7.10]")
class Nova {
  companion object {
    @JvmStatic val LOG: Logger = LogManager.getLogger(Constant.MOD_ID)

    @JvmStatic
    @SidedProxy(
        clientSide = "vis.rhynia.nova.proxy.ClientProxy",
        serverSide = "vis.rhynia.nova.proxy.CommonProxy")
    lateinit var proxy: CommonProxy
  }

  @Mod.EventHandler
  fun preInit(event: FMLPreInitializationEvent) {
    // proxy.preInit(event)
  }

  @Mod.EventHandler
  fun init(event: FMLInitializationEvent) {
    // proxy.init(event)
  }

  @Mod.EventHandler
  fun postInit(event: FMLPostInitializationEvent) {
    // proxy.postInit(event)
  }

  @Mod.EventHandler
  fun completeInit(event: FMLLoadCompleteEvent) {
    // proxy.completeInit(event)
  }

  @Mod.EventHandler
  fun serverStarting(event: FMLServerStartingEvent) {
    // proxy.serverStarting(event)
  }
}

val Any.Log: Logger
  get() = Nova.LOG
