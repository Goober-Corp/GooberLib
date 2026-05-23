package com.goobercorp.gooberlib.option;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class BaseOption<T extends BaseOption<T>> implements Option<T> {
	protected Text name;
	protected Function<T, Text> description;
	private ValueChangeCallback<T> callback;
	private final WidgetProvider<T> provider;

	protected BaseOption(Text name, Function<T, Text> description, @Nullable WidgetProvider<T> provider) {
		this.name = name;
		this.description = description;
		// todo: this needs testing for EnumOption due to it being a generic class
		@SuppressWarnings("unchecked")
		Class<T> optionClass = (Class<T>) this.getClass();
		this.provider = provider == null
				? GooberLibApi.getDefaultWidgetProvider(optionClass)
				: provider;
	}

	@Override
	public Text name() {
		return name;
	}

	@Override
	public Function<T, Text> description() {
		return description;
	}

	@Override
	public Text getDescription() {
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
	public ClickableWidget makeWidget(int x, int y, double width, double height) {
		return this.getWidgetProvider().makeWidget(thisT(), x, y, width, height);
	}

	private T thisT() {
		//noinspection unchecked
		return (T) this;
	}
}
