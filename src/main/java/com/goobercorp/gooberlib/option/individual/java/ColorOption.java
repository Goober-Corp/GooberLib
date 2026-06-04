package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;
import java.awt.*;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;

public class ColorOption extends BaseOption<ColorOption> {
	/// @implNote This is in ARGB
	private final int defaultValue;
	/// @implNote Modifying this value directly will *not* trigger .onChange(). This is in ARGB
	public int value;

	public ColorOption(Component name, Function<ColorOption, Component> description, int defaultValue, WidgetProvider<ColorOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public ColorOption(String name, String description) {
		this(Component.literal(name), _ -> Component.literal(description), 0xFFFFFFFF, null);
	}

	public ColorOption(String name, String description, WidgetProvider<ColorOption> provider) {
		this(Component.literal(name), _ -> Component.literal(description), 0xFFFFFFFF, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createInt(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.intValue();
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int newValue) {
		if (this.value != newValue) {
			this.value = newValue;
			this.onChange();
		}
	}

	public void resetToDefault() {
		if (this.value != this.defaultValue) {
			setValue(this.defaultValue);
		}
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public int getRGB() {
		return this.value & 0xFFFFFF;
	}

	public int getAlpha() {
		return this.value >>> 24;
	}

	public Color asColor() {
		return new Color(this.value, true);
	}

	public Predicate<String> getPredicate() {
		return s -> {
			try {
				theThing.apply(s);
				return true;
			} catch (IllegalStateException _) {
				return false;
			}
		};
	}

	public void setFromString(String s) {
		try {
			setValue(theThing.apply(s));
		} catch (IllegalStateException _) {
		}
	}

	public static final Function<String, Integer> theThing = s -> {
		if (s.isEmpty()) throw new IllegalStateException("Not a valid input");
		if (s.startsWith("0x")) s = s.replaceFirst("0x", "");
		if (s.startsWith("#")) s = s.replaceFirst("#", "");
		if (s.length() == 3) { // R G B -> RR GG BB
			String r = s.substring(0, 1);
			String g = s.substring(1, 2);
			String b = s.substring(2, 3);
			s = r + r + g + g + b + b;
		}
		if (s.length() == 4) { // A R G B -> AA RR GG BB
			String a = s.substring(0, 1);
			String r = s.substring(1, 2);
			String g = s.substring(2, 3);
			String b = s.substring(3, 4);
			s = a + a + r + r + g + g + b + b;
		}
		if (s.length() == 5) { // X XX XX -> 0X XX XX
			s = "0" + s;
		}
		if (s.length() == 6) { // XX XX XX -> FF XX XX XX
			s = "FF" + s;
		}
		if (s.length() == 7) { // X XX XX XX -> 0X XX XX XX
			s = "0" + s;
		}
		if (s.length() == 8) {
			try {
				int a = Integer.parseInt(s.substring(0, 2), 16);
				int r = Integer.parseInt(s.substring(2, 4), 16);
				int g = Integer.parseInt(s.substring(4, 6), 16);
				int b = Integer.parseInt(s.substring(6, 8), 16);

				return (a << 24 | r << 16 | g << 8 | b);
			} catch (NumberFormatException _) {
				throw new IllegalStateException("Not a valid input");
			}
		} else {
			throw new IllegalStateException("Not a valid input");
		}
	};

	public Predicate<String> getImmediatePredicate() {
		return s -> {
			if (s.startsWith("#")) s = s.substring(1);
			else if (s.startsWith("0x")) s = s.substring(2);
			return (Predicates.HEX_IMMEDIATE.test(s) && s.length() <= 8) || s.length() == 9 && s.startsWith("0");
		};
	}
}
