package com.goobercorp.gooberlib.option.individual.primitive;

import com.goobercorp.gooberlib.interfaces.AdvanceableOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

public class BooleanOption extends BaseOption<BooleanOption> implements AdvanceableOption<BooleanOption> {
	private final boolean defaultValue;
	/// @implNote Modifying this value directly will *not* trigger onChange()!
	public boolean value;

	public BooleanOption(CharSequence name, Function<BooleanOption, CharSequence> description, boolean defaultValue, WidgetProvider<BooleanOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public BooleanOption(CharSequence name, Function<BooleanOption, CharSequence> description, WidgetProvider<BooleanOption> provider) {
		this(name, description, false, provider);
	}

	public BooleanOption(CharSequence name, Function<BooleanOption, CharSequence> description, boolean defaultValue) {
		this(name, description, defaultValue, null);
	}

	public BooleanOption(CharSequence name, CharSequence description, boolean defaultValue, WidgetProvider<BooleanOption> provider) {
		this(name, _ -> description, defaultValue, provider);
	}

	public BooleanOption(CharSequence name, CharSequence description, boolean defaultValue) {
		this(name, _ -> description, defaultValue, null);
	}

	public BooleanOption(CharSequence name, boolean defaultValue) {
		this(name, _ -> "", defaultValue, null);
	}

	public BooleanOption(CharSequence name, Function<BooleanOption, CharSequence> description) {
		this(name, description, false, null);
	}

	public BooleanOption(CharSequence name, CharSequence description, WidgetProvider<BooleanOption> provider) {
		this(name, _ -> description, false, provider);
	}

	public BooleanOption(CharSequence name, CharSequence description) {
		this(name, _ -> description, false, null);
	}

	public BooleanOption(CharSequence name) {
		this(name, _ -> "", false, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createBoolean(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getBooleanValue(object)
				.getOrThrow();
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean newValue) {
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

	public boolean getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void advance() {
		this.setValue(!this.getValue());
	}

	@Override
	public void regress() {
		this.setValue(!this.getValue());
	}
}
