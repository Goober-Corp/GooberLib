package com.goobercorp.gooberlib.mixin.hotkey;

import com.goobercorp.gooberlib.util.HotkeyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
	@Inject(method = "onButton", at = @At("HEAD"))
	private void onKey(long l, MouseButtonInfo mouseInput, int i, CallbackInfo ci) {
		if (Minecraft.getInstance().getWindow().handle() == l) {
			HotkeyUtil.handleMouse(i, mouseInput);
		}
	}
}
