package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.clamp;

public class IntOption extends BaseOption<IntOption> implements NumberOption<IntOption> {
	private final int defaultValue;
	private final int min;
	private final int max;
	public int value;

	public IntOption(CharSequence name, Function<IntOption, CharSequence> description, int defaultValue, int min, int max, WidgetProvider<IntOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public IntOption(CharSequence name, CharSequence description, int defaultValue, int min, int max, WidgetProvider<IntOption> provider) {
		this(name, _ -> description, defaultValue, min, max, provider);
	}

	public IntOption(CharSequence name, CharSequence description, int defaultValue, int min, int max) {
		this(name, _ -> description, defaultValue, min, max, null);
	}

	public IntOption(CharSequence name, int defaultValue, int min, int max, WidgetProvider<IntOption> provider) {
		this(name, _ -> "", defaultValue, min, max, provider);
	}

	public IntOption(CharSequence name, int defaultValue, int min, int max) {
		this(name, _ -> "", defaultValue, min, max, null);
	}

	public IntOption(CharSequence name, CharSequence description, int defaultValue, WidgetProvider<IntOption> provider) {
		this(name, _ -> description, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, provider);
	}

	public IntOption(CharSequence name, CharSequence description, int defaultValue) {
		this(name, _ -> description, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
	}

	public IntOption(CharSequence name, int defaultValue, WidgetProvider<IntOption> provider) {
		this(name, _ -> "", defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, provider);
	}

	public IntOption(CharSequence name, int defaultValue) {
		this(name, _ -> "", defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
	}

	public IntOption(CharSequence name, CharSequence description, WidgetProvider<IntOption> provider) {
		this(name, _ -> description, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, provider);
	}

	public IntOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
	}

	public IntOption(CharSequence name, WidgetProvider<IntOption> provider) {
		this(name, _ -> "", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, provider);
	}

	public IntOption(CharSequence name) {
		this(name, _ -> "", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, null);
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
		return Predicates.INTEGER;
	}

	@Override
	public Predicate<String> getImmediatePredicate() {
		return Predicates.INTEGER_IMMEDIATE;
	}
}
