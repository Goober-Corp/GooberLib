package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import net.minecraft.network.chat.Component;

public class EnumOption<T extends Enum<T>> extends CycleOption<T> {
	public EnumOption(CharSequence name, Function<CycleOption<T>, CharSequence> description, T defaultValue, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider, Function<T, CharSequence> displayNameProvider) {
		super(name, description, defaultValue, List.of(enumClass.getEnumConstants()), provider, displayNameProvider == null ? t -> Component.nullToEmpty(t.name().replace("_", " ").toLowerCase(Locale.ROOT)) : displayNameProvider);
	}

	public EnumOption(CharSequence name, CharSequence description, T defaultValue, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> description, defaultValue, enumClass, provider, displayNameProvider);
	}

	public EnumOption(CharSequence name, CharSequence description, T defaultValue, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider) {
		this(name, _ -> description, defaultValue, enumClass, provider, null);
	}

	public EnumOption(CharSequence name, CharSequence description, T defaultValue, Class<T> enumClass, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> description, defaultValue, enumClass, null, displayNameProvider);
	}

	public EnumOption(CharSequence name, CharSequence description, T defaultValue, Class<T> enumClass) {
		this(name, _ -> description, defaultValue, enumClass, null, null);
	}

	public EnumOption(CharSequence name, CharSequence description, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> description, enumClass.getEnumConstants()[0], enumClass, provider, displayNameProvider);
	}

	public EnumOption(CharSequence name, CharSequence description, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider) {
		this(name, _ -> description, enumClass.getEnumConstants()[0], enumClass, provider, null);
	}

	public EnumOption(CharSequence name, CharSequence description, Class<T> enumClass, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> description, enumClass.getEnumConstants()[0], enumClass, null, displayNameProvider);
	}

	public EnumOption(CharSequence name, CharSequence description, Class<T> enumClass) {
		this(name, _ -> description, enumClass.getEnumConstants()[0], enumClass, null, null);
	}

	public EnumOption(CharSequence name, T defaultValue, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> "", defaultValue, enumClass, provider, displayNameProvider);
	}

	public EnumOption(CharSequence name, T defaultValue, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider) {
		this(name, _ -> "", defaultValue, enumClass, provider, null);
	}

	public EnumOption(CharSequence name, T defaultValue, Class<T> enumClass, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> "", defaultValue, enumClass, null, displayNameProvider);
	}

	public EnumOption(CharSequence name, T defaultValue, Class<T> enumClass) {
		this(name, _ -> "", defaultValue, enumClass, null, null);
	}

	public EnumOption(CharSequence name, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> "", enumClass.getEnumConstants()[0], enumClass, provider, displayNameProvider);
	}

	public EnumOption(CharSequence name, Class<T> enumClass, WidgetProvider<CycleOption<T>> provider) {
		this(name, _ -> "", enumClass.getEnumConstants()[0], enumClass, provider, null);
	}

	public EnumOption(CharSequence name, Class<T> enumClass, Function<T, CharSequence> displayNameProvider) {
		this(name, _ -> "", enumClass.getEnumConstants()[0], enumClass, null, displayNameProvider);
	}

	public EnumOption(CharSequence name, Class<T> enumClass) {
		this(name, _ -> "", enumClass.getEnumConstants()[0], enumClass, null, null);
	}
}
