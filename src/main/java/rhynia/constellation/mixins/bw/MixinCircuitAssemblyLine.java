package rhynia.constellation.mixins.bw;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import bartworks.common.tileentities.multis.MTECircuitAssemblyLine;
import gregtech.api.logic.ProcessingLogic;

@Mixin(value = MTECircuitAssemblyLine.class, priority = 2000, remap = false)
public class MixinCircuitAssemblyLine {

    @ModifyReturnValue(method = "createProcessingLogic", at = @At("RETURN"))
    private ProcessingLogic modifySpeedModifier(ProcessingLogic o) {
        return o.setSpeedBonus(1F / 3F);
    }
}
