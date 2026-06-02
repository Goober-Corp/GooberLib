package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.clamp;

public class LongOption extends BaseOption<LongOption> implements NumberOption<LongOption> {
	private final long defaultValue;
	private final long min;
	private final long max;
	public long value;

	public LongOption(Text name, Function<LongOption, Text> description, long defaultValue, long min, long max, WidgetProvider<LongOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public LongOption(String name, String description) {
		this(Text.literal(name), _ -> Text.literal(description), 0, Long.MIN_VALUE, Long.MAX_VALUE, null);
	}

	public LongOption(String name, String description, WidgetProvider<LongOption> provider) {
		this(Text.literal(name), _ -> Text.literal(description), 0, Long.MIN_VALUE, Long.MAX_VALUE, provider);
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
