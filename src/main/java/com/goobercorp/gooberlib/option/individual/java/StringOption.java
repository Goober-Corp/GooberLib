package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.function.Function;

public class StringOption extends BaseOption<StringOption> {
	private final String defaultValue;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public String value;

	public StringOption(Text name, Function<StringOption, Text> description, String defaultValue, WidgetProvider<StringOption> provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public StringOption(String name, String description) {
		this(Text.literal(name), _ -> Text.literal(description), "", null);
	}

	public StringOption(String name, String description, WidgetProvider<StringOption> provider) {
		this(Text.literal(name), _ -> Text.literal(description), "", provider);
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
