package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.clamp;

public class LongOption extends BaseOption<LongOption> implements NumberOption<LongOption> {
	private final long defaultValue;
	private final long min;
	private final long max;
	public long value;

	public LongOption(CharSequence name, Function<LongOption, CharSequence> description, long defaultValue, long min, long max, WidgetProvider<LongOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public LongOption(CharSequence name, CharSequence description, long defaultValue, long min, long max, WidgetProvider<LongOption> provider) {
		this(name, _ -> description, defaultValue, min, max, provider);
	}

	public LongOption(CharSequence name, CharSequence description, long defaultValue, long min, long max) {
		this(name, _ -> description, defaultValue, min, max, null);
	}

	public LongOption(CharSequence name, long defaultValue, long min, long max, WidgetProvider<LongOption> provider) {
		this(name, _ -> "", defaultValue, min, max, provider);
	}

	public LongOption(CharSequence name, long defaultValue, long min, long max) {
		this(name, _ -> "", defaultValue, min, max, null);
	}

	public LongOption(CharSequence name, CharSequence description, long defaultValue, WidgetProvider<LongOption> provider) {
		this(name, _ -> description, defaultValue, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, provider);
	}

	public LongOption(CharSequence name, CharSequence description, long defaultValue) {
		this(name, _ -> description, defaultValue, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, null);
	}

	public LongOption(CharSequence name, long defaultValue, WidgetProvider<LongOption> provider) {
		this(name, _ -> "", defaultValue, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, provider);
	}

	public LongOption(CharSequence name, long defaultValue) {
		this(name, _ -> "", defaultValue, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, null);
	}

	public LongOption(CharSequence name, CharSequence description, WidgetProvider<LongOption> provider) {
		this(name, _ -> description, GooberLibApi.Defaults.longDefault, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, provider);
	}

	public LongOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, GooberLibApi.Defaults.longDefault, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, null);
	}

	public LongOption(CharSequence name, WidgetProvider<LongOption> provider) {
		this(name, _ -> "", GooberLibApi.Defaults.longDefault, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, provider);
	}

	public LongOption(CharSequence name) {
		this(name, _ -> "", GooberLibApi.Defaults.longDefault, GooberLibApi.Defaults.longDefaultMin, GooberLibApi.Defaults.longDefaultMax, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createLong(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.longValue();
	}

	public long getValue() {
		return value;
	}

	public void setValue(long newValue) {
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

	public long getDefaultValue() {
		return defaultValue;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	@Override
	public Number getNumberValue() {
		return this.value;
	}

	@Override
	public void setDoubleValue(double n) {
		this.value = (long) n;
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
			this.setValue(Long.parseLong(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.LONG;
	}

	@Override
	public Predicate<String> getImmediatePredicate() {
		return Predicates.INTEGER_IMMEDIATE;
	}
}
