package com.goobercorp.gooberlib.util;

import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class Util {
	public static Component fromChars(CharSequence charSequence) {
		if (charSequence instanceof Component c) return c;
		if (charSequence == null) return Component.empty();
		if (charSequence.isEmpty()) return Component.empty();
		return Component.literal(charSequence.toString());
	}

	public static <T> Function<T, Component> fromCharsFunction(Function<T, CharSequence> function) {
		return t -> fromChars(function.apply(t));
	}
}
