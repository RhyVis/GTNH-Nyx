package vis.rhynia.nova.mixins.pp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import gregtech.api.logic.ProcessingLogic;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEElementalDuplicator;

@Mixin(value = MTEElementalDuplicator.class, priority = 2000, remap = false)
public class MixinElementalDuplicator {

    @ModifyReturnValue(method = "createProcessingLogic", at = @At("RETURN"))
    private ProcessingLogic injectEUModifier(ProcessingLogic o) {
        return o.setEuModifier(0.50F)
            .setSpeedBonus(1F / 3F);
    }

    @ModifyReturnValue(method = "getMaxParallelRecipes", at = @At("RETURN"))
    private int getMaxParallelRecipes(int o) {
        return o * 4;
    }
}
