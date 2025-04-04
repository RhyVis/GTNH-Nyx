package rhynia.nyx.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import static rhynia.nyx.MainKt.MOD_ID;
import static rhynia.nyx.MainKt.MOD_NAME;
import static rhynia.nyx.config.Definition.CATEGORY_MIXIN;

@Config(modid = MOD_ID, configSubDirectory = MOD_NAME, category = CATEGORY_MIXIN, filename = CATEGORY_MIXIN)
@Config.RequiresMcRestart
public class ConfigMixin {
    @Config.Comment("Enable AE2 Portable Cell Mixin, increases bytes to 32768")
    @Config.DefaultBoolean(true)
    public static boolean MIXIN_AE_PORTABLE_CELL;

    @Config.Comment("Enable AE2 Quantum Cell Mixin, allowing 63 types")
    @Config.DefaultBoolean(false)
    public static boolean MIXIN_AE_QUANTUM_CELL;

    @Config.Comment("Disable BartWorks mega multi machine inner air check")
    @Config.DefaultBoolean(true)
    public static boolean MIXIN_BW_MEGA_NO_AIR_CHECK;
}
