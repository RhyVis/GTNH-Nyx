package vis.rhynia.nova.proxy

import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import vis.rhynia.nova.Config
import vis.rhynia.nova.Constant
import vis.rhynia.nova.Log
import vis.rhynia.nova.Tags
import vis.rhynia.nova.common.loader.MachineLoader
import vis.rhynia.nova.common.loader.MaterialLoader
import vis.rhynia.nova.common.loader.RecipeLoader
import vis.rhynia.nova.common.loader.WirelessExtraLoader

open class CommonProxy {
  // Read config, create blocks, items, etc., and register them with the GameRegistry.
  open fun preInit(event: FMLPreInitializationEvent) {
    Log.info("Hello Minecraft! ${Constant.MOD_NAME} initializing at version ${Tags.VERSION}")
    Config.syncConfig(event.suggestedConfigurationFile)

    Log.info("Initializing ${Constant.MOD_NAME} materials...")
    MaterialLoader.load()
  }

  // Do mod setup. Build data structures. Register recipes.
  open fun init(event: FMLInitializationEvent) {
    Log.info("Initializing ${Constant.MOD_NAME} machines...")
    MachineLoader.load()
    WirelessExtraLoader.load()
  }

  // Handle interaction with other mods, complete setup.
  open fun postInit(event: FMLPostInitializationEvent) {}

  // Additional loader for complete init
  open fun completeInit(event: FMLLoadCompleteEvent) {
    Log.info("Initializing ${Constant.MOD_NAME} recipes...")
    RecipeLoader.loadAtCompleteInit()
  }

  // register server commands
  open fun serverStarting(event: FMLServerStartingEvent) {}
}
