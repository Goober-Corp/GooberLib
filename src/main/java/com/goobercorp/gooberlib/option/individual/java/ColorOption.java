package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.awt.*;

public class ColorOption extends BaseOption<ColorOption> {
	/// @implNote This is in ARGB
	private final int defaultValue;
	/// @implNote Modifying this value directly will *not* trigger .onChange(). This is in ARGB
	public int value;

	public ColorOption(Text name, Text description, int defaultValue, WidgetProvider<ColorOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public ColorOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), 0xFFFFFFFF, null);
	}

	public ColorOption(String name, String description, WidgetProvider<ColorOption> provider) {
		this(Text.literal(name), Text.literal(description), 0xFFFFFFFF, provider);
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
		return new Color((this.value << 8) | this.getAlpha(), true);
	}
}
