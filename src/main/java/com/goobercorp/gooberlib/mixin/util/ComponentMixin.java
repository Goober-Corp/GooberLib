package com.goobercorp.gooberlib.mixin.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Component.class)
public interface ComponentMixin extends CharSequence {
	@Shadow
	String getString();

	@Override
	default int length() {
		return getString().length();
	}

	@Override
	default char charAt(int index) {
		return getString().charAt(index);
	}

	@Override
	@NotNull
	default CharSequence subSequence(int start, int end) {
		return getString().subSequence(start, end);
	}
}
