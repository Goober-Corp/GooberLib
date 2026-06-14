package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.util.Predicates;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;
import java.util.function.Predicate;

public class CharOption extends BaseOption<CharOption> implements NumberOption<CharOption> {
	private final char defaultValue;
	private final char min;
	private final char max;
	public char value;

	public CharOption(CharSequence name, Function<CharOption, CharSequence> description, char defaultValue, char min, char max, WidgetProvider<CharOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}

	public CharOption(CharSequence name, CharSequence description, char defaultValue, char min, char max, WidgetProvider<CharOption> provider) {
		this(name, _ -> description, defaultValue, min, max, provider);
	}

	public CharOption(CharSequence name, CharSequence description, char defaultValue, char min, char max) {
		this(name, _ -> description, defaultValue, min, max, null);
	}

	public CharOption(CharSequence name, char defaultValue, char min, char max, WidgetProvider<CharOption> provider) {
		this(name, _ -> "", defaultValue, min, max, provider);
	}

	public CharOption(CharSequence name, char defaultValue, char min, char max) {
		this(name, _ -> "", defaultValue, min, max, null);
	}

	public CharOption(CharSequence name, CharSequence description, char defaultValue, WidgetProvider<CharOption> provider) {
		this(name, _ -> description, defaultValue, Character.MIN_VALUE, Character.MAX_VALUE, provider);
	}

	public CharOption(CharSequence name, CharSequence description, char defaultValue) {
		this(name, _ -> description, defaultValue, Character.MIN_VALUE, Character.MAX_VALUE, null);
	}

	public CharOption(CharSequence name, char defaultValue, WidgetProvider<CharOption> provider) {
		this(name, _ -> "", defaultValue, Character.MIN_VALUE, Character.MAX_VALUE, provider);
	}

	public CharOption(CharSequence name, char defaultValue) {
		this(name, _ -> "", defaultValue, Character.MIN_VALUE, Character.MAX_VALUE, null);
	}

	public CharOption(CharSequence name, CharSequence description, WidgetProvider<CharOption> provider) {
		this(name, _ -> description, (char) 0, Character.MIN_VALUE, Character.MAX_VALUE, provider);
	}

	public CharOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, (char) 0, Character.MIN_VALUE, Character.MAX_VALUE, null);
	}

	public CharOption(CharSequence name, WidgetProvider<CharOption> provider) {
		this(name, _ -> "", (char) 0, Character.MIN_VALUE, Character.MAX_VALUE, provider);
	}

	public CharOption(CharSequence name) {
		this(name, _ -> "", (char) 0, Character.MIN_VALUE, Character.MAX_VALUE, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createString(String.valueOf(value));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getStringValue(object)
				.getOrThrow()
				.charAt(0);
	}

	public char getValue() {
		return value;
	}

	public void setValue(char newValue) {
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

	public char getDefaultValue() {
		return defaultValue;
	}

	public char getMin() {
		return min;
	}

	public char getMax() {
		return max;
	}

	@Override
	public Number getNumberValue() {
		return (int) this.value;
	}

	@Override
	public void setDoubleValue(double n) {
		this.value = (char) n;
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
		if (s.length() == 1) {
			this.setValue(s.charAt(0));
		} else {
			try {
				this.setValue((char) Integer.parseInt(s));
			} catch (NumberFormatException _) {
			}
		}
	}

	@Override
	public Predicate<String> getPredicate() {
		return s -> {
			if (s.length() == 1) return true;
			try {
				Integer.parseInt(s);
				return true;
			} catch (NumberFormatException _) {
				return false;
			}
		};
	}

	@Override
	public Predicate<String> getImmediatePredicate() {
		return Predicates.INTEGER_IMMEDIATE.or(s -> s.length() == 1);
	}
}
