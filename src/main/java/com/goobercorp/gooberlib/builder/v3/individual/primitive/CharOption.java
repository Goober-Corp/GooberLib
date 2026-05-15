package com.goobercorp.gooberlib.builder.v3.individual.primitive;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public class CharOption extends BaseOption<CharOption> {
	private final char defaultValue;
	private final char min;
	private final char max;
	public char value;

	public CharOption(Text name, Text description, char defaultValue, char min, char max, WidgetProvider provider) {
		super(name, description, provider == null ? GooberLibApi.getDefaultWidgetProvider(CharOption.class) : provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public CharOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), (char) 0, Character.MIN_VALUE, Character.MAX_VALUE, null);
	}

	public CharOption(String name, String description, WidgetProvider provider) {
		this(Text.literal(name), Text.literal(description), (char) 0, Character.MIN_VALUE, Character.MAX_VALUE, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createString(String.valueOf(value));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getStringValue(object)
				.getOrThrow()
				.charAt(0);
	}

	public char getValue() {
		return value;
	}

	public void setValue(char newValue) {
		newValue = newValue < min ? min : (newValue > max ? max : newValue);
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

	public char getDefaultValue() {
		return defaultValue;
	}

	public char getMin() {
		return min;
	}

	public char getMax() {
		return max;
	}
}
