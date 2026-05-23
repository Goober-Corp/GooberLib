package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import static java.lang.Math.clamp;

public class FloatOption extends BaseOption<FloatOption> {
	private final float defaultValue;
	private final float min;
	private final float max;
	public float value;

	public FloatOption(Text name, Text description, float defaultValue, float min, float max, WidgetProvider<FloatOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public FloatOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), 0, Float.MIN_VALUE, Float.MAX_VALUE, null);
	}

	public FloatOption(String name, String description, WidgetProvider<FloatOption> provider) {
		this(Text.literal(name), Text.literal(description), 0, Float.MIN_VALUE, Float.MAX_VALUE, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createFloat(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.floatValue();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float newValue) {
		newValue = clamp(newValue, min, max);
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

	public float getDefaultValue() {
		return defaultValue;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}
}
