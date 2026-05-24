package com.goobercorp.gooberlib.mixin.gui;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;render(Z)V"))
	void yeah(MinecraftClient instance, boolean bl, Operation<Void> original) {
		try {
			original.call(instance, bl);
		} catch (Throwable e) {
			if (MinecraftClient.getInstance().currentScreen instanceof GooberScreen g && FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment()) {
				GooberLibEntrypoint.LOGGER.warn("Error while in GooberScreen! Screen closed to avoid relaunch!", e);
				g.close();
			} else {
				rethrow(e);
			}
		}
	}
}
