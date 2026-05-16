package com.goobercorp.gooberlib.builder.v3.individual.java;

import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public class StringOption extends BaseOption<StringOption> {
	private final String defaultValue;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public String value;

	public StringOption(Text name, Text description, String defaultValue, WidgetProvider provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public StringOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), "", null);
	}

	public StringOption(String name, String description, WidgetProvider provider) {
		this(Text.literal(name), Text.literal(description), "", provider);
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
