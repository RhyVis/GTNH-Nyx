package rhynia.constellation.mixins.ae;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import appeng.items.tools.powered.ToolPortableCell;

@Mixin(value = ToolPortableCell.class, priority = 2000, remap = false)
public class MixinPortableCell {

    @ModifyReturnValue(method = "getBytes", at = @At("RETURN"))
    private int modifyBytes(int o) {
        return 32768;
    }
}
