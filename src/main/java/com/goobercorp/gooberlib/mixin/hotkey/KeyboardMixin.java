package com.goobercorp.gooberlib.mixin.hotkey;

import com.goobercorp.gooberlib.util.HotkeyUtil;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
	@Inject(method = "keyPress", at = @At("HEAD"))
	private void onKey(long l, int i, KeyEvent keyInput, CallbackInfo ci) {
		if (Minecraft.getInstance().getWindow().handle() == l) {
			HotkeyUtil.handleKeyboard(i, keyInput);
		}
	}
}
