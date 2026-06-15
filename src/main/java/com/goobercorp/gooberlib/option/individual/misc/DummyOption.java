package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class DummyOption<T extends BaseOption<T>> extends BaseOption<T> {
	protected DummyOption(CharSequence name, Function<T, CharSequence> description, @Nullable WidgetProvider<T> provider) {
		super(name, description, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.empty();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
	}

	@Override
	public void resetToDefault() {
	}
}
