package com.goobercorp.gooberlib.builder.v3.individual.primitive;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import static java.lang.Math.clamp;

public class DoubleOption extends BaseOption<DoubleOption> {
	private final double defaultValue;
	private final double min;
	private final double max;
	public double value;

	public DoubleOption(Text name, Text description, double defaultValue, double min, double max, WidgetProvider provider) {
		super(name, description, provider == null ? GooberLibApi.getDefaultWidgetProvider(DoubleOption.class) : provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public DoubleOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), 0, Double.MIN_VALUE, Double.MAX_VALUE, null);
	}

	public DoubleOption(String name, String description, WidgetProvider provider) {
		this(Text.literal(name), Text.literal(description), 0, Double.MIN_VALUE, Double.MAX_VALUE, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createDouble(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.doubleValue();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double newValue) {
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

	public double getDefaultValue() {
		return defaultValue;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}
}
