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

public class DoubleRangeOption extends BaseOption<DoubleRangeOption> implements NumberRangeOption<DoubleRangeOption> {
	private final double defaultValueMin;
	private final double defaultValueMax;
	private final double min;
	private final double max;
	public double minValue;
	public double maxValue;

	public DoubleRangeOption(CharSequence name, Function<DoubleRangeOption, CharSequence> description, double defaultValueMin, double defaultValueMax, double min, double max, WidgetProvider<DoubleRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public DoubleRangeOption(CharSequence name, CharSequence description, double defaultValueMin, double defaultValueMax, double min, double max, WidgetProvider<DoubleRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, provider);
	}

	public DoubleRangeOption(CharSequence name, CharSequence description, double defaultValueMin, double defaultValueMax, double min, double max) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, null);
	}

	public DoubleRangeOption(CharSequence name, CharSequence description, double defaultValueMin, double defaultValueMax, WidgetProvider<DoubleRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, provider);
	}

	public DoubleRangeOption(CharSequence name, CharSequence description, double defaultValueMin, double defaultValueMax) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, null);
	}

	public DoubleRangeOption(CharSequence name, CharSequence description, WidgetProvider<DoubleRangeOption> provider) {
		this(name, _ -> description, GooberLibApi.Defaults.doubleRangeDefaultMinValue, GooberLibApi.Defaults.doubleRangeDefaultMaxValue, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, provider);
	}

	public DoubleRangeOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, GooberLibApi.Defaults.doubleRangeDefaultMinValue, GooberLibApi.Defaults.doubleRangeDefaultMaxValue, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, null);
	}

	public DoubleRangeOption(CharSequence name, double defaultValueMin, double defaultValueMax, double min, double max, WidgetProvider<DoubleRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, provider);
	}

	public DoubleRangeOption(CharSequence name, double defaultValueMin, double defaultValueMax, double min, double max) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, null);
	}

	public DoubleRangeOption(CharSequence name, double defaultValueMin, double defaultValueMax, WidgetProvider<DoubleRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, provider);
	}

	public DoubleRangeOption(CharSequence name, double defaultValueMin, double defaultValueMax) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, null);
	}

	public DoubleRangeOption(CharSequence name, WidgetProvider<DoubleRangeOption> provider) {
		this(name, _ -> "", GooberLibApi.Defaults.doubleRangeDefaultMinValue, GooberLibApi.Defaults.doubleRangeDefaultMaxValue, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, provider);
	}

	public DoubleRangeOption(CharSequence name) {
		this(name, _ -> "", GooberLibApi.Defaults.doubleRangeDefaultMinValue, GooberLibApi.Defaults.doubleRangeDefaultMaxValue, GooberLibApi.Defaults.doubleRangeDefaultMin, GooberLibApi.Defaults.doubleRangeDefaultMin, null);
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMinValue(double value) {
		this.minValue = value;
	}

	public void setMaxValue(double value) {
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
			this.setMinValue(Double.parseDouble(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Double.parseDouble(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.DOUBLE;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(Map.of(ops.createString("min"), ops.createDouble(this.minValue), ops.createString("max"), ops.createDouble(this.maxValue)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Map<S, S> map = ops.getMapValues(object).getOrThrow().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		this.minValue = ops.getNumberValue(map.get(ops.createString("min"))).getOrThrow().doubleValue();
		this.maxValue = ops.getNumberValue(map.get(ops.createString("max"))).getOrThrow().doubleValue();
	}
}
