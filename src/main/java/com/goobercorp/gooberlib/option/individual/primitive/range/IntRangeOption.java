package com.goobercorp.gooberlib.option.individual.primitive.range;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IntRangeOption extends BaseOption<IntRangeOption> implements NumberRangeOption<IntRangeOption> {
	private final int defaultValueMin;
	private final int defaultValueMax;
	private final int min;
	private final int max;
	public int minValue;
	public int maxValue;

	public IntRangeOption(Text name, Function<IntRangeOption, Text> description, int defaultValueMin, int defaultValueMax, int min, int max, WidgetProvider<IntRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMinValue(int value) {
		this.minValue = value;
	}

	public void setMaxValue(int value) {
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
			this.setMinValue(Integer.parseInt(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Integer.parseInt(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.INTEGER;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = (int) v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = (int) v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(Map.of(ops.createString("min"), ops.createInt(this.minValue), ops.createString("max"), ops.createInt(this.maxValue)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Map<S, S> map = ops.getMapValues(object).getOrThrow().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		this.minValue = ops.getNumberValue(map.get(ops.createString("min"))).getOrThrow().intValue();
		this.maxValue = ops.getNumberValue(map.get(ops.createString("max"))).getOrThrow().intValue();
	}
}
