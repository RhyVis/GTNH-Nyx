package rhynia.nyx.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import static rhynia.nyx.MainKt.MOD_ID;
import static rhynia.nyx.MainKt.MOD_NAME;
import static rhynia.nyx.config.Definition.CATEGORY_DEBUG;

@Config(modid = MOD_ID, configSubDirectory = MOD_NAME, category = CATEGORY_DEBUG, filename = CATEGORY_DEBUG)
@Config.RequiresMcRestart
public class ConfigDebug {
    @Config.Comment("Print all loaded mods to the console.")
    @Config.DefaultBoolean(false)
    public static boolean DEBUG_PRINT_MOD_LIST;

    @Config.Comment("Print all MTE IDs to the console.")
    @Config.DefaultBoolean(false)
    public static boolean DEBUG_PRINT_MTE_IDS;
}
