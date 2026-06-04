package com.goobercorp.gooberlib.mixin.gui;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

	@WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runTick(Z)V"))
	void yeah(Minecraft instance, boolean bl, Operation<Void> original) {
		try {
			original.call(instance, bl);
		} catch (Throwable e) { // todo: catching Throwable bad bad bad
			if (Minecraft.getInstance().screen instanceof GooberScreen g && FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment()) {
				GooberLibEntrypoint.LOGGER.warn("Error while in GooberScreen! Screen closed to avoid relaunch!", e);
				g.onClose();
			} else {
				rethrow(e);
			}
		}
	}
}
