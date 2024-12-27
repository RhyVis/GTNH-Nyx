package vis.rhynia.nova

import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent

open class CommonProxy {
  // Read config, create blocks, items, etc., and register them with the GameRegistry.
  open fun preInit(event: FMLPreInitializationEvent) {
    Config.synchronizeConfiguration(event.suggestedConfigurationFile)

    Nova.LOG.info(Config.greeting)
    Nova.LOG.info("I am Nova at version " + Tags.VERSION)
  }

  // Do mod setup. Build data structures. Register recipes.
  open fun init(event: FMLInitializationEvent) {}

  // Handle interaction with other mods, complete setup.
  open fun postInit(event: FMLPostInitializationEvent) {}

  // register server commands
  open fun serverStarting(event: FMLServerStartingEvent) {}
}
