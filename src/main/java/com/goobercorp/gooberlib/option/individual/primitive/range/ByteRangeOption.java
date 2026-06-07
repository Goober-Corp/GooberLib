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

public class ByteRangeOption extends BaseOption<ByteRangeOption> implements NumberRangeOption<ByteRangeOption> {
	private final byte defaultValueMin;
	private final byte defaultValueMax;
	private final byte min;
	private final byte max;
	public byte minValue;
	public byte maxValue;

	public ByteRangeOption(CharSequence name, Function<ByteRangeOption, CharSequence> description, byte defaultValueMin, byte defaultValueMax, byte min, byte max, WidgetProvider<ByteRangeOption> provider) {
		super(name, description, provider);
		this.minValue = defaultValueMin;
		this.maxValue = defaultValueMax;
		this.defaultValueMin = defaultValueMin;
		this.defaultValueMax = defaultValueMax;
		this.min = min;
		this.max = max;
	}

	public byte getMinValue() {
		return minValue;
	}

	public byte getMaxValue() {
		return maxValue;
	}

	public void setMinValue(byte value) {
		this.minValue = value;
	}

	public void setMaxValue(byte value) {
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
			this.setMinValue(Byte.parseByte(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public void setMaxFromString(String s) {
		try {
			this.setMaxValue(Byte.parseByte(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.BYTE;
	}

	@Override
	public void setMaxDoubleValue(double v) {
		this.maxValue = (byte) v;
	}

	@Override
	public void setMinDoubleValue(double v) {
		this.minValue = (byte) v;
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(Map.of(ops.createString("min"), ops.createByte(this.minValue), ops.createString("max"), ops.createByte(this.maxValue)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Map<S, S> map = ops.getMapValues(object).getOrThrow().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
		this.minValue = ops.getNumberValue(map.get(ops.createString("min"))).getOrThrow().byteValue();
		this.maxValue = ops.getNumberValue(map.get(ops.createString("max"))).getOrThrow().byteValue();
	}
}
