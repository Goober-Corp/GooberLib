package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IdentifierOption extends BaseOption<IdentifierOption> {
	private final Identifier defaultValue;
	private Identifier value;

	public IdentifierOption(Text name, Text description, Identifier defaultValue, WidgetProvider provider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public IdentifierOption(String name, String description, Identifier defaultValue) {
		this(Text.literal(name), Text.literal(description), defaultValue, null);
	}

	public IdentifierOption(String name, String description, Identifier defaultValue, WidgetProvider provider) {
		this(Text.literal(name), Text.literal(description), defaultValue, provider);
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
		this.setValue(Identifier.of(newValue));
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
