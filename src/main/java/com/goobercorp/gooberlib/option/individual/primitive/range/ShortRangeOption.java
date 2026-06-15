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

public class ShortRangeOption extends BaseOption<ShortRangeOption> implements NumberRangeOption<ShortRangeOption> {
	private final short defaultValueMin;
	private final short defaultValueMax;
	private final short min;
	private final short max;
	public short minValue;
	public short maxValue;

	public ShortRangeOption(CharSequence name, Function<ShortRangeOption, CharSequence> description, short defaultValueMin, short defaultValueMax, short min, short max, WidgetProvider<ShortRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public ShortRangeOption(CharSequence name, CharSequence description, short defaultValueMin, short defaultValueMax, short min, short max, WidgetProvider<ShortRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, provider);
	}

	public ShortRangeOption(CharSequence name, CharSequence description, short defaultValueMin, short defaultValueMax, short min, short max) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, min, max, null);
	}

	public ShortRangeOption(CharSequence name, CharSequence description, short defaultValueMin, short defaultValueMax, WidgetProvider<ShortRangeOption> provider) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, Short.MIN_VALUE, Short.MAX_VALUE, provider);
	}

	public ShortRangeOption(CharSequence name, CharSequence description, short defaultValueMin, short defaultValueMax) {
		this(name, _ -> description, defaultValueMin, defaultValueMax, Short.MIN_VALUE, Short.MAX_VALUE, null);
	}

	public ShortRangeOption(CharSequence name, CharSequence description, WidgetProvider<ShortRangeOption> provider) {
		this(name, _ -> description, (short) -1, (short) 1, Short.MIN_VALUE, Short.MAX_VALUE, provider);
	}

	public ShortRangeOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, (short) -1, (short) 1, Short.MIN_VALUE, Short.MAX_VALUE, null);
	}

	public ShortRangeOption(CharSequence name, short defaultValueMin, short defaultValueMax, short min, short max, WidgetProvider<ShortRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, provider);
	}

	public ShortRangeOption(CharSequence name, short defaultValueMin, short defaultValueMax, short min, short max) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, min, max, null);
	}

	public ShortRangeOption(CharSequence name, short defaultValueMin, short defaultValueMax, WidgetProvider<ShortRangeOption> provider) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, Short.MIN_VALUE, Short.MAX_VALUE, provider);
	}

	public ShortRangeOption(CharSequence name, short defaultValueMin, short defaultValueMax) {
		this(name, _ -> "", defaultValueMin, defaultValueMax, Short.MIN_VALUE, Short.MAX_VALUE, null);
	}

	public ShortRangeOption(CharSequence name, WidgetProvider<ShortRangeOption> provider) {
		this(name, _ -> "", (short) -1, (short) 1, Short.MIN_VALUE, Short.MAX_VALUE, provider);
	}

	public ShortRangeOption(CharSequence name) {
		this(name, _ -> "", (short) -1, (short) 1, Short.MIN_VALUE, Short.MAX_VALUE, null);
	}

	public short getMinValue() {
		return minValue;
	}

	public short getMaxValue() {
		return maxValue;
	}

	public void setMinValue(short value) {
		this.minValue = value;
	}

	public void setMaxValue(short value) {
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
			this.setMinValue(Short.parseShort(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Short.parseShort(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.SHORT;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = (short) v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = (short) v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(Map.of(ops.createString("min"), ops.createShort(this.minValue), ops.createString("max"), ops.createShort(this.maxValue)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Map<S, S> map = ops.getMapValues(object).getOrThrow().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		this.minValue = ops.getNumberValue(map.get(ops.createString("min"))).getOrThrow().shortValue();
		this.maxValue = ops.getNumberValue(map.get(ops.createString("max"))).getOrThrow().shortValue();
	}
}
