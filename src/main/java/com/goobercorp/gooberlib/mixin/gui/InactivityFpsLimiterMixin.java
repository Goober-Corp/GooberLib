package com.goobercorp.gooberlib.mixin.gui;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InactivityFpsLimiter.class)
public abstract class InactivityFpsLimiterMixin {

    @Shadow
    public abstract InactivityFpsLimiter.LimitReason getLimitReason();

    @Shadow
    private int maxFps;

    @ModifyReturnValue(method = "update", at = @At("RETURN"))
    //this is how we make everyone think that it's smoother than it actually is. shh, don't tell anyone!
    int yeah(int original){
        if(getLimitReason() == InactivityFpsLimiter.LimitReason.OUT_OF_LEVEL_MENU){
            return maxFps;
        }
        return original;
    }
}
