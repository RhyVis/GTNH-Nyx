package rhynia.nyx.proxy

import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import rhynia.nyx.Config
import rhynia.nyx.Log
import rhynia.nyx.NYX_MOD_NAME
import rhynia.nyx.Nyx.DevEnv
import rhynia.nyx.Tags
import rhynia.nyx.common.tile.multi.process.NyxMTEProcessingComplex
import rhynia.nyx.init.MachineLoader
import rhynia.nyx.init.MaterialLoader
import rhynia.nyx.init.RecipeLoader
import rhynia.nyx.init.WirelessExtraLoader

open class CommonProxy {
  // Read config, create blocks, items, etc., and register them with the GameRegistry.
  open fun preInit(event: FMLPreInitializationEvent) {
    Log.info(
        "Hello Minecraft! $NYX_MOD_NAME initializing at version ${Tags.VERSION}" +
            if (DevEnv) " (dev)" else null)
    Config.syncConfig(event.suggestedConfigurationFile)

    Log.info("Initializing $NYX_MOD_NAME materials...")
    MaterialLoader.load()
  }

  // Do mod setup. Build data structures. Register recipes.
  open fun init(event: FMLInitializationEvent) {
    Log.info("Initializing $NYX_MOD_NAME machines...")
    MachineLoader.load()
    WirelessExtraLoader.load()
  }

  // Handle interaction with other mods, complete setup.
  open fun postInit(event: FMLPostInitializationEvent) {}

  // Additional loader for complete init
  open fun completeInit(event: FMLLoadCompleteEvent) {
    Log.info("Initializing $NYX_MOD_NAME recipes...")
    RecipeLoader.load()
    NyxMTEProcessingComplex.ProcessReference.generateMap()
  }

  // register server commands
  open fun serverStarting(event: FMLServerStartingEvent) {
    // StructureLib.DEBUG_MODE = true
  }
}
