package com.goobercorp.gooberlib.option.individual.java;

import com.goobercorp.gooberlib.interfaces.AdvanceableOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class CycleOption<T> extends BaseOption<CycleOption<T>> implements AdvanceableOption<CycleOption<T>> {
	private final T defaultValue;
	private final List<T> options;
	private final Function<T, CharSequence> displayNameProvider;
	/// @implNote Modifying this value directly will *not* trigger .onChange()
	public T value;

	public CycleOption(CharSequence name, Function<CycleOption<T>, CharSequence> description, T defaultValue, List<T> options, WidgetProvider<CycleOption<T>> provider, @NotNull Function<T, CharSequence> displayNameProvider) {
		super(name, description, provider);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.options = options;
		this.displayNameProvider = displayNameProvider;
	}

	@SafeVarargs
	public CycleOption(String name, String description, Function<T, CharSequence> displayNameProvider, T... options) {
		this(name, _ -> description, options[0], List.of(options), null, displayNameProvider);
	}


	@SafeVarargs
	public CycleOption(String name, String description, T... options) {
		this(name, description, Object::toString, options);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createInt(options.indexOf(value));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		this.value = this.options.get(ops.getNumberValue(object).getOrThrow().intValue());
	}

	public T getValue() {
		return value;
	}

	public void setValue(T newValue) {
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

	public T getDefaultValue() {
		return defaultValue;
	}

	public Function<CycleOption<T>, CharSequence> getDisplayNameProvider() {
		return o -> displayNameProvider.apply(o.value);
	}

	public void advance() {
		setValue(options.get((options.indexOf(getValue()) + 1) % options.size()));
	}

	public void regress() {
		int index = options.indexOf(getValue()) - 1;
		setValue(options.get(index == -1 ? options.size() - 1 : index));
	}
}
