package rhynia.nyx.mixins.pp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import gregtech.api.logic.ProcessingLogic;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialFluidHeater;

@Mixin(value = MTEIndustrialFluidHeater.class, priority = 2000, remap = false)
public class MixinIndustrialFluidHeater {

    @ModifyReturnValue(method = "createProcessingLogic", at = @At("RETURN"))
    private ProcessingLogic speedModify(ProcessingLogic o) {
        return o.setSpeedBonus(1F / 8F)
            .setEuModifier(0.35F);
    }

    @ModifyReturnValue(method = "getMaxParallelRecipes", at = @At("RETURN"))
    private int getMaxParallelRecipes(int o) {
        return o * 6;
    }
}
