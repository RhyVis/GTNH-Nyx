package rhynia.constellation.proxy

import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import rhynia.constellation.CEL_MOD_NAME
import rhynia.constellation.Config
import rhynia.constellation.Constellation
import rhynia.constellation.Log
import rhynia.constellation.Tags
import rhynia.constellation.init.MachineLoader
import rhynia.constellation.init.MaterialLoader
import rhynia.constellation.init.RecipeLoader
import rhynia.constellation.init.WirelessExtraLoader

open class CommonProxy {
  // Read config, create blocks, items, etc., and register them with the GameRegistry.
  open fun preInit(event: FMLPreInitializationEvent) {
    Log.info(
        "Hello Minecraft! $CEL_MOD_NAME initializing at version ${Tags.VERSION}" +
            if (Constellation.Companion.DevEnv) " (dev)" else null)
    Config.syncConfig(event.suggestedConfigurationFile)

    Log.info("Initializing $CEL_MOD_NAME materials...")
    MaterialLoader.load()
  }

  // Do mod setup. Build data structures. Register recipes.
  open fun init(event: FMLInitializationEvent) {
    Log.info("Initializing $CEL_MOD_NAME machines...")
    MachineLoader.load()
    WirelessExtraLoader.load()
  }

  // Handle interaction with other mods, complete setup.
  open fun postInit(event: FMLPostInitializationEvent) {}

  // Additional loader for complete init
  open fun completeInit(event: FMLLoadCompleteEvent) {
    Log.info("Initializing $CEL_MOD_NAME recipes...")
    RecipeLoader.load()
  }

  // register server commands
  open fun serverStarting(event: FMLServerStartingEvent) {}
}
