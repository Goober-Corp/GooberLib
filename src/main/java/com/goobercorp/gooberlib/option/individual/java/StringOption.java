package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

public class StringOption extends BaseOption<StringOption> {
	private final String defaultValue;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public String value;

	public StringOption(CharSequence name, Function<StringOption, CharSequence> description, String defaultValue, WidgetProvider<StringOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public StringOption(CharSequence name, Function<StringOption, CharSequence> description, String defaultValue) {
		this(name, description, defaultValue, null);
	}

	public StringOption(CharSequence name, CharSequence description, String defaultValue, WidgetProvider<StringOption> provider) {
		this(name, _ -> description, defaultValue, provider);
	}

	public StringOption(CharSequence name, CharSequence description, String defaultValue) {
		this(name, _ -> description, defaultValue, null);
	}

	public StringOption(CharSequence name, Function<StringOption, CharSequence> description, WidgetProvider<StringOption> provider) {
		this(name, description, "", provider);
	}

	public StringOption(CharSequence name, Function<StringOption, CharSequence> description) {
		this(name, description, "", null);
	}

	public StringOption(CharSequence name, CharSequence description, WidgetProvider<StringOption> provider) {
		this(name, _ -> description, "", provider);
	}

	public StringOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, "", null);
	}

	public StringOption(CharSequence name) {
		this(name, _ -> "", "", null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createString(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getStringValue(object)
				.getOrThrow();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String newValue) {
		if (!this.value.equals(newValue)) {
			this.value = newValue;
			this.onChange();
		}
	}

	public void resetToDefault() {
		if (!this.value.equals(this.defaultValue)) {
			setValue(this.defaultValue);
		}
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}
