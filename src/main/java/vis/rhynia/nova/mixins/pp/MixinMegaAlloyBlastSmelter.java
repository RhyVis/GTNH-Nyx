package vis.rhynia.nova.mixins.pp;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GTRecipe;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega.MTEMegaAlloyBlastSmelter;

@Mixin(value = MTEMegaAlloyBlastSmelter.class, priority = 2000, remap = false)
public class MixinMegaAlloyBlastSmelter {

    @Shadow
    private double energyDiscount;

    @Inject(method = "calculateEnergyDiscount", at = @At("HEAD"), cancellable = true)
    private void calculateEnergyDiscount(HeatingCoilLevel lvl, GTRecipe recipe, CallbackInfo ci) {
        energyDiscount = 1.0 - Math.min(Math.pow(0.8, lvl.getTier()), 0.3);
        ci.cancel();
    }

    @ModifyConstant(method = "createProcessingLogic", constant = @Constant(intValue = 256))
    private int parallelModify(int p) {
        return 1024;
    }
}
