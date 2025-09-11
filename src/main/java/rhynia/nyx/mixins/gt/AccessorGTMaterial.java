package rhynia.nyx.mixins.gt;

import gregtech.api.enums.Materials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = Materials.class, remap = false)
public interface AccessorGTMaterial {
    @Accessor("MATERIALS_MAP")
    static Map<String, Materials> getMaterialMap() {
        throw new AssertionError();
    }
}
