package com.goobercorp.gooberlib.option;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class BaseOption<T extends Option<T>> implements Option<T> {
	protected Component name;
	protected Function<T, Component> description;
	private ValueChangeCallback<T> callback;
	private final WidgetProvider<T> provider;

	protected BaseOption(Component name, Function<T, Component> description, @Nullable WidgetProvider<T> provider) {
		this.name = name;
		this.description = description;
		this.provider = provider == null
				? GooberLibApi.getDefaultWidgetProvider(thisT())
				: provider;
	}

	@Override
	public Component name() {
		return name;
	}

	@Override
	public Function<T, Component> description() {
		return description;
	}

	@Override
	public Component getDescription() {
		return description.apply(thisT());
	}

	@Override
	public void onChange() {
		if (callback != null) {
			this.callback.onValueChanged(thisT());
		}
	}

	@Override
	public T setOnValueChange(ValueChangeCallback<T> t) {
		this.callback = t;
		return thisT();
	}

	@Override
	public WidgetProvider<T> getWidgetProvider() {
		if (this.provider == null)
			throw new IllegalStateException("??");
		return this.provider;
	}

	@Override
	public AbstractWidget makeWidget(int x, int y, int width, int height) {
		return this.getWidgetProvider().makeWidget(thisT(), x, y, width, height);
	}

	private T thisT() {
		//noinspection unchecked
		return (T) this;
	}
}
