package com.goobercorp.gooberlib.builder.v3.individual.java;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

public class EnumOption<T extends Enum<T>> extends BaseOption<EnumOption<T>> {
	private final T defaultValue;
	private final T[] enumConstants;
	private final Function<T, Text> displayNameProvider;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public T value;

	public EnumOption(Text name, Text description, T defaultValue, Class<T> enumClass, WidgetProvider provider, Function<T, Text> displayNameProvider) {
		//noinspection unchecked FUCK GENERICS !!!!!!!!!
		super(name, description, provider == null ? GooberLibApi.getDefaultWidgetProvider((Class<? extends Option<?>>) EnumOption.class) : provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.enumConstants = enumClass.getEnumConstants();
		if (displayNameProvider == null) {
			// todo make better
			this.displayNameProvider = t -> Text.of(t.name().replace("_", " ").toLowerCase(Locale.ROOT));
		} else {
			this.displayNameProvider = displayNameProvider;
		}
	}

	public EnumOption(String name, String description, Class<T> enumClass) {
		this(Text.literal(name), Text.literal(description), enumClass.getEnumConstants()[0], enumClass, null, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createString(value.name());
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		String name = ops.getStringValue(object)
				.getOrThrow();
		this.value = Arrays.stream(this.enumConstants).filter(t -> t.name().equals(name)).findAny().get();
	}

	public T getValue() {
		return value;
	}

	public void setValue(T newValue) {
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

	public T getDefaultValue() {
		return defaultValue;
	}

	public Function<T, Text> getDisplayNameProvider() {
		return displayNameProvider;
	}
}
