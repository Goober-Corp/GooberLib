package com.goobercorp.gooberlib.builder.v3;

import com.goobercorp.gooberlib.interfaces.ValueChangeCallback;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.text.Text;

public abstract class BaseOption<V extends BaseOption<V>> implements Option<V> {
	protected Text name;
	protected Text description;
	private ValueChangeCallback<V> callback;
	private final WidgetProvider provider;

	protected BaseOption(Text name, Text description, WidgetProvider provider) {
		this.name = name;
		this.description = description;
		this.provider = provider;
	}

	@Override
	public Text name() {
		return name;
	}

	@Override
	public Text description() {
		return description;
	}

	@Override
	public void onChange() {
		//noinspection unchecked
		this.callback.onValueChanged((V) this);
	}

	@Override
	public void setOnValueChange(ValueChangeCallback<V> t) {
		this.callback = t;
	}

	@Override
	public WidgetProvider getWidgetProvider() {
		return this.provider;
	}
}
