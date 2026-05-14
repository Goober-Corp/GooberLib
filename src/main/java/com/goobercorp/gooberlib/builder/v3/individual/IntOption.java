package com.goobercorp.gooberlib.builder.v3.individual;

import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public class IntOption extends BaseOption<Integer> {
	public int value;

	public IntOption(Text name, Text description, WidgetProvider provider) {
		super(name, description, provider);
	}

	public IntOption(String name, String description) {
		this(Text.literal(name), Text.literal(description), null);
	}

	public IntOption(String name, String description, WidgetProvider provider) {
		this(Text.literal(name), Text.literal(description), provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createInt(value);
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = ops.getNumberValue(object)
				.getOrThrow()
				.intValue();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int newValue) {
		this.value = newValue;
		this.onChange();
	}
}
