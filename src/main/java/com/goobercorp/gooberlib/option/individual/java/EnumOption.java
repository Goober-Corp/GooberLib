package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class EnumOption<T extends Enum<T>> extends CycleOption<T> {
	public EnumOption(Text name, Text description, T defaultValue, Class<T> enumClass, WidgetProvider provider, Function<T, Text> displayNameProvider) {
		super(name, description, defaultValue, List.of(enumClass.getEnumConstants()), provider, displayNameProvider == null ? t -> Text.of(t.name().replace("_", " ").toLowerCase(Locale.ROOT)) : displayNameProvider);
	}

	public EnumOption(String name, String description, Class<T> enumClass) {
		this(Text.literal(name), Text.literal(description), enumClass.getEnumConstants()[0], enumClass, null, null);
	}
}
