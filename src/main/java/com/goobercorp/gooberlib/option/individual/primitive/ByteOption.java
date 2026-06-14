package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;
import java.util.function.Predicate;

public class ByteOption extends BaseOption<ByteOption> implements NumberOption<ByteOption> {
	private final byte defaultValue;
	private final byte min;
	private final byte max;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public byte value;

	public ByteOption(CharSequence name, Function<ByteOption, CharSequence> description, byte defaultValue, byte min, byte max, WidgetProvider<ByteOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public ByteOption(CharSequence name, CharSequence description, byte defaultValue, byte min, byte max, WidgetProvider<ByteOption> provider) {
		this(name, _ -> description, defaultValue, min, max, provider);
	}

	public ByteOption(CharSequence name, CharSequence description, byte defaultValue, byte min, byte max) {
		this(name, _ -> description, defaultValue, min, max, null);
	}

	public ByteOption(CharSequence name, byte defaultValue, byte min, byte max, WidgetProvider<ByteOption> provider) {
		this(name, _ -> "", defaultValue, min, max, provider);
	}

	public ByteOption(CharSequence name, byte defaultValue, byte min, byte max) {
		this(name, _ -> "", defaultValue, min, max, null);
	}

	public ByteOption(CharSequence name, CharSequence description, byte defaultValue, WidgetProvider<ByteOption> provider) {
		this(name, _ -> description, defaultValue, Byte.MIN_VALUE, Byte.MAX_VALUE, provider);
	}

	public ByteOption(CharSequence name, CharSequence description, byte defaultValue) {
		this(name, _ -> description, defaultValue, Byte.MIN_VALUE, Byte.MAX_VALUE, null);
	}

	public ByteOption(CharSequence name, byte defaultValue, WidgetProvider<ByteOption> provider) {
		this(name, _ -> "", defaultValue, Byte.MIN_VALUE, Byte.MAX_VALUE, provider);
	}

	public ByteOption(CharSequence name, byte defaultValue) {
		this(name, _ -> "", defaultValue, Byte.MIN_VALUE, Byte.MAX_VALUE, null);
	}

	public ByteOption(CharSequence name, CharSequence description, WidgetProvider<ByteOption> provider) {
		this(name, _ -> description, (byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, provider);
	}

	public ByteOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, (byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, null);
	}

	public ByteOption(CharSequence name, WidgetProvider<ByteOption> provider) {
		this(name, _ -> "", (byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, provider);
	}

	public ByteOption(CharSequence name) {
		this(name, _ -> "", (byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createByte(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.byteValue();
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte newValue) {
		newValue = newValue < min ? min : (newValue > max ? max : newValue);
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

	public byte getDefaultValue() {
		return defaultValue;
	}

	public byte getMin() {
		return min;
	}

	public byte getMax() {
		return max;
	}

	@Override
	public Number getNumberValue() {
		return this.value;
	}

	@Override
	public void setDoubleValue(double n) {
		this.value = (byte) n;
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
			this.setValue(Byte.parseByte(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.BYTE;
	}

	@Override
	public Predicate<String> getImmediatePredicate() {
		return Predicates.INTEGER_IMMEDIATE;
	}
}
