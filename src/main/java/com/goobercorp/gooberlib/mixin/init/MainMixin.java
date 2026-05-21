package com.goobercorp.gooberlib.mixin.init;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At("HEAD"))
	private static void inject(String[] strings, CallbackInfo ci) {
		GooberLibEntrypoint.init();
	}
}
