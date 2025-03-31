package rhynia.nyx.init.mixin

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader
import com.gtnewhorizon.gtnhmixins.LateMixin

@LateMixin
class MixinManager : ILateMixinLoader {
    override fun getMixinConfig(): String = "mixins.nyx.late.json"

    override fun getMixins(loadedMods: Set<String>): List<String> = MixinEntry.findLateMixins(loadedMods)
}
