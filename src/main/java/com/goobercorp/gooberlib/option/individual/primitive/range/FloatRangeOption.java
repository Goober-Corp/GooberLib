package com.goobercorp.gooberlib.option.individual.primitive.range;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FloatRangeOption extends BaseOption<FloatRangeOption> implements NumberRangeOption<FloatRangeOption> {
	private final float defaultValueMin;
	private final float defaultValueMax;
	private final float min;
	private final float max;
	public float minValue;
	public float maxValue;

	public FloatRangeOption(CharSequence name, Function<FloatRangeOption, CharSequence> description, float defaultValueMin, float defaultValueMax, float min, float max, WidgetProvider<FloatRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public FloatRangeOption(CharSequence name, CharSequence description, float defaultValueMin, float defaultValueMax, float min, float max, WidgetProvider<FloatRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, provider);
	}

	public FloatRangeOption(CharSequence name, CharSequence description, float defaultValueMin, float defaultValueMax, float min, float max) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, null);
	}

	public FloatRangeOption(CharSequence name, CharSequence description, float defaultValueMin, float defaultValueMax, WidgetProvider<FloatRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, -Float.MAX_VALUE, Float.MAX_VALUE, provider);
	}

	public FloatRangeOption(CharSequence name, CharSequence description, float defaultValueMin, float defaultValueMax) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, -Float.MAX_VALUE, Float.MAX_VALUE, null);
	}

	public FloatRangeOption(CharSequence name, float defaultValueMin, float defaultValueMax, float min, float max, WidgetProvider<FloatRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, provider);
	}

	public FloatRangeOption(CharSequence name, float defaultValueMin, float defaultValueMax, float min, float max) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, null);
	}

	public FloatRangeOption(CharSequence name, float defaultValueMin, float defaultValueMax, WidgetProvider<FloatRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, -Float.MAX_VALUE, Float.MAX_VALUE, provider);
	}

	public FloatRangeOption(CharSequence name, float defaultValueMin, float defaultValueMax) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, -Float.MAX_VALUE, Float.MAX_VALUE, null);
	}

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMinValue(float value) {
		this.minValue = value;
	}

	public void setMaxValue(float value) {
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
			this.setMinValue(Float.parseFloat(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Float.parseFloat(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.FLOAT;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = (float) v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = (float) v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(Map.of(ops.createString("min"), ops.createFloat(this.minValue), ops.createString("max"), ops.createFloat(this.maxValue)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Map<S, S> map = ops.getMapValues(object).getOrThrow().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		this.minValue = ops.getNumberValue(map.get(ops.createString("min"))).getOrThrow().floatValue();
		this.maxValue = ops.getNumberValue(map.get(ops.createString("max"))).getOrThrow().floatValue();
	}
}
