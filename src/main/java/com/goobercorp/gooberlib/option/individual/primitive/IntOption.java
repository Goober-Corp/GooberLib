package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.clamp;

public class IntOption extends BaseOption<IntOption> implements NumberOption<IntOption> {
	private final int defaultValue;
	private final int min;
	private final int max;
	public int value;

	public IntOption(Text name, Function<IntOption, Text> description, int defaultValue, int min, int max, WidgetProvider<IntOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public IntOption(String name, String description) {
		this(Text.literal(name), _ -> Text.literal(description), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
	}

	public IntOption(String name, String description, WidgetProvider<IntOption> provider) {
		this(Text.literal(name), _ -> Text.literal(description), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, provider);
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
		return value;
	}

	public void setValue(int newValue) {
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

	public int getDefaultValue() {
		return defaultValue;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	@Override
	public Number getNumberValue() {
		return this.value;
	}

	@Override
	public void setDoubleValue(double n) {
		this.value = (int) n;
	}

	@Override
	public double getDoubleMin() {
		return this.getMin();
	}

	@Override
	public double getDoubleMax() {
		return this.getMax();
	}

	@Override
	public void setFromString(String s) {
		try {
			this.setValue(Integer.parseInt(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return s -> {
			try {
				Integer.parseInt(s);
				return true;
			} catch (NumberFormatException | NullPointerException _) {
				return false;
			}
		};
	}
}
