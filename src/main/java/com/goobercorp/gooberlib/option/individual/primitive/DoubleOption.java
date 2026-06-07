package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.clamp;

public class DoubleOption extends BaseOption<DoubleOption> implements NumberOption<DoubleOption> {
	private final double defaultValue;
	private final double min;
	private final double max;
	public double value;

	public DoubleOption(CharSequence name, Function<DoubleOption, CharSequence> description, double defaultValue, double min, double max, WidgetProvider<DoubleOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public DoubleOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, 0, -Double.MAX_VALUE, Double.MAX_VALUE, null);
	}

	public DoubleOption(CharSequence name, CharSequence description, WidgetProvider<DoubleOption> provider) {
		this(name, _ -> description, 0, -Double.MAX_VALUE, Double.MAX_VALUE, provider);
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

	@Override
	public Number getNumberValue() {
		return this.value;
	}

	@Override
	public void setDoubleValue(double n) {
		this.value = n;
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
			this.setValue(Double.parseDouble(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.DOUBLE;
	}

	@Override
	public Predicate<String> getImmediatePredicate() {
		return Predicates.DOUBLE_IMMEDIATE;
	}
}
