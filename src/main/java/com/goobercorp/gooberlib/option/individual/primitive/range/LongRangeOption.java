package com.goobercorp.gooberlib.option.individual.primitive.range;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LongRangeOption extends BaseOption<LongRangeOption> implements NumberRangeOption<LongRangeOption> {
	private final long defaultValueMin;
	private final long defaultValueMax;
	private final long min;
	private final long max;
	public long minValue;
	public long maxValue;

	public LongRangeOption(CharSequence name, Function<LongRangeOption, CharSequence> description, long defaultValueMin, long defaultValueMax, long min, long max, WidgetProvider<LongRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public LongRangeOption(CharSequence name, CharSequence description, long defaultValueMin, long defaultValueMax, long min, long max, WidgetProvider<LongRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, provider);
	}

	public LongRangeOption(CharSequence name, CharSequence description, long defaultValueMin, long defaultValueMax, long min, long max) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, null);
	}

	public LongRangeOption(CharSequence name, CharSequence description, long defaultValueMin, long defaultValueMax, WidgetProvider<LongRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, provider);
	}

	public LongRangeOption(CharSequence name, CharSequence description, long defaultValueMin, long defaultValueMax) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, null);
	}

	public LongRangeOption(CharSequence name, CharSequence description, WidgetProvider<LongRangeOption> provider) {
		this(name, _ -> description, GooberLibApi.Defaults.longRangeDefaultMinValue, GooberLibApi.Defaults.longRangeDefaultMaxValue, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, provider);
	}

	public LongRangeOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, GooberLibApi.Defaults.longRangeDefaultMinValue, GooberLibApi.Defaults.longRangeDefaultMaxValue, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, null);
	}

	public LongRangeOption(CharSequence name, long defaultValueMin, long defaultValueMax, long min, long max, WidgetProvider<LongRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, provider);
	}

	public LongRangeOption(CharSequence name, long defaultValueMin, long defaultValueMax, long min, long max) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, null);
	}

	public LongRangeOption(CharSequence name, long defaultValueMin, long defaultValueMax, WidgetProvider<LongRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, provider);
	}

	public LongRangeOption(CharSequence name, long defaultValueMin, long defaultValueMax) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, null);
	}


	public LongRangeOption(CharSequence name, WidgetProvider<LongRangeOption> provider) {
		this(name, _ -> "", GooberLibApi.Defaults.longRangeDefaultMinValue, GooberLibApi.Defaults.longRangeDefaultMaxValue, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, provider);
	}

	public LongRangeOption(CharSequence name) {
		this(name, _ -> "", GooberLibApi.Defaults.longRangeDefaultMinValue, GooberLibApi.Defaults.longRangeDefaultMaxValue, GooberLibApi.Defaults.longRangeDefaultMin, GooberLibApi.Defaults.longRangeDefaultMin, null);
	}

	public long getMinValue() {
		return minValue;
	}

	public long getMaxValue() {
		return maxValue;
	}

	public void setMinValue(long value) {
		this.minValue = value;
	}

	public void setMaxValue(long value) {
		this.maxValue = value;
	}

	public void resetToDefault() {
		setMinValue(defaultValueMin);
		setMaxValue(defaultValueMax);
	}

	@Override
	public double getDoubleMin() {
		return min;
	}

	@Override
	public double getDoubleMax() {
		return max;
	}

	@Override
	public Number getNumberMinValue() {
		return this.minValue;
	}

	@Override
	public Number getNumberMaxValue() {
		return this.maxValue;
	}

	@Override
	public void setMinFromString(String s) {
		try {
			this.setMinValue(Long.parseLong(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Long.parseLong(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.LONG;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = (long) v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = (long) v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(Map.of(ops.createString("min"), ops.createLong(this.minValue), ops.createString("max"), ops.createLong(this.maxValue)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Map<S, S> map = ops.getMapValues(object).getOrThrow().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		this.minValue = ops.getNumberValue(map.get(ops.createString("min"))).getOrThrow().longValue();
		this.maxValue = ops.getNumberValue(map.get(ops.createString("max"))).getOrThrow().longValue();
	}
}
