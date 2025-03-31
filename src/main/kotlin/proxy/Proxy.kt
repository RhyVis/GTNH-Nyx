package rhynia.nyx.proxy

import com.gtnewhorizon.structurelib.StructureLib
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import rhynia.nyx.Config
import rhynia.nyx.DevEnv
import rhynia.nyx.MOD_NAME
import rhynia.nyx.ModLogger
import rhynia.nyx.Tags
import rhynia.nyx.init.MachineLoader
import rhynia.nyx.init.MaterialLoader
import rhynia.nyx.init.RecipeLoader

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        ModLogger.info(
            "Hello Minecraft! $MOD_NAME initializing at version ${Tags.VERSION}" +
                if (DevEnv) " (dev)" else null,
        )
        Config.syncConfig(event.suggestedConfigurationFile)

        ModLogger.info("Initializing $MOD_NAME materials...")
        MaterialLoader.load()
    }

    open fun init(event: FMLInitializationEvent) {
        ModLogger.info("Initializing $MOD_NAME machines...")
        MachineLoader.load()
    }

    open fun postInit(event: FMLPostInitializationEvent) {}

    open fun completeInit(event: FMLLoadCompleteEvent) {
        ModLogger.info("Initializing $MOD_NAME recipes...")
        RecipeLoader.load()
    }

    open fun serverStarting(event: FMLServerStartingEvent) {
        if (DevEnv) {
            StructureLib.DEBUG_MODE = true
        }
    }
}

@Suppress("UNUSED")
class ClientProxy : CommonProxy()
