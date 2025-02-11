package rhynia.nyx.mixins.bw;

import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import bwcrossmod.galacticgreg.MTEVoidMinerBase;

@Mixin(value = MTEVoidMinerBase.class, priority = 2000, remap = false)
public class MixinVoidMiner {

    @Shadow
    private int multiplier;

    /**
     * Multiply output of VM
     */
    @Inject(method = "getNobleGasInputAndSetMultiplier", at = @At(value = "RETURN", ordinal = 0))
    private void injectMultiplier(CallbackInfoReturnable<FluidStack> cir) {
        this.multiplier = this.multiplier * 16384;
    }
}
