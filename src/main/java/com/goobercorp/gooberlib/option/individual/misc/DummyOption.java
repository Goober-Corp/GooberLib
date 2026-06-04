package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.network.chat.Component;

public abstract class DummyOption<T extends BaseOption<T>> extends BaseOption<T> {
	protected DummyOption(Component name, Function<T, Component> description, @Nullable WidgetProvider<T> provider) {
		super(name, description, provider);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.empty();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
	}
}
