package vis.rhynia.nova.mixins.pp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import gregtech.api.logic.ProcessingLogic;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialDehydrator;

@Mixin(value = MTEIndustrialDehydrator.class, priority = 2000, remap = false)
public class MixinIndustrialDehydrator {

    @ModifyConstant(method = "getMaxParallelRecipes", constant = @Constant(intValue = 4))
    private int parallel(int i) {
        return 16384;
    }

    @ModifyReturnValue(method = "createProcessingLogic", at = @At("RETURN"))
    private ProcessingLogic eu(ProcessingLogic o) {
        return o.setSpeedBonus(1F / 16F)
            .setEuModifier(1F / 8F);
    }
}
