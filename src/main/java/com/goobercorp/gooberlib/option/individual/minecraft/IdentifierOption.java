package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

import net.minecraft.resources.Identifier;

public class IdentifierOption extends BaseOption<IdentifierOption> {
	private final Identifier defaultValue;
	private Identifier value;

	public IdentifierOption(CharSequence name, Function<IdentifierOption, CharSequence> description, Identifier defaultValue, WidgetProvider<IdentifierOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public IdentifierOption(CharSequence name, CharSequence description, Identifier defaultValue) {
		this(name, _ -> description, defaultValue, null);
	}

	public IdentifierOption(CharSequence name, CharSequence description, Identifier defaultValue, WidgetProvider<IdentifierOption> provider) {
		this(name, _ -> description, defaultValue, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return Identifier.CODEC.encodeStart(ops, this.value).getOrThrow();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = Identifier.CODEC.parse(ops, object).getOrThrow();
	}

	public Identifier getValue() {
		return value;
	}

	public void setValue(Identifier newValue) {
		if (!this.value.equals(newValue)) {
			this.value = newValue;
			this.onChange();
		}
	}

	public void setValueFromString(String newValue) {
		this.setValue(Identifier.parse(newValue));
	}

	public void resetToDefault() {
		if (!this.value.equals(this.defaultValue)) {
			setValue(this.defaultValue);
		}
	}

	public Identifier getDefaultValue() {
		return defaultValue;
	}
}
