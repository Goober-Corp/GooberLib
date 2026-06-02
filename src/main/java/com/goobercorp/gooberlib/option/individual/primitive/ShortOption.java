package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.function.Function;
import java.util.function.Predicate;

public class ShortOption extends BaseOption<ShortOption> implements NumberOption<ShortOption> {
	private final short defaultValue;
	private final short min;
	private final short max;
	public short value;

	// TODO overloads for taking in default, min and max as ints, and making sure they're within bound, so the user doesn't need to do (short) 0, and can instead just do 0
	//  also needs to be done for .getValueInt(), .setValueInt(), getMinInt() etc
	//  also needs to be done for ByteOption, CharOption
	public ShortOption(Text name, Function<ShortOption, Text> description, short defaultValue, short min, short max, WidgetProvider<ShortOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public ShortOption(String name, String description) {
		this(Text.literal(name), _ -> Text.literal(description), (short) 0, Short.MIN_VALUE, Short.MAX_VALUE, null);
	}

	public ShortOption(String name, String description, WidgetProvider<ShortOption> provider) {
		this(Text.literal(name), _ -> Text.literal(description), (short) 0, Short.MIN_VALUE, Short.MAX_VALUE, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createShort(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.shortValue();
	}

	public short getValue() {
		return value;
	}

	public void setValue(short newValue) {
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

	public short getDefaultValue() {
		return defaultValue;
	}

	public short getMin() {
		return min;
	}

	public short getMax() {
		return max;
	}

	@Override
	public Number getNumberValue() {
		return this.value;
	}

	@Override
	public void setDoubleValue(double n) {
		this.value = (short) n;
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
			this.setValue(Short.parseShort(s));
		} catch (NumberFormatException _) {
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return Predicates.SHORT;
	}

	@Override
	public Predicate<String> getImmediatePredicate() {
		return Predicates.INTEGER_IMMEDIATE;
	}
}
