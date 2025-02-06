package vis.rhynia.nova.mixins.pp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import gregtech.api.logic.ProcessingLogic;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEMassFabricator;

@Mixin(value = MTEMassFabricator.class, priority = 2000, remap = false)
public class MixinMassFabricator {

    @ModifyReturnValue(method = "createProcessingLogic", at = @At("RETURN"))
    private ProcessingLogic injectProcessingLogic(ProcessingLogic o) {
        return o.setEuModifier(1.0F / 10.0F)
            .setSpeedBonus(1.0F / 4.0F);
    }

    @ModifyReturnValue(method = "getMaxParallelRecipes", at = @At("RETURN"))
    private int parallel(int o) {
        return o * 8;
    }
}
