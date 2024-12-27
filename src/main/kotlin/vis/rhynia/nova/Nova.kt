package vis.rhynia.nova

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
    modid = Nova.MODID,
    version = Tags.VERSION,
    name = "Nova",
    acceptedMinecraftVersions = "[1.7.10]")
class Nova {
  companion object {
    const val MODID = "nova"
    val LOG: Logger = LogManager.getLogger(MODID)

    @SidedProxy(
        clientSide = "vis.rhynia.nova.ClientProxy", serverSide = "vis.rhynia.nova.CommonProxy")
    lateinit var proxy: CommonProxy
  }

  @Mod.EventHandler
  fun preInit(event: FMLPreInitializationEvent) {
    proxy.preInit(event)
  }

  @Mod.EventHandler
  fun init(event: FMLInitializationEvent) {
    proxy.init(event)
  }

  @Mod.EventHandler
  fun postInit(event: FMLPostInitializationEvent) {
    proxy.postInit(event)
  }

  @Mod.EventHandler
  fun serverStarting(event: FMLServerStartingEvent) {
    proxy.serverStarting(event)
  }
}
