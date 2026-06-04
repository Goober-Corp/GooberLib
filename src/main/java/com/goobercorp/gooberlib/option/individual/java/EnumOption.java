package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.network.chat.Component;

public class EnumOption<T extends Enum<T>> extends CycleOption<T> {
	public EnumOption(Component name, Function<CycleOption<T>, Component> description, T defaultValue, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider, Function<T, Component> displayNameProvider) {
		super(name, description, defaultValue, List.of(enumClass.getEnumConstants()), provider, displayNameProvider == null ? t -> Component.nullToEmpty(t.name().replace("_", " ").toLowerCase(Locale.ROOT)) : displayNameProvider);
	}

	public EnumOption(String name, String description, Class<T> enumClass) {
		this(Component.literal(name), _ -> Component.literal(description), enumClass.getEnumConstants()[0], enumClass, null, null);
	}
}
