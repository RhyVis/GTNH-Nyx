package rhynia.nyx.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import static rhynia.nyx.MainKt.MOD_ID;
import static rhynia.nyx.MainKt.MOD_NAME;
import static rhynia.nyx.config.Definition.CATEGORY_RECIPE;

@Config(modid = MOD_ID, configSubDirectory = MOD_NAME, category = CATEGORY_RECIPE, filename = CATEGORY_RECIPE)
@Config.RequiresMcRestart
public class ConfigRecipe {
    @Config.Comment("Enable easy wireless recipes")
    @Config.DefaultBoolean(true)
    public static boolean RECIPE_EASY_WIRELESS;
}
