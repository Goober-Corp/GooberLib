package com.goobercorp.gooberlib.mixin.hotkey;

import com.goobercorp.gooberlib.util.HotkeyUtil;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At("HEAD"))
	private void onKey(long l, int i, KeyInput keyInput, CallbackInfo ci) {
		if (MinecraftClient.getInstance().getWindow().getHandle() == l) {
			HotkeyUtil.handleKeyboard(i, keyInput);
		}
	}
}
