package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public class ByteOption extends BaseOption<ByteOption> {
	private final byte defaultValue;
	private final byte min;
	private final byte max;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public byte value;

	public ByteOption(Text name, Text description, byte defaultValue, byte min, byte max, WidgetProvider<ByteOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public ByteOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), (byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, null);
	}

	public ByteOption(String name, String description, WidgetProvider<ByteOption> provider) {
		this(Text.literal(name), Text.literal(description), (byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, provider);
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
}
